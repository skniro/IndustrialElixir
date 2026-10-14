package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.api.fluid.SingleFluidStorage;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.BrewReactorCraftingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import com.skniro.industrial_elixir.api.renderer.GuiFluidTankRenderer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

public class BrewReactorCategory implements IRecipeCategory<RecipeHolder<BrewReactorCraftingRecipe>> {
    public static final RecipeType<RecipeHolder<BrewReactorCraftingRecipe>> TYPE =
            RecipeType.createFromVanilla(AlchemyRecipeType.BREW_REACTOR.type.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyBar;
    private final IDrawableAnimated arrow;
    private final GuiFluidTankRenderer fluidRenderer;
    private final SingleFluidStorage emptyFluid;

    public BrewReactorCategory(IGuiHelper helper) {
        ResourceLocation texture = Helper.id("textures/gui/container/machine/brewreactor.png");
        background = helper.createDrawable(texture, 0, 0, 175, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(GrowableOresBlocks.Brew_Reactor_BLOCK.get()));
        energyBar = helper.createDrawable(texture, 176, 0, 13, 16);
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/brewreactor.png"), 189, 0, 11, 16).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
        // fluid renderer for JEI preview (empty storage)
        this.fluidRenderer = new GuiFluidTankRenderer(1000, true, 16, 50);
        this.emptyFluid = new SingleFluidStorage() {
            @Override
            protected int getCapacity(FluidStack variant) {
                return 1000;
            }
            {
                setFluid(FluidStack.EMPTY);
            }
        };
    }

    @Override
    public RecipeType<RecipeHolder<BrewReactorCraftingRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.BrewReactor);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BrewReactorCraftingRecipe> recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 13)
                .addIngredients(recipe.value().ingredient());

        if (recipe.value().ingredient2() != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 52, 49)
                    .addIngredients(recipe.value().ingredient2());
        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, 52, 49).addItemStack(ItemStack.EMPTY);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 34)
                .addItemStack(recipe.value().output());

        try {
            Optional<ResourceLocation> fid = recipe.value().requiredFluid();
            if (fid.isPresent()) {ResourceLocation bucketId = ResourceLocation.fromNamespaceAndPath(fid.get().getNamespace(), fid.get().getPath() + "_cell");
                Item bucket = BuiltInRegistries.ITEM.getOptional(bucketId).orElse(null);

                if (bucket != null && bucket != Items.AIR) {
                    builder.addSlot(RecipeIngredientRole.INPUT, 8, 5).addItemStack(new ItemStack(bucket));
                }
            }
        } catch (Throwable ignored) {
        }
    }

    private SingleFluidStorage getRecipeFluidStorage(RecipeHolder<BrewReactorCraftingRecipe> recipe) {
        try {
            Optional<ResourceLocation> fid = recipe.value().requiredFluid();

            Optional<Integer> famt = recipe.value().requiredFluidAmount();

            if (fid.isPresent()) {Fluid fluid = BuiltInRegistries.FLUID.getOptional(fid.get()).orElse(Fluids.EMPTY);
                int amount = famt.orElse(1000);
                return new SingleFluidStorage() {
                    @Override
                    protected int getCapacity(FluidStack variant) {
                        return 1000;
                    }

                    {
                        setFluid(new FluidStack(fluid, amount));
                    }
                };
            }
        } catch (Throwable ignored) {
        }

        return emptyFluid;
    }

    @Override
    public void draw(RecipeHolder<BrewReactorCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        energyBar.draw(guiGraphics, 129, 45);
        arrow.draw(guiGraphics, 79, 32);

        SingleFluidStorage storage = getRecipeFluidStorage(recipe);
        fluidRenderer.render(guiGraphics, 8, 5, storage);

        int fx = 8;
        int fy = 5;
        int fw = fluidRenderer.getWidth();
        int fh = fluidRenderer.getHeight();

        if (mouseX >= fx && mouseX <= fx + fw && mouseY >= fy && mouseY <= fy + fh) {

            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, fluidRenderer.getTooltip(storage), (int) mouseX, (int) mouseY);
        }

        int count = recipe.value().requiredCount();
        if (count > 1) {
            Font font = net.minecraft.client.Minecraft.getInstance().font;
            String text = String.valueOf(count);
            int x = 52 + 16 - font.width(text) - 1;
            int y = 13 + 16 - 8 - 1;
            guiGraphics.drawString(font, text, x, y, 0xFFFFFFFF, true);
        }
    }
}

