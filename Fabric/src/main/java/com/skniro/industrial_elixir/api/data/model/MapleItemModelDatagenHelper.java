package com.skniro.industrial_elixir.api.data.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.Damage;
import net.minecraft.world.item.Item;

public class MapleItemModelDatagenHelper {
    private final ItemModelGenerators generator;;

    public MapleItemModelDatagenHelper(ItemModelGenerators generator) {
        this.generator = generator;
    }

    public final void registerDurabilityItem(Item item) {
        ItemModel.Unbaked low0   = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_0", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked low25  = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_1", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked low50  = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_2", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked low75  = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_3", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked low100 = ItemModelUtils.plainModel(generator.createFlatItemModel(item, "_4", ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked dispatched = ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), ItemModelUtils.rangeSelect(new Damage(false), 0.0F, low0, ItemModelUtils.override(low25, 0.25F), ItemModelUtils.override(low50, 0.50F), ItemModelUtils.override(low75, 0.75F), ItemModelUtils.override(low100, 1.0F)), low0);
        generator.itemModelOutput.accept(item, dispatched);
    }

}