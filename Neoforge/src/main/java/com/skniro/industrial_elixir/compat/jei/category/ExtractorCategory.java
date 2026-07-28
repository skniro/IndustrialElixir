package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.ExtractorCraftingRecipe;
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

public class ExtractorCategory implements IRecipeCategory<RecipeHolder<ExtractorCraftingRecipe>> {

    public static final IRecipeHolderType<ExtractorCraftingRecipe> TYPE =
            IRecipeHolderType.create(AlchemyRecipeType.EXTRACTOR.type);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyBar;
    private final IDrawableAnimated arrow;

    public ExtractorCategory(IGuiHelper helper) {
        Identifier texture = Helper.id( "textures/gui/container/machine/extractor.png");

        background = helper.createDrawable(texture, 0, 0, 175, 82);

        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(GrowableOresBlocks.Extractor_Block.get()));

        energyBar = helper.createDrawable(texture, 176, 0, 13, 16);
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/extractor.png"), 190, 0, 23, 14).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public IRecipeType<RecipeHolder<ExtractorCraftingRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.Extractor);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ExtractorCraftingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 13)
                .add(recipe.value().ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 33)
                .add(recipe.value().output());
    }

    @Override
    public void draw(RecipeHolder<ExtractorCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);

        energyBar.draw(guiGraphics, 52, 31);

        arrow.draw(guiGraphics, 75, 35);

        int count = recipe.value().requiredCount();

        if (count > 1) {
            Font font = Minecraft.getInstance().font;
            String text = String.valueOf(count);

            int x = 52 + 16 - font.width(text) - 1;
            int y = 13 + 16 - 8 - 1;

            guiGraphics.text(font, text, x, y, 0xFFFFFFFF, true);
        }
    }
}