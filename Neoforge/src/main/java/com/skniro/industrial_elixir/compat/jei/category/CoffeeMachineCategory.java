package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.CoffeeMachineCraftingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class CoffeeMachineCategory implements IRecipeCategory<RecipeHolder<CoffeeMachineCraftingRecipe>> {
    public static final IRecipeHolderType<CoffeeMachineCraftingRecipe> TYPE =
            IRecipeHolderType.create(AlchemyRecipeType.COFFEE_MACHINE.type.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyBar;
    private final IDrawableAnimated arrow;
    private final GuiFluidTankRenderer fluidRenderer;

    public CoffeeMachineCategory(IGuiHelper helper) {
        Identifier texture = Helper.id("textures/gui/container/machine/coffee_machine.png");
        background = helper.createDrawable(texture, 0, 0, 175, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(GrowableOresBlocks.COFFEE_MACHINE_Block.get()));
        energyBar = helper.createDrawable(texture, 176, 0, 13, 16);
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/coffee_machine.png"),
                189, 0, 24, 16).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
        this.fluidRenderer = new GuiFluidTankRenderer(1000, true, 16, 50);
    }

    @Override
    public IRecipeHolderType<CoffeeMachineCraftingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.CoffeeMachine);
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CoffeeMachineCraftingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 15)
                .add(recipe.value().ingredient());

        recipe.value().ingredient2().ifPresent(ing ->
                builder.addSlot(RecipeIngredientRole.INPUT, 52, 33).add(ing));

        recipe.value().ingredient3().ifPresent(ing ->
                builder.addSlot(RecipeIngredientRole.INPUT, 52, 51).add(ing));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 34)
                .add(recipe.value().output());

        try {
            Identifier fluidId = recipe.value().requiredFluid();
            Identifier cellId = Identifier.fromNamespaceAndPath(fluidId.getNamespace(), fluidId.getPath() + "_cell");
            Item cell = BuiltInRegistries.ITEM.getOptional(cellId).orElse(null);
            if (cell != null && cell != Items.AIR) {
                builder.addSlot(RecipeIngredientRole.INPUT, 8, 5).add(new ItemStack(cell));
            }
        } catch (Throwable ignored) {
        }
    }

    private SingleVariantStorage<FluidVariant> getRecipeFluidStorage(RecipeHolder<CoffeeMachineCraftingRecipe> recipe) {
        Identifier fluidId = recipe.value().requiredFluid();
        int amount = recipe.value().requiredFluidAmount();
        Fluid fluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(Fluids.EMPTY);
        return new SingleVariantStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return 1000;
            }

            {
                this.variant = FluidVariant.of(fluid);
                this.amount = amount;
            }
        };
    }

    @Override
    public void draw(RecipeHolder<CoffeeMachineCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        energyBar.draw(guiGraphics, 129, 45);
        arrow.draw(guiGraphics, 70, 34);

        SingleVariantStorage<FluidVariant> storage = getRecipeFluidStorage(recipe);
        fluidRenderer.render(guiGraphics, 8, 5, storage);

        int fx = 8;
        int fy = 5;
        int fw = fluidRenderer.getWidth();
        int fh = fluidRenderer.getHeight();
        if (mouseX >= fx && mouseX <= fx + fw && mouseY >= fy && mouseY <= fy + fh) {
            guiGraphics.setComponentTooltipForNextFrame(Minecraft.getInstance().font, fluidRenderer.getTooltip(storage), (int) mouseX, (int) mouseY);
        }

        int count = recipe.value().requiredCount();
        if (count > 1) {
            Font font = Minecraft.getInstance().font;
            String text = String.valueOf(count);
            int x = 52 + 16 - font.width(text) - 1;
            int y = 15 + 16 - 8 - 1;
            guiGraphics.text(font, text, x, y, 0xFFFFFFFF, true);
        }
    }
}
