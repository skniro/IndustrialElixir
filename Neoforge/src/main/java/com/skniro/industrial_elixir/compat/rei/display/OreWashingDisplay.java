package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.OreWashingCraftingRecipe;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OreWashingDisplay extends BasicDisplay {
    private final int count;
    private final Identifier fluid;
    private final int fluidAmount;

    public static final CategoryIdentifier<OreWashingDisplay> ORE_WASHING =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "ore_washing");

    public static final DisplaySerializer<OreWashingDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BasicDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BasicDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(BasicDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("count").forGetter(d -> d.count),
                    Identifier.CODEC.fieldOf("fluid").forGetter(d -> d.fluid),
                    Codec.INT.fieldOf("fluid_amount").forGetter(d -> d.fluidAmount)
            ).apply(instance, OreWashingDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    BasicDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    BasicDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    BasicDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    d -> d.count,
                    Identifier.STREAM_CODEC,
                    d -> d.fluid,
                    ByteBufCodecs.INT,
                    d -> d.fluidAmount,
                    OreWashingDisplay::new
            )
    );

    public OreWashingDisplay(OreWashingCraftingRecipe recipe) {
        this(
                List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                buildOutputs(recipe),
                Optional.empty(),
                recipe.requiredCount(),
                recipe.requiredFluid(),
                recipe.requiredFluidAmount()
        );
    }

    public OreWashingDisplay(RecipeHolder<OreWashingCraftingRecipe> recipe) {
        super(
                List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                buildOutputs(recipe.value()));
        this.count = recipe.value().requiredCount();
        this.fluid = recipe.value().requiredFluid();
        this.fluidAmount = recipe.value().requiredFluidAmount();
    }

    public OreWashingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int count, Identifier fluid, int fluidAmount) {
        super(inputs, outputs, location);
        this.count = count;
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
    }

    private static List<EntryIngredient> buildOutputs(OreWashingCraftingRecipe recipe) {
        List<EntryIngredient> outputs = new ArrayList<>();
        outputs.add(EntryIngredient.of(EntryStacks.of(recipe.output().create())));
        recipe.output2().ifPresent(o -> outputs.add(EntryIngredient.of(EntryStacks.of(o.create()))));
        recipe.output3().ifPresent(o -> outputs.add(EntryIngredient.of(EntryStacks.of(o.create()))));
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ORE_WASHING;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public int getCount() {
        return count;
    }

    public Identifier getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }
}
