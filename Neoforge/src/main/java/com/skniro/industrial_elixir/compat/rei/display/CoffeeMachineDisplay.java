package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.CoffeeMachineCraftingRecipe;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoffeeMachineDisplay extends BasicDisplay {
    private final int count;
    private final ResourceLocation fluid;
    private final int fluidAmount;

    public static final CategoryIdentifier<CoffeeMachineDisplay> COFFEE_MACHINE =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "coffee_machine");

    public static final DisplaySerializer<CoffeeMachineDisplay> SERIALIZER = BasicDisplay.Serializer.<CoffeeMachineDisplay>of(
            (inputs, outputs, location, tag) -> new CoffeeMachineDisplay(inputs, outputs, location, tag.getInt("count"),
                    ResourceLocation.parse(tag.getString("fluid")), tag.getInt("fluid_amount")),
            (display, tag) -> {
                tag.putInt("count", display.getCount());
                tag.putString("fluid", display.getFluid().toString());
                tag.putInt("fluid_amount", display.getFluidAmount());
            }
    );

    public CoffeeMachineDisplay(CoffeeMachineCraftingRecipe recipe) {
        this(
                buildInputs(recipe),
                List.of(EntryIngredients.of(recipe.output())),
                Optional.empty(),
                recipe.requiredCount(),
                recipe.requiredFluid(),
                recipe.requiredFluidAmount()
        );
    }

    public CoffeeMachineDisplay(RecipeHolder<CoffeeMachineCraftingRecipe> recipe) {
        super(
                buildInputs(recipe.value()),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output()))));
        this.count = recipe.value().requiredCount();
        this.fluid = recipe.value().requiredFluid();
        this.fluidAmount = recipe.value().requiredFluidAmount();
    }

    public CoffeeMachineDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs,
                                 Optional<ResourceLocation> location, int count, ResourceLocation fluid, int fluidAmount) {
        super(inputs, outputs, location);
        this.count = count;
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
    }

    private static List<EntryIngredient> buildInputs(CoffeeMachineCraftingRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.ofIngredient(recipe.ingredient()));
        recipe.ingredient2().ifPresent(i -> inputs.add(EntryIngredients.ofIngredient(i)));
        recipe.ingredient3().ifPresent(i -> inputs.add(EntryIngredients.ofIngredient(i)));
        return inputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return COFFEE_MACHINE;
    }

    public int getCount() {
        return count;
    }

    public ResourceLocation getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }
}
