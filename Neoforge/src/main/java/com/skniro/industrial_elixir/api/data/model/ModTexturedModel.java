package com.skniro.industrial_elixir.api.data.model;

import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;

import static net.minecraft.data.models.model.TexturedModel.createDefault;


public class ModTexturedModel {
    public static final TexturedModel.Provider CUBE;


    static {

        CUBE = createDefault(ModTextureMap::allSides, ModelTemplates.CUBE);
    }
}
