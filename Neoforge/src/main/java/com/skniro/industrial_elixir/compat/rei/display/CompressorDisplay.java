package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.CompressorCraftingRecipe;
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

public class CompressorDisplay extends BasicDisplay {
    private int count;

    public static final CategoryIdentifier<CompressorDisplay> Compressor =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "compressor");

    public static final DisplaySerializer<CompressorDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(CompressorDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(CompressorDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(CompressorDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("count").forGetter(CompressorDisplay::getCount)
            ).apply(instance, CompressorDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    CompressorDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    CompressorDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    CompressorDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    CompressorDisplay::getCount,
                    CompressorDisplay::new
            )
    );

    public CompressorDisplay(CompressorCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())), Optional.empty(), recipe.requiredCount());
        this.count = recipe.requiredCount();
    }

    public CompressorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int count) {
        super(inputs, outputs, location);
        this.count = count;
    }

    public CompressorDisplay(RecipeHolder<CompressorCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output().create()))));
        this.count = recipe.value().requiredCount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Compressor;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public int getCount() {
        return count;
    }
}