package com.skniro.industrial_elixir.api.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MapleItemModelDatagenHelper {
    private final ItemModelGenerators generator;

    public MapleItemModelDatagenHelper(ItemModelGenerators generator) {
        this.generator = generator;
    }

    public final void registerDurabilityItem(Item item) {
        // Generate the charge level models: <item>_0 (empty) .. <item>_4 (full).
        for (int i = 0; i <= 4; i++) {
            generator.generateFlatItem(item, "_" + i, ModelTemplates.FLAT_ITEM);
        }

        ResourceLocation level0 = ModelLocationUtils.getModelLocation(item, "_0");
        ResourceLocation level1 = ModelLocationUtils.getModelLocation(item, "_1");
        ResourceLocation level2 = ModelLocationUtils.getModelLocation(item, "_2");
        ResourceLocation level3 = ModelLocationUtils.getModelLocation(item, "_3");
        ResourceLocation level4 = ModelLocationUtils.getModelLocation(item, "_4");

        // The base model uses the full texture and switches to lower charge
        // models via durability-based overrides. The "damage" predicate is the
        // fraction of consumed durability (0.0 = full, 1.0 = empty).
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", TextureMapping.getItemTexture(item, "_4").toString());
        root.add("textures", textures);

        JsonArray overrides = new JsonArray();
        overrides.add(createOverride(level0, 1.0F));
        overrides.add(createOverride(level1, 0.75F));
        overrides.add(createOverride(level2, 0.5F));
        overrides.add(createOverride(level3, 0.25F));
        root.add("overrides", overrides);

        generator.output.accept(ModelLocationUtils.getModelLocation(item), () -> root);
    }

    private static JsonObject createOverride(ResourceLocation model, float damage) {
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty("damage", damage);
        override.add("predicate", predicate);
        override.addProperty("model", model.toString());
        return override;
    }
}
