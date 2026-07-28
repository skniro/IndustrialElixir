package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.AlchemyCraftingRecipe;
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

public class GrowableOresDisplay extends BasicDisplay {
    public static final CategoryIdentifier<GrowableOresDisplay> GrowableOres =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "cane_converter");

    public static final DisplaySerializer<GrowableOresDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(GrowableOresDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(GrowableOresDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(GrowableOresDisplay::getDisplayLocation)
            ).apply(instance, GrowableOresDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    GrowableOresDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    GrowableOresDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    GrowableOresDisplay::getDisplayLocation,
                    GrowableOresDisplay::new
            )
    );

    public GrowableOresDisplay(AlchemyCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())), Optional.empty());
    }

    public GrowableOresDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    public GrowableOresDisplay(RecipeHolder<AlchemyCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output().create()))));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return GrowableOres;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}