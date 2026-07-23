package com.skniro.industrial_elixir.api.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

// CREDIT: https://github.com/mezz/JustEnoughItems by mezz
// Under MIT-License: https://github.com/mezz/JustEnoughItems/blob/1.18/LICENSE.txt
public interface IIngredientRenderer<T> {
	/**
	 * Renders an ingredient.
	 *
	 * @param guiGraphics The current {@link GuiGraphicsExtractor} for rendering the ingredient.
	 * @param ingredient the ingredient to render.
	 *
	 * @since 9.3.0
	 */
	void render(GuiGraphicsExtractor guiGraphics, T ingredient);

	/**
	 * Renders an ingredient at a specific location.
	 *
	 * @param guiGraphics The current {@link GuiGraphicsExtractor} for rendering the ingredient.
	 * @param ingredient the ingredient to render.
	 * @param posX       the x offset for rendering this ingredient
	 * @param posY       the y offset for rendering this ingredient
	 *
	 * @since 19.5.5
	 */
	default void render(GuiGraphicsExtractor guiGraphics, T ingredient, int posX, int posY) {
		var poseStack = guiGraphics.pose();
		poseStack.pushMatrix();
		{
			poseStack.translate(posX, posY);
			render(guiGraphics, ingredient);
		}
		poseStack.popMatrix();
	}


	/**
	 * Get the tooltip text for this ingredient. JEI searches tooltips based on this.
	 *
	 * @param ingredient  The ingredient to get the tooltip for.
	 * @param tooltipFlag Whether to show advanced information on item tooltips, toggled by F3+H
	 * @return The tooltip text for the ingredient.
	 */
	List<Component> getTooltip(T ingredient, TooltipFlag tooltipFlag);


	/**
	 * Get the tooltip font renderer for this ingredient. JEI renders the tooltip based on this.
	 *
	 * @param minecraft  The minecraft instance.
	 * @param ingredient The ingredient to get the tooltip for.
	 * @return The font renderer for the ingredient.
	 */
	default Font getFontRenderer(Minecraft minecraft, T ingredient) {
		return minecraft.font;
	}

	/**
	 * Get the width of the ingredient drawn on screen by this renderer.
	 *
	 * @since 9.3.0
	 */
	default int getWidth() {
		return 16;
	}

	/**
	 * Get the height of the ingredient drawn on screen by this renderer.
	 *
	 * @since 9.3.0
	 */
	default int getHeight() {
		return 16;
	}
}