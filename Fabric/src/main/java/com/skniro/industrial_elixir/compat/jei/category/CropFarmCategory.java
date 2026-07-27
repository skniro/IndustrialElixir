package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.CropFarmCraftingRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CropFarmCategory implements IRecipeCategory<RecipeHolder<CropFarmCraftingRecipe>> {
    public static final IRecipeHolderType<CropFarmCraftingRecipe> TYPE =
            IRecipeHolderType.create(AlchemyRecipeType.CROP_FARM.type);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyBar;
    private final IDrawableAnimated arrow;

    public CropFarmCategory(IGuiHelper helper) {
        Identifier texture = Helper.id("textures/gui/container/machine/crop_farm.png");
        background = helper.createDrawable(texture, 0, 0, 175, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(GrowableOresBlocks.CROP_FARM_Block));
        energyBar = helper.createDrawable(texture, 176, 0, 13, 16);
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/crop_farm.png"),
                189, 0, 24, 16).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public IRecipeHolderType<CropFarmCraftingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.CropFarm);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CropFarmCraftingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 33)
                .add(recipe.value().ingredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 16)
                .add(recipe.value().output());

        recipe.value().output2().ifPresent(output ->
                builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 34).add(output));

        recipe.value().output3().ifPresent(output ->
                builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 52).add(output));
    }

    @Override
    public void draw(RecipeHolder<CropFarmCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        energyBar.draw(guiGraphics, 129, 45);
        arrow.draw(guiGraphics, 70, 34);

        int count = recipe.value().requiredCount();
        if (count > 1) {
            Font font = Minecraft.getInstance().font;
            String text = String.valueOf(count);
            int x = 52 + 16 - font.width(text) - 1;
            int y = 33 + 16 - 8 - 1;
            guiGraphics.text(font, text, x, y, 0xFFFFFFFF, true);
        }
    }
}
