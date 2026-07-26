package com.skniro.industrial_elixir.item;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.item.init.CoffeeBeansItem;
import com.skniro.industrial_elixir.item.init.ItemBottle;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Function;

public class MapleFoodComponents {
    public static final Item Coffee_Beans = registerItem("coffee_beans",
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

    public static final Item Coffee_Black = registerItem(
            "coffee_black",
            createBlockItemWithUniqueName(GeneralBlocks.COFFEE_BLACK),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationModifier(0.6F)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.defaultDrink()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                                            new MobEffectInstance(MobEffects.SPEED, 2400, 1),
                                            new MobEffectInstance(MobEffects.HASTE, 1200, 0)),
                                            1.0F))
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Item Cappuccino = registerItem(
            "cappuccino",
            createBlockItemWithUniqueName(GeneralBlocks.CAPPUCCINO),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(9)
                                    .saturationModifier(1.2F)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.defaultDrink()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                                            new MobEffectInstance(MobEffects.SPEED, 1800, 1),
                                            new MobEffectInstance(MobEffects.REGENERATION, 300, 0)),
                                            1.0F))
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Item Latte = registerItem(
            "latte",
            createBlockItemWithUniqueName(GeneralBlocks.LATTE),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(7)
                                    .saturationModifier(1.0F)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.defaultDrink()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                                            new MobEffectInstance(MobEffects.SPEED, 1200, 0),
                                            1.0F))
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Item Mocha = registerItem(
            "mocha",
            createBlockItemWithUniqueName(GeneralBlocks.MOCHA),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(10)
                                    .saturationModifier(1.3F)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.defaultDrink()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                                            new MobEffectInstance(MobEffects.SPEED, 2400, 1),
                                            new MobEffectInstance(MobEffects.REGENERATION, 600, 0)),
                                            1.0F))
                                    .build()
                    )
                    .stacksTo(1)
    );

    public static final Item Hot_Cocoa = registerItem(
            "hot_cocoa",
            createBlockItemWithUniqueName(GeneralBlocks.HOT_COCOA),
            new Item.Properties()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationModifier(1.0F)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.defaultDrink()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                                            new MobEffectInstance(MobEffects.RESISTANCE, 400, 0),
                                            new MobEffectInstance(MobEffects.REGENERATION, 600, 0)),
                                            1.0F))
                                    .build()
                    )
                    .stacksTo(1)
    );



    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name))));
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, name)), item);
    }

    private static Function<Item.Properties, Item> createBlockItemWithUniqueName(Block block) {
        return (settings) -> {
            return new BlockItem(block, settings.useItemDescriptionPrefix());
        };
    }


    public static void registerMapleFoodItems() {
        IndustrialElixir.LOGGER.info("Registering Maple Food Items for " + IndustrialElixir.MOD_ID);
    }
}
