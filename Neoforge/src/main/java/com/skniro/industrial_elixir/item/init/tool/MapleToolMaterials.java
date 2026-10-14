package com.skniro.industrial_elixir.item.init.tool;


import com.skniro.industrial_elixir.registry.tag.ModItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * 26.1's {@code ToolMaterial} record has no 1.21.1 counterpart; 1.21.1 uses the {@link Tier}
 * interface instead, so the same values are supplied through an anonymous implementation.
 */
public class MapleToolMaterials {
    public static final Tier BRONZE = new Tier() {
        @Override
        public int getUses() {
            return 340;
        }

        @Override
        public float getSpeed() {
            return 6.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.0F;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_IRON_TOOL;
        }

        @Override
        public int getEnchantmentValue() {
            return 14;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItemTags.BRONZE_TOOL_MATERIALS);
        }
    };
}
