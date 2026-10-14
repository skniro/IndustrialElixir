package com.skniro.industrial_elixir.compat.jei.category;

import com.skniro.industrial_elixir.api.Helper;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.recipe.AlchemyRecipeType;
import com.skniro.industrial_elixir.recipe.machine.HeatCentrifugeCraftingRecipe;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class HeatCentrifugeCategory implements IRecipeCategory<RecipeHolder<HeatCentrifugeCraftingRecipe>> {

    public static final RecipeType<RecipeHolder<HeatCentrifugeCraftingRecipe>> TYPE =
            RecipeType.createFromVanilla(AlchemyRecipeType.HEAT_CENTRIFUGE.type.get());

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable energyBar;
    private final IDrawableAnimated arrow;

    public HeatCentrifugeCategory(IGuiHelper helper) {
        ResourceLocation texture = Helper.id("textures/gui/container/machine/heatcentrifuge.png");

        background = helper.createDrawable(texture, 0, 0, 175, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(GrowableOresBlocks.HEAT_CENTRIFUGE.get()));
        energyBar = helper.createDrawable(texture, 176, 0, 13, 16);
        this.arrow = helper.drawableBuilder(Helper.id("textures/gui/container/machine/heatcentrifuge.png"),
                189, 0, 26, 16).buildAnimated(72, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<RecipeHolder<HeatCentrifugeCraftingRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(FurnitureStrings.Heat_Centrifuge);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<HeatCentrifugeCraftingRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 34)
                .addIngredients(recipe.value().ingredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 16)
                .addItemStack(recipe.value().output());

        recipe.value().output2().ifPresent(output ->
                builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 34).addItemStack(output));

        recipe.value().output3().ifPresent(output ->
                builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 52).addItemStack(output));
    }

    @Override
    public void draw(RecipeHolder<HeatCentrifugeCraftingRecipe> recipe, IRecipeSlotsView slots, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        energyBar.draw(guiGraphics, 129, 45);
        arrow.draw(guiGraphics, 70, 34);

        int count = recipe.value().requiredCount();
        if (count > 1) {
            Font font = Minecraft.getInstance().font;
            String text = String.valueOf(count);
            int x = 52 + 16 - font.width(text) - 1;
            int y = 34 + 16 - 8 - 1;
            guiGraphics.drawString(font, text, x, y, 0xFFFFFFFF, true);
        }
    }
}
