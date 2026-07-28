package com.skniro.industrial_elixir.compat.jei.category;


import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.CuttingCraftingRecipe;
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


public class BlockCutterCategory implements IRecipeCategory<RecipeHolder<CuttingCraftingRecipe>> {
    public static final IRecipeHolderType<CuttingCraftingRecipe> TYPE =
            IRecipeHolderType.create(AlchemyRecipeType.CUTTING.type.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyBar;
    private final IDrawableAnimated arrow;

    public BlockCutterCategory(IGuiHelper helper) {
        Identifier texture = Helper.id("textures/gui/container/machine/block_cutter.png");
        background = helper.createDrawable(texture, 0, 0, 175, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(GrowableOresBlocks.CUTTING_Block.get()));
        energyBar = helper.createDrawable(texture, 176, 0, 13, 16);
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/block_cutter.png"), 190, 0, 48, 19).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public IRecipeHolderType<CuttingCraftingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.CUTTING);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CuttingCraftingRecipe> recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 13)
                .add(recipe.value().ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 34)
                .add(recipe.value().output());
    }

    @Override
    public void draw(RecipeHolder<CuttingCraftingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        energyBar.draw(guiGraphics, 22, 32);
        arrow.draw(guiGraphics, 70, 33);

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