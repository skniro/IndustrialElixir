package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class IndustrialElixirItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
   public IndustrialElixirItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
   }
   @Override
   protected void addTags(HolderLookup.Provider arg) {
      builder(ModItemTags.BATTERY)
              .add(GrowableOresItems.RE_BATTERY.builtInRegistryHolder().key())
              .add(GrowableOresItems.ADVANCED_RE_BATTERY.builtInRegistryHolder().key())
              .add(GrowableOresItems.ENERGY_CRYSTAL.builtInRegistryHolder().key())
              .add(GrowableOresItems.LAPOTRON_CRYSTAL.builtInRegistryHolder().key());
      builder(ItemTags.LEAVES)
              .add(Item.byBlock(GeneralBlocks.Rubber_LEAVES).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.SAPLINGS)
              .add(Item.byBlock(GeneralBlocks.Rubber_SAPLING).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.LOGS_THAT_BURN)
              .add(Item.byBlock(GeneralBlocks.Rubber_LOG).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.Rubber_Rubber_LOG).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.Rubber_WOOD).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.STRIPPED_Rubber_LOG).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.STRIPPED_Rubber_WOOD).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.WOODEN_BUTTONS)
              .add(Item.byBlock(GeneralBlocks.Rubber_BUTTON).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.WOODEN_DOORS)
              .add(Item.byBlock(GeneralBlocks.Rubber_DOOR).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.WOODEN_TRAPDOORS)
              .add(Item.byBlock(GeneralBlocks.Rubber_TRAPDOOR).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.WOODEN_PRESSURE_PLATES)
              .add(Item.byBlock(GeneralBlocks.Rubber_PRESSURE_PLATE).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.WOODEN_SLABS)
              .add(Item.byBlock(GeneralBlocks.Rubber_SLAB).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.WOODEN_STAIRS)
              .add(Item.byBlock(GeneralBlocks.Rubber_STAIRS).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ItemTags.PLANKS)
              .add(Item.byBlock(GeneralBlocks.Rubber_PLANKS).builtInRegistryHolder().key())
              .setReplace(false);
      builder(BlockItemTags.FENCES.item())
              .add(Item.byBlock(GeneralBlocks.Rubber_FENCE).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ModItemTags.SACRED_ORES)
              .add(Item.byBlock(GeneralBlocks.SACRED_Ore).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.Deepslate_SACRED_Ore).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ModItemTags.TIN_ORES)
              .add(Item.byBlock(GeneralBlocks.Tin_Ore).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.Deepslate_Tin_Ore).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ModItemTags.LEAD_ORES)
              .add(Item.byBlock(GeneralBlocks.Lead_Ore).builtInRegistryHolder().key())
              .add(Item.byBlock(GeneralBlocks.Deepslate_Lead_Ore).builtInRegistryHolder().key())
              .setReplace(false);
      builder(ModItemTags.DUST_TIN)
              .add(GrowableOresItems.TIN_DUST.builtInRegistryHolder().key())
              .add(GrowableOresItems.PURIFIED_TIN.builtInRegistryHolder().key())
              .add(GrowableOresItems.CRUSHED_TIN.builtInRegistryHolder().key())
              .setReplace(false);
      builder(ModItemTags.DUST_COPPER)
              .add(GrowableOresItems.COPPER_DUST.builtInRegistryHolder().key())
              .add(GrowableOresItems.PURIFIED_COPPER.builtInRegistryHolder().key())
              .add(GrowableOresItems.CRUSHED_COPPER.builtInRegistryHolder().key())
              .setReplace(false);
      builder(ModItemTags.REPAIRS_BRONZE_ARMOR)
              .add(GrowableOresItems.BRONZE_INGOT.builtInRegistryHolder().key());
      builder(ModItemTags.BRONZE_TOOL_MATERIALS)
              .add(GrowableOresItems.BRONZE_INGOT.builtInRegistryHolder().key());
      builder(ModItemTags.Diamond)
              .add(GrowableOresItems.INDUSTRIAL_DIAMOND.builtInRegistryHolder().key())
              .add(Items.DIAMOND.builtInRegistryHolder().key());
      builder(BlockItemTags.DOORS.item())
              .add(Item.byBlock(GeneralBlocks.Reinforced_DOOR).builtInRegistryHolder().key())
              .setReplace(false);
      builder(BlockItemTags.STAIRS.item())
              .add(Item.byBlock(GeneralBlocks.COBBLESTONE_STAIRS).builtInRegistryHolder().key())
              .setReplace(false);
   }
}