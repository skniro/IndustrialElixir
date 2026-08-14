package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.MolecularTransformerCraftingRecipe;
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

public class MolecularTransformerDisplay extends BasicDisplay {
    private int count;
    private long energy;

    public static final CategoryIdentifier<MolecularTransformerDisplay> MolecularTransformer =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "molecular_transformer");

    public static final DisplaySerializer<MolecularTransformerDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(MolecularTransformerDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(MolecularTransformerDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(MolecularTransformerDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("count").forGetter(MolecularTransformerDisplay::getCount),
                    Codec.LONG.fieldOf("energy").forGetter(MolecularTransformerDisplay::getEnergy)
            ).apply(instance, MolecularTransformerDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    MolecularTransformerDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    MolecularTransformerDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    MolecularTransformerDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    MolecularTransformerDisplay::getCount,
                    ByteBufCodecs.LONG,
                    MolecularTransformerDisplay::getEnergy,
                    MolecularTransformerDisplay::new
            )
    );

    public MolecularTransformerDisplay(MolecularTransformerCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())), Optional.empty(), recipe.requiredCount(), recipe.getEnergyRequired());
        this.count = recipe.requiredCount();
        this.energy = recipe.getEnergyRequired();
    }

    public MolecularTransformerDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int count, long energy) {
        super(inputs, outputs, location);
        this.count = count;
        this.energy = energy;
    }

    public MolecularTransformerDisplay(RecipeHolder<MolecularTransformerCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output().create()))));
        this.count = recipe.value().requiredCount();
        this.energy = recipe.value().getEnergyRequired();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MolecularTransformer;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public int getCount() {
        return count;
    }

    public long getEnergy() {
        return energy;
    }
}