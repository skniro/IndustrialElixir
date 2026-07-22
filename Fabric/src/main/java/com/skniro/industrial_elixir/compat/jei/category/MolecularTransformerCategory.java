package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.MolecularTransformerCraftingRecipe;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class MolecularTransformerCategory implements IRecipeCategory<RecipeHolder<MolecularTransformerCraftingRecipe>> {

    public static final IRecipeHolderType<MolecularTransformerCraftingRecipe> TYPE =
            IRecipeHolderType.create(AlchemyRecipeType.MOLECULAR_TRANSFORMER.type);

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated arrow;

    public MolecularTransformerCategory(IGuiHelper helper) {
        Identifier texture = Helper.id( "textures/gui/container/machine/molecular_transformer.png");

        background = helper.createDrawable(texture, 0, 0, 175, 82);

        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(GrowableOresBlocks.MolecularTransformerBlock));

        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/molecular_transformer.png"), 194, 2, 23, 12).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public IRecipeType<RecipeHolder<MolecularTransformerCraftingRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.MolecularTransformer);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MolecularTransformerCraftingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 33)
                .add(recipe.value().ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 34)
                .add(recipe.value().output());
    }

    @Override
    public void draw(RecipeHolder<MolecularTransformerCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);

        arrow.draw(guiGraphics, 74, 37);

        int count = recipe.value().requiredCount();

        if (count > 1) {
            Font font = Minecraft.getInstance().font;
            String text = String.valueOf(count);

            int x = 52 + 16 - font.width(text) - 1;
            int y = 13 + 16 - 8 - 1;

            guiGraphics.text(font, text, x, y, 0xFFFFFFFF, true);
        }

        long energy = recipe.value().getEnergyRequired();

        Font font = Minecraft.getInstance().font;
        String text = "Energy:" + energy;

        int x = 72 + 16 - font.width(text) - 1;
        int y = 53 + 16 - 8 - 1;

        guiGraphics.text(font, text, x, y, 0xFFFFFFFF, true);
    }
}