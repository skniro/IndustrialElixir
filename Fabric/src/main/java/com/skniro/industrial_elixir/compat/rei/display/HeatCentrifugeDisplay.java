package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.HeatCentrifugeCraftingRecipe;
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

public class HeatCentrifugeDisplay extends BasicDisplay {
    private final int count;

    public static final CategoryIdentifier<HeatCentrifugeDisplay> HEAT_CENTRIFUGE =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "heat_centrifuge");

    public static final DisplaySerializer<HeatCentrifugeDisplay> SERIALIZER = BasicDisplay.Serializer.<HeatCentrifugeDisplay>of(
            (inputs, outputs, location, tag) -> new HeatCentrifugeDisplay(inputs, outputs, location, tag.getInt("count")),
            (display, tag) -> tag.putInt("count", display.getCount())
    );

    public HeatCentrifugeDisplay(HeatCentrifugeCraftingRecipe recipe) {
        this(
                List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                buildOutputs(recipe),
                Optional.empty(),
                recipe.requiredCount()
        );
    }

    public HeatCentrifugeDisplay(RecipeHolder<HeatCentrifugeCraftingRecipe> recipe) {
        super(
                List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                buildOutputs(recipe.value()));
        this.count = recipe.value().requiredCount();
    }

    public HeatCentrifugeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location, int count) {
        super(inputs, outputs, location);
        this.count = count;
    }

    private static List<EntryIngredient> buildOutputs(HeatCentrifugeCraftingRecipe recipe) {
        List<EntryIngredient> outputs = new ArrayList<>();
        outputs.add(EntryIngredient.of(EntryStacks.of(recipe.output())));
        recipe.output2().ifPresent(o -> outputs.add(EntryIngredient.of(EntryStacks.of(o))));
        recipe.output3().ifPresent(o -> outputs.add(EntryIngredient.of(EntryStacks.of(o))));
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HEAT_CENTRIFUGE;
    }


    public int getCount() {
        return count;
    }
}
