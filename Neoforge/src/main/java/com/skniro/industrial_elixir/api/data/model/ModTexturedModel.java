package com.skniro.industrial_elixir.api.data.model;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;

import static net.minecraft.client.data.models.model.TexturedModel.createDefault;


public class ModTexturedModel {
    public static final TexturedModel.Provider CUBE;


    static {

        CUBE = createDefault(ModTextureMap::allSides, ModelTemplates.CUBE);
    }
}