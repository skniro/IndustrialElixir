package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class IndustrialElixirItemTagGenerator extends FabricTagProvider.ItemTagProvider {
   public IndustrialElixirItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
   }
   @Override
   protected void addTags(HolderLookup.Provider arg) {
      getOrCreateTagBuilder(ModItemTags.BATTERY)
              .add(GrowableOresItems.RE_BATTERY)
              .add(GrowableOresItems.ADVANCED_RE_BATTERY)
              .add(GrowableOresItems.ENERGY_CRYSTAL)
              .add(GrowableOresItems.LAPOTRON_CRYSTAL);
      getOrCreateTagBuilder(ItemTags.LEAVES)
              .add(Item.byBlock(GeneralBlocks.Rubber_LEAVES))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.SAPLINGS)
              .add(Item.byBlock(GeneralBlocks.Rubber_SAPLING))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
              .add(Item.byBlock(GeneralBlocks.Rubber_LOG))
              .add(Item.byBlock(GeneralBlocks.Rubber_Rubber_LOG))
              .add(Item.byBlock(GeneralBlocks.Rubber_WOOD))
              .add(Item.byBlock(GeneralBlocks.STRIPPED_Rubber_LOG))
              .add(Item.byBlock(GeneralBlocks.STRIPPED_Rubber_WOOD))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
              .add(Item.byBlock(GeneralBlocks.Rubber_BUTTON))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
              .add(Item.byBlock(GeneralBlocks.Rubber_DOOR))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
              .add(Item.byBlock(GeneralBlocks.Rubber_TRAPDOOR))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
              .add(Item.byBlock(GeneralBlocks.Rubber_PRESSURE_PLATE))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
              .add(Item.byBlock(GeneralBlocks.Rubber_SLAB))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
              .add(Item.byBlock(GeneralBlocks.Rubber_STAIRS))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.PLANKS)
              .add(Item.byBlock(GeneralBlocks.Rubber_PLANKS))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.FENCES)
              .add(Item.byBlock(GeneralBlocks.Rubber_FENCE))
              .setReplace(false);
      getOrCreateTagBuilder(ModItemTags.SACRED_ORES)
              .add(Item.byBlock(GeneralBlocks.SACRED_Ore))
              .add(Item.byBlock(GeneralBlocks.Deepslate_SACRED_Ore))
              .setReplace(false);
      getOrCreateTagBuilder(ModItemTags.TIN_ORES)
              .add(Item.byBlock(GeneralBlocks.Tin_Ore))
              .add(Item.byBlock(GeneralBlocks.Deepslate_Tin_Ore))
              .setReplace(false);
      getOrCreateTagBuilder(ModItemTags.LEAD_ORES)
              .add(Item.byBlock(GeneralBlocks.Lead_Ore))
              .add(Item.byBlock(GeneralBlocks.Deepslate_Lead_Ore))
              .setReplace(false);
      getOrCreateTagBuilder(ModItemTags.DUST_TIN)
              .add(GrowableOresItems.TIN_DUST)
              .add(GrowableOresItems.PURIFIED_TIN)
              .add(GrowableOresItems.CRUSHED_TIN)
              .setReplace(false);
      getOrCreateTagBuilder(ModItemTags.DUST_COPPER)
              .add(GrowableOresItems.COPPER_DUST)
              .add(GrowableOresItems.PURIFIED_COPPER)
              .add(GrowableOresItems.CRUSHED_COPPER)
              .setReplace(false);
      getOrCreateTagBuilder(ModItemTags.REPAIRS_BRONZE_ARMOR)
              .add(GrowableOresItems.BRONZE_INGOT);
      getOrCreateTagBuilder(ModItemTags.BRONZE_TOOL_MATERIALS)
              .add(GrowableOresItems.BRONZE_INGOT);
      getOrCreateTagBuilder(ModItemTags.Diamond)
              .add(GrowableOresItems.INDUSTRIAL_DIAMOND)
              .add(Items.DIAMOND);
      getOrCreateTagBuilder(ItemTags.DOORS)
              .add(Item.byBlock(GeneralBlocks.Reinforced_DOOR))
              .setReplace(false);
      getOrCreateTagBuilder(ItemTags.STAIRS)
              .add(Item.byBlock(GeneralBlocks.COBBLESTONE_STAIRS))
              .setReplace(false);
   }
}