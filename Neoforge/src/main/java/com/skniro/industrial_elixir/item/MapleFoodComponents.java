package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.item.init.MapleBlockItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class MapleFoodComponents {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IndustrialElixir.MOD_ID);

    public static final Supplier<Item> Coffee_Beans = registerItem("coffee_beans",
            createBlockItemWithUniqueName(GeneralBlocks.Coffee_Block),
            new Item
                    .Properties() .food
                            (new FoodProperties
                                    .Builder()
                                    .nutrition(1)
                                    .saturationModifier(0.1f)
                                    .alwaysEdible()
                                    .build()
                            )
                    .stacksTo(64)
    );

    public static final Supplier<Item> Coffee_Black = registerItem(
            "coffee_black",
            createBlockItemWithUniqueName(GeneralBlocks.COFFEE_BLACK),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationModifier(0.6F)
                                    .alwaysEdible()
                                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), 1.0F)
                                    .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0), 1.0F)
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Supplier<Item> Cappuccino = registerItem(
            "cappuccino",
            createBlockItemWithUniqueName(GeneralBlocks.CAPPUCCINO),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(9)
                                    .saturationModifier(1.2F)
                                    .alwaysEdible()
                                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 1), 1.0F)
                                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 300, 0), 1.0F)
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Supplier<Item> Latte = registerItem(
            "latte",
            createBlockItemWithUniqueName(GeneralBlocks.LATTE),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(7)
                                    .saturationModifier(1.0F)
                                    .alwaysEdible()
                                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0), 1.0F)
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Supplier<Item> Mocha = registerItem(
            "mocha",
            createBlockItemWithUniqueName(GeneralBlocks.MOCHA),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(10)
                                    .saturationModifier(1.3F)
                                    .alwaysEdible()
                                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), 1.0F)
                                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 1.0F)
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Supplier<Item> Hot_Cocoa = registerItem(
            "hot_cocoa",
            createBlockItemWithUniqueName(GeneralBlocks.HOT_COCOA),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationModifier(1.0F)
                                    .alwaysEdible()
                                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0), 1.0F)
                                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 1.0F)
                                    .build()
                    )
                    .stacksTo(1)
    );



    public static <B extends Item> Supplier<Item> register(String name, Function<Item.Properties, ? extends B> func) {
        return ITEMS.register(name, () -> (Item) func.apply(new Item.Properties()));
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, ? extends T> item, Item.Properties properties) {
        return ITEMS.registerItem(name, item, properties);
    }

    private static <T extends Item> Supplier<Item> registerItemDeferred(String name, Function<Item.Properties, ? extends T> item, Supplier<Item.Properties> propertiesSupplier) {
        return ITEMS.register(name, () -> item.apply(propertiesSupplier.get()));
    }

    private static Function<Item.Properties, Item> createBlockItemWithUniqueName(Supplier<Block> block) {
        return (properties) -> new MapleBlockItem(block, properties);
    }


    public static void registerMapleFoodItems(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering Maple Food Items for " + IndustrialElixir.MOD_ID);
        ITEMS.register(eventBus);
    }
}
