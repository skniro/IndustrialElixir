package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.RecyclerCraftingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecyclerDisplay extends BasicDisplay {
    private int count;

    public static final CategoryIdentifier<RecyclerDisplay> Recycler =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "recycler");

    public static final DisplaySerializer<RecyclerDisplay> SERIALIZER = BasicDisplay.Serializer.<RecyclerDisplay>of(
            (inputs, outputs, location, tag) -> new RecyclerDisplay(outputs, location),
            (display, tag) -> {
            }
    );

    public RecyclerDisplay(RecyclerCraftingRecipe recipe) {
        super(List.of(EntryIngredients.ofItemStacks(getAllItems())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.output()))));
    }

    public RecyclerDisplay(List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(List.of(EntryIngredients.ofItemStacks(getAllItems())), outputs, location);
    }

    public RecyclerDisplay(RecipeHolder<RecyclerCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofItemStacks(getAllItems())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output()))));
    }

    private static List<ItemStack> getAllItems() {
        List<ItemStack> list = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            list.add(new ItemStack(item));
        }
        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return RecyclerDisplay.Recycler;
    }

}