package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.MaceratorCraftingRecipe;
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

public class MaceratorDisplay extends BasicDisplay {

    public static final CategoryIdentifier<MaceratorDisplay> MACERATOR =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "macerator");

    public static final DisplaySerializer<MaceratorDisplay> SERIALIZER = BasicDisplay.Serializer.<MaceratorDisplay>ofSimple(
            (inputs, outputs, location) -> new MaceratorDisplay(inputs, outputs, location)
    );

    public MaceratorDisplay(MaceratorCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())),
                Optional.empty());
    }

    public MaceratorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }

    public MaceratorDisplay(RecipeHolder<MaceratorCraftingRecipe> recipe) {
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output()))));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MACERATOR;
    }

}