package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.ExtractorCraftingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import java.util.List;
import java.util.Optional;

public class ExtractorDisplay extends BasicDisplay {
    private int count;

    public static final CategoryIdentifier<ExtractorDisplay> Extractor =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "extractor");

    public static final DisplaySerializer<ExtractorDisplay> SERIALIZER = BasicDisplay.Serializer.<ExtractorDisplay>of(
            (inputs, outputs, location, tag) -> new ExtractorDisplay(inputs, outputs, location, tag.getInt("count")),
            (display, tag) -> tag.putInt("count", display.getCount())
    );

    public ExtractorDisplay(ExtractorCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())), Optional.empty(), recipe.requiredCount());
        this.count = recipe.requiredCount();
    }

    public ExtractorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, int count) {
        super(inputs, outputs, location);
        this.count = count;
    }

    public ExtractorDisplay(RecipeHolder<ExtractorCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output()))));
        this.count = recipe.value().requiredCount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Extractor;
    }

    public int getCount() {
        return count;
    }
}