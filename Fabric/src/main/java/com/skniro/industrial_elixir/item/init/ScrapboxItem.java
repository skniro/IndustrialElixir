package com.skniro.industrial_elixir.item.init;

import com.skniro.industrial_elixir.item.GrowableOresItems;
import java.util.Map;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ScrapboxItem extends Item {
    public ScrapboxItem(Properties settings) {
        super(settings);
    }

    private static final Map<Item, Double> SCRAPBOX_DROPS = Map.ofEntries(
            Map.entry(Items.BLAZE_ROD, 0.0008),    // 0.08%
            Map.entry(Items.NETHER_BRICKS, 0.0387), // 3.87%
            Map.entry(Items.COOKED_PORKCHOP, 0.0174),
            Map.entry(Items.GOLDEN_HELMET, 0.0002),
            Map.entry(Items.WOODEN_SHOVEL, 0.0193),
            Map.entry(Items.CAKE, 0.0097),
            Map.entry(Items.LEATHER, 0.0193),
            Map.entry(Items.APPLE, 0.0290),
            Map.entry(Items.IRON_ORE, 0.0097),
            Map.entry(GrowableOresItems.TIN_INGOT, 0.0290),
            Map.entry(Items.OAK_SIGN, 0.0193),
            Map.entry(Items.WOODEN_SWORD, 0.0193),
            Map.entry(Items.COOKED_BEEF, 0.0174),
            Map.entry(Items.COAL, 0.0155),
            Map.entry(GrowableOresItems.TIN_DUST, 0.0155),
            Map.entry(Items.DIAMOND, 0.0019),
            Map.entry(Items.BONE, 0.0193),
            Map.entry(GrowableOresItems.IRON_DUST, 0.0135),
            Map.entry(Items.ENDER_PEARL, 0.0015),
            Map.entry(Items.REDSTONE, 0.0174),
            Map.entry(Items.MINECART, 0.0002),
            Map.entry(Items.DIRT, 0.0967),
            Map.entry(Items.BREAD, 0.0290),
            Map.entry(Items.STICK, 0.0774),
            Map.entry(Items.WOODEN_PICKAXE, 0.0193),
            Map.entry(GrowableOresItems.Raw_Tin, 0.0135),
            Map.entry(Items.ROTTEN_FLESH, 0.0387),
            Map.entry(Items.GRASS_BLOCK, 0.0580),
            Map.entry(Items.COOKED_CHICKEN, 0.0174),
            Map.entry(Items.GOLD_ORE, 0.0097),
            Map.entry(Items.GLOWSTONE_DUST, 0.0155),
            Map.entry(Items.EMERALD, 0.0010),
            Map.entry(Items.PUMPKIN, 0.0174),
            Map.entry(Items.GRAVEL, 0.0580),
            Map.entry(Items.WOODEN_HOE, 0.0969),
            Map.entry(GrowableOresItems.GOLD_DUST, 0.0135),
            Map.entry(GrowableOresItems.COPPER_DUST, 0.0155),
            Map.entry(Items.SLIME_BALL, 0.0116),
            Map.entry(Items.FEATHER, 0.0193),
            Map.entry(GrowableOresItems.RE_BATTERY, 0.0135),
            Map.entry(Items.RAW_COPPER, 0.0135),
            Map.entry(Items.SOUL_SAND, 0.0193),
            Map.entry(GrowableOresItems.Rubber, 0.0155),
            Map.entry(Items.EGG, 0.0155)
    );

    private ItemStack rollScrapboxDrop(RandomSource random) {
        double r = random.nextDouble();
        double cumulative = 0.0;

        for (Map.Entry<Item, Double> entry : SCRAPBOX_DROPS.entrySet()) {
            cumulative += entry.getValue();
            if (r < cumulative) {
                if (entry.getKey() == null) return ItemStack.EMPTY;
                return new ItemStack(entry.getKey());
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            ItemStack drop = rollScrapboxDrop(world.getRandom());
            if (!drop.isEmpty()) {
                double dx = -Math.sin(Math.toRadians(player.getYRot())) * 0.5;
                double dz = Math.cos(Math.toRadians(player.getYRot())) * 0.5;
                double dy = 0.2;
                ItemEntity itemEntity = new ItemEntity(world, player.getX() + dx, player.getY() + player.getEyeHeight() - 0.3, player.getZ() + dz, drop);
                itemEntity.setDeltaMovement(dx * 0.5, dy, dz * 0.5);
                itemEntity.setPickUpDelay(40);
                world.addFreshEntity(itemEntity);
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

}
