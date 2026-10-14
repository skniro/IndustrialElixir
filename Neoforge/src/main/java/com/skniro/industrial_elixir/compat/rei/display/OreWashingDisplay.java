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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OreWashingDisplay extends BasicDisplay {
    private final int count;
    private final ResourceLocation fluid;
    private final int fluidAmount;

    public static final CategoryIdentifier<OreWashingDisplay> ORE_WASHING =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "ore_washing");

    public static final DisplaySerializer<OreWashingDisplay> SERIALIZER = BasicDisplay.Serializer.<OreWashingDisplay>of(
            (inputs, outputs, location, tag) -> new OreWashingDisplay(inputs, outputs, location, tag.getInt("count"),
                    ResourceLocation.parse(tag.getString("fluid")), tag.getInt("fluid_amount")),
            (display, tag) -> {
                tag.putInt("count", display.getCount());
                tag.putString("fluid", display.getFluid().toString());
                tag.putInt("fluid_amount", display.getFluidAmount());
            }
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

    public OreWashingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, int count, ResourceLocation fluid, int fluidAmount) {
        super(inputs, outputs, location);
        this.count = count;
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
    }

    private static List<EntryIngredient> buildOutputs(OreWashingCraftingRecipe recipe) {
        List<EntryIngredient> outputs = new ArrayList<>();
        outputs.add(EntryIngredient.of(EntryStacks.of(recipe.output())));
        recipe.output2().ifPresent(o -> outputs.add(EntryIngredient.of(EntryStacks.of(o))));
        recipe.output3().ifPresent(o -> outputs.add(EntryIngredient.of(EntryStacks.of(o))));
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ORE_WASHING;
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
