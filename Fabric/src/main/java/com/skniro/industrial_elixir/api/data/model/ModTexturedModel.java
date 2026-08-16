package com.skniro.industrial_elixir.api.data.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;

import static net.minecraft.data.models.model.TexturedModel.createDefault;

@Environment(EnvType.CLIENT)
public class ModTexturedModel {
    public static final TexturedModel.Provider CUBE;


    static {

        CUBE = createDefault(ModTextureMap::allSides, ModelTemplates.CUBE);
    }
}