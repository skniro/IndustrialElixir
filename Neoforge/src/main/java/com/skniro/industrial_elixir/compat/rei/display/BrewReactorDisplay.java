package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.BrewReactorCraftingRecipe;
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

public class BrewReactorDisplay extends BasicDisplay {

    private int count;
    private Optional<Identifier> fluid;
    private Optional<Integer> fluidamt;

    public static final CategoryIdentifier<BrewReactorDisplay>
            BREW_REACTOR = CategoryIdentifier.of(IndustrialElixir.MOD_ID, "brew_reactor");

    public static final DisplaySerializer<BrewReactorDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BrewReactorDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BrewReactorDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(BrewReactorDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("count").forGetter(BrewReactorDisplay::getCount),
                    Identifier.CODEC.optionalFieldOf("fluid").forGetter(BrewReactorDisplay::getFluid),
                    Codec.INT.optionalFieldOf("fluid_amount").forGetter(BrewReactorDisplay::getFluidAmount)
            ).apply(instance, BrewReactorDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    BrewReactorDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    BrewReactorDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    BrewReactorDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    BrewReactorDisplay::getCount,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    BrewReactorDisplay::getFluid,
                    ByteBufCodecs.optional(ByteBufCodecs.INT),
                    BrewReactorDisplay::getFluidAmount,
                    BrewReactorDisplay::new
            )
    );



    public BrewReactorDisplay(BrewReactorCraftingRecipe recipe) {
        this(recipe.ingredient2() != null ?
                        List.of(EntryIngredients.ofIngredient(recipe.ingredient()),
                                EntryIngredients.ofIngredient(recipe.ingredient2())) :
                        List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())),
                Optional.empty(),
                recipe.requiredCount(),
                recipe.requiredFluid(),
                recipe.requiredFluidAmount()
        );
    }

    public BrewReactorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int count, Optional<Identifier> fluid, Optional<Integer> fluidamt) {
        super(inputs, outputs, location);
        this.count = count;
        this.fluid = fluid;
        this.fluidamt = fluidamt;
    }

    public BrewReactorDisplay(RecipeHolder<BrewReactorCraftingRecipe> recipe) {
        super(
                recipe.value().ingredient2() != null ?
                        List.of(EntryIngredients.ofIngredient(recipe.value().ingredient()),
                                EntryIngredients.ofIngredient(recipe.value().ingredient2())) :
                        List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),

                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output().create()))));
        this.count = recipe.value().requiredCount();
        this.fluid = recipe.value().requiredFluid();
        this.fluidamt = recipe.value().requiredFluidAmount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BREW_REACTOR;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public int getCount() {
        return count;
    }

    public Optional<Identifier> getFluid() {
        return fluid;
    }

    public Optional<Integer> getFluidAmount() {
        return fluidamt;
    }
}