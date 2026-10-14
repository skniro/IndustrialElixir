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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Optional;

public class BrewReactorDisplay extends BasicDisplay {

    private int count;
    private Optional<ResourceLocation> fluid;
    private Optional<Integer> fluidamt;

    public static final CategoryIdentifier<BrewReactorDisplay>
            BREW_REACTOR = CategoryIdentifier.of(IndustrialElixir.MOD_ID, "brew_reactor");

    public static final DisplaySerializer<BrewReactorDisplay> SERIALIZER = BasicDisplay.Serializer.<BrewReactorDisplay>of(
            (inputs, outputs, location, tag) -> new BrewReactorDisplay(inputs, outputs, location, tag.getInt("count"),
                    tag.contains("fluid") ? Optional.of(ResourceLocation.parse(tag.getString("fluid"))) : Optional.empty(),
                    tag.contains("fluid_amount") ? Optional.of(tag.getInt("fluid_amount")) : Optional.empty()),
            (display, tag) -> {
                tag.putInt("count", display.getCount());
                display.getFluid().ifPresent(fluid -> tag.putString("fluid", fluid.toString()));
                display.getFluidAmount().ifPresent(amount -> tag.putInt("fluid_amount", amount));
            }
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

    public BrewReactorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, int count, Optional<ResourceLocation> fluid, Optional<Integer> fluidamt) {
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

                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output()))));
        this.count = recipe.value().requiredCount();
        this.fluid = recipe.value().requiredFluid();
        this.fluidamt = recipe.value().requiredFluidAmount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BREW_REACTOR;
    }


    public int getCount() {
        return count;
    }

    public Optional<ResourceLocation> getFluid() {
        return fluid;
    }

    public Optional<Integer> getFluidAmount() {
        return fluidamt;
    }
}