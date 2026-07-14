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
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import java.util.List;
import java.util.Optional;

public class ExtractorDisplay extends BasicDisplay {
    private int count;

    public static final CategoryIdentifier<ExtractorDisplay> Extractor =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "extractor");

    public static final DisplaySerializer<ExtractorDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(ExtractorDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(ExtractorDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(ExtractorDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("count").forGetter(ExtractorDisplay::getCount)
            ).apply(instance, ExtractorDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    ExtractorDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    ExtractorDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    ExtractorDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    ExtractorDisplay::getCount,
                    ExtractorDisplay::new
            )
    );

    public ExtractorDisplay(ExtractorCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())), Optional.empty(), recipe.requiredCount());
        this.count = recipe.requiredCount();
    }

    public ExtractorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int count) {
        super(inputs, outputs, location);
        this.count = count;
    }

    public ExtractorDisplay(RecipeHolder<ExtractorCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output().create()))));
        this.count = recipe.value().requiredCount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Extractor;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public int getCount() {
        return count;
    }
}