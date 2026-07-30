package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.api.fluid.SingleFluidStorage;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.ModBlastFurnaceCraftingRecipe;
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
import mezz.jei.api.recipe.types.IRecipeType;
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
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class BlastFurnaceCategory implements IRecipeCategory<RecipeHolder<ModBlastFurnaceCraftingRecipe>> {

    public static final IRecipeHolderType<ModBlastFurnaceCraftingRecipe> TYPE =
            IRecipeHolderType.create(AlchemyRecipeType.MOD_BLAST_FURNACE.type.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final GuiFluidTankRenderer fluidRenderer;
    private final SingleFluidStorage emptyFluid;

    public BlastFurnaceCategory(IGuiHelper helper) {
        Identifier texture = Helper.id("textures/gui/container/machine/blastfurnace.png");

        background = helper.createDrawable(texture, 0, 0, 175, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(GrowableOresBlocks.BLAST_FURNACE_BLOCK.get()));
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/blastfurnace.png"),
                189, 0, 26, 16).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
        this.fluidRenderer = new GuiFluidTankRenderer(1000, true, 16, 50);
        this.emptyFluid = new SingleFluidStorage() {

            @Override
            protected int getCapacity(FluidResource variant) {
                return 1000;
            }

            {
                this.variant = FluidResource.EMPTY;
                this.amount = 0;
            }
        };
    }

    @Override
    public IRecipeType<RecipeHolder<ModBlastFurnaceCraftingRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.Blast_Furnace);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ModBlastFurnaceCraftingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 34)
                .add(recipe.value().ingredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 26)
                .add(recipe.value().output());

        recipe.value().output2().ifPresent(output ->
                builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 44).add(output));

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

    private SingleFluidStorage getRecipeFluidStorage(RecipeHolder<ModBlastFurnaceCraftingRecipe> recipe) {
        try {
            Identifier fluidId = recipe.value().requiredFluid();
            int amount = recipe.value().requiredFluidAmount();
            Fluid fluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(Fluids.EMPTY);
            return new SingleFluidStorage() {

                @Override
                protected int getCapacity(FluidResource variant) {
                    return 1000;
                }

                {
                    this.variant = FluidResource.of(fluid);
                    this.amount = amount;
                }
            };
        } catch (Throwable ignored) {
        }
        return emptyFluid;
    }

    @Override
    public void draw(RecipeHolder<ModBlastFurnaceCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        arrow.draw(guiGraphics, 73, 34);

        SingleFluidStorage storage = getRecipeFluidStorage(recipe);
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
            int y = 34 + 16 - 8 - 1;
            guiGraphics.text(font, text, x, y, 0xFFFFFFFF, true);
        }
    }
}
