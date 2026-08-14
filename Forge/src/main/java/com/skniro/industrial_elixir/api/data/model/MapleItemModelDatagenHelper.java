package com.skniro.industrial_elixir.api.data.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.world.item.Item;

import java.util.List;

public class MapleItemModelDatagenHelper {
    private final ItemModelGenerators generator;;

    public MapleItemModelDatagenHelper(ItemModelGenerators generator) {
        this.generator = generator;
    }

    public final void registerDurabilityItem(Item item) {
        ItemModel.Unbaked level0 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_0", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked level1 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_1", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked level2 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_2", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked level3 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_3", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked level4 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_4", ModelTemplates.FLAT_ITEM));

        generator.itemModelOutput.accept(item, ItemModelUtils.select(new BatteryLevelProperty(), level0,
                        List.of(
                                new SelectItemModel.SwitchCase<>(List.of(1), level1),
                                new SelectItemModel.SwitchCase<>(List.of(2), level2),
                                new SelectItemModel.SwitchCase<>(List.of(3), level3),
                                new SelectItemModel.SwitchCase<>(List.of(4), level4)
                        )
                )
        );
    }

}