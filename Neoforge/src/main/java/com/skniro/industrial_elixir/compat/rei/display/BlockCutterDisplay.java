package com.skniro.industrial_elixir.compat.rei.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.CuttingCraftingRecipe;
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

public class BlockCutterDisplay extends BasicDisplay {
    private int count;

    public static final CategoryIdentifier<BlockCutterDisplay> BlockCutter =
            CategoryIdentifier.of(IndustrialElixir.MOD_ID, "block_cutter");

    public static final DisplaySerializer<BlockCutterDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BlockCutterDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BlockCutterDisplay::getOutputEntries),
                    Identifier.CODEC.optionalFieldOf("location").forGetter(BlockCutterDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("count").forGetter(BlockCutterDisplay::getCount)
            ).apply(instance, BlockCutterDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    BlockCutterDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    BlockCutterDisplay::getOutputEntries,
                    ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                    BlockCutterDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    BlockCutterDisplay::getCount,
                    BlockCutterDisplay::new
            )
    );

    public BlockCutterDisplay(CuttingCraftingRecipe recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.ingredient())),
                List.of(EntryIngredients.of(recipe.output())), Optional.empty(), recipe.requiredCount());
        this.count = recipe.requiredCount();
    }

    public BlockCutterDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int count) {
        super(inputs, outputs, location);
        this.count = count;
    }

    public BlockCutterDisplay(RecipeHolder<CuttingCraftingRecipe> recipe){
        super(List.of(EntryIngredients.ofIngredient(recipe.value().ingredient())),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().output().create()))));
        this.count = recipe.value().requiredCount();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BlockCutter;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public int getCount() {
        return count;
    }
}