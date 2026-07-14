package com.skniro.industrial_elixir.api.data.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import java.util.Map;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

import com.mojang.math.Quadrant;

public class MapleModelDatagenHelper {
    private final BlockModelGenerators generator;;
    private static final PropertyDispatch<VariantMutator> NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS;

    public MapleModelDatagenHelper(BlockModelGenerators generator) {
        this.generator = generator;
    }

/*    public void registerModLogs(Block block) {
        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block)
                .with(BlockStateVariantMap.models(Properties.AGE_2).generate(stage ->
                        createWeightedVariant(generator.createSubModel(block, "_stage" + stage, Models.CUBE_ALL, TextureMap::all)
                        )
                ))
        );
    }*/

    public void registerModLogs(Block block) {

        Identifier[] verticalModels = new Identifier[3];
        Identifier[] horizontalModels = new Identifier[3];

        for (int age = 0; age <= 2; age++) {

            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_stage" + age))
                    .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top"))
                    .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_stage" + age));

            verticalModels[age] = ModelTemplates.CUBE_COLUMN.createWithSuffix(
                    block,
                    "_stage" + age,
                    textures,
                    generator.modelOutput
            );

            horizontalModels[age] = ModelTemplates.CUBE_COLUMN_HORIZONTAL.createWithSuffix(
                    block,
                    "_stage" + age,
                    textures,
                    generator.modelOutput
            );
        }

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(BlockStateProperties.AGE_2, BlockStateProperties.AXIS)
                                .generate((age, axis) -> {

                                    if (axis == Direction.Axis.Y) {
                                        return BlockModelGenerators
                                                .plainVariant(verticalModels[age]);
                                    }

                                    MultiVariant variant =
                                            BlockModelGenerators
                                                    .plainVariant(horizontalModels[age]);

                                    if (axis == Direction.Axis.X) {
                                        return variant.with(
                                                generator.X_ROT_90
                                        );
                                    }

                                    // Z
                                    return variant
                                            .with(generator.X_ROT_90)
                                            .with(generator.Y_ROT_90);
                                })
                        )
        );

        generator.registerSimpleItemModel(block, verticalModels[0]);
    }




    public void registerBaseMachineBlock(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_left"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_right"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_left"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom_active"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back_active"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_left_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_right_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_left"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerBaseMachineBlockSameSide(Block cooker, TexturedModel.Provider modelFactory) {
        Map<TextureSlot, String> faceTextureSuffixes = Map.of(
                TextureSlot.DOWN, "_down",
                TextureSlot.UP, "_up",
                TextureSlot.NORTH, "_front",
                TextureSlot.SOUTH, "_back",
                TextureSlot.WEST, "_side",
                TextureSlot.EAST, "_side"
        );

        MultiVariant baseVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                    });
                }).create(cooker, generator.modelOutput)
        );

        MultiVariant litVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        if (key == TextureSlot.NORTH) {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_on"));
                        } else {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                        }
                    });
                }).createWithSuffix(cooker, "_on", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(cooker)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerBaseMachineBlockSameUpAndDown(Block cooker, TexturedModel.Provider modelFactory) {
        Map<TextureSlot, String> faceTextureSuffixes = Map.of(
                TextureSlot.DOWN, "_side",
                TextureSlot.UP, "_side",
                TextureSlot.NORTH, "_front",
                TextureSlot.SOUTH, "_back",
                TextureSlot.WEST, "_left",
                TextureSlot.EAST, "_right"
        );

        MultiVariant baseVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                    });
                }).create(cooker, generator.modelOutput)
        );

        MultiVariant litVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        if (key == TextureSlot.NORTH) {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_on"));
                        } else {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                        }
                    });
                }).createWithSuffix(cooker, "_on", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(cooker)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerBaseMachineBlockSameUpDownSide(Block cooker, TexturedModel.Provider modelFactory) {
        Map<TextureSlot, String> faceTextureSuffixes = Map.of(
                TextureSlot.DOWN, "_side",
                TextureSlot.UP, "_side",
                TextureSlot.NORTH, "_front",
                TextureSlot.SOUTH, "_back",
                TextureSlot.WEST, "_side",
                TextureSlot.EAST, "_side"
        );

        MultiVariant baseVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                    });
                }).create(cooker, generator.modelOutput)
        );

        MultiVariant litVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        if (key == TextureSlot.NORTH) {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_on"));
                        } else {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                        }
                    });
                }).createWithSuffix(cooker, "_on", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(cooker)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerBaseMachineBlockSameSideBack(Block cooker, TexturedModel.Provider modelFactory) {
        Map<TextureSlot, String> faceTextureSuffixes = Map.of(
                TextureSlot.DOWN, "_bottom",
                TextureSlot.UP, "_top",
                TextureSlot.NORTH, "_front",
                TextureSlot.SOUTH, "_leftrightback",
                TextureSlot.WEST, "_leftrightback",
                TextureSlot.EAST, "_leftrightback"
        );

        MultiVariant baseVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                    });
                }).create(cooker, generator.modelOutput)
        );

        MultiVariant litVariant = plainVariant(
                modelFactory.get(cooker).updateTextures(textures -> {
                    faceTextureSuffixes.forEach((key, suffix) -> {
                        if (key == TextureSlot.NORTH) {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_active"));
                        } else {
                            textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                        }
                    });
                }).createWithSuffix(cooker, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(cooker)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachine(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineSameNorthSouth(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_front"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_front_active"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineSameSide(Block block) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }


    public void registerMachineDiffBottom(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineExtractor(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineTransformer(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top_active"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineDiffBottomBack(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }


    public void registerMachineElectricHeater(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back_active"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineBlastFurnace(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineHeatCentrifuge(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineSolidHeater(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back_active"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerMachineChunkLoader(Block block, boolean hasActiveTexture) {

        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
            );
            return;
        }

        MultiVariant litVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side_active"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).createWithSuffix(block, "_active", generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, baseVariant))
                        .with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }

    public void registerCable(Block block) {
        MultiVariant baseVariant = plainVariant(
                ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
                    textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
                    textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

                    textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

                    textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
                    textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
                }).create(block, generator.modelOutput)
        );

        generator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, baseVariant).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS)
        );
    }




    public void registerModSweetBerryBush(Item fruititem, Block block) {
        generator.registerSimpleFlatItemModel(fruititem);
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.AGE_3).generate(stage ->
                                plainVariant(
                                generator.createSuffixedVariant(block, "_stage" + stage, ModelTemplates.CROSS, TextureMapping::cross)
                        )
                ))
        );
    }

    public void registerModBookshelf(Block block, Block plank) {
        TextureMapping textureMap = TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(plank));
        MultiVariant identifier = plainVariant(ModelTemplates.CUBE_COLUMN.create(block, textureMap, generator.modelOutput));
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, identifier));
    }

    public void registerLamp(Block block) {
        MultiVariant identifier = plainVariant(ModelLocationUtils.getModelLocation(block));
        MultiVariant identifier2 = plainVariant(ModelLocationUtils.getModelLocation(block,"_on"));
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(BlockStateProperties.LIT, identifier2, identifier)));
    }

    public final void registerBlockState(Block block) {
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(ModelLocationUtils.getModelLocation(block))));
    }

    public void registerFridge(Block block) {
        Identifier bottomModel = ModelLocationUtils.getModelLocation(block, "_bottom");
        Identifier topModel = ModelLocationUtils.getModelLocation(block, "_top");

        PropertyDispatch.C2<MultiVariant,Direction, DoubleBlockHalf> variantMap =
                PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF);

        fillSimpleDoubleVariantMap(variantMap, DoubleBlockHalf.LOWER, bottomModel);
        fillSimpleDoubleVariantMap(variantMap, DoubleBlockHalf.UPPER, topModel);

        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(variantMap));
    }


    public static PropertyDispatch.C2<MultiVariant, Direction, DoubleBlockHalf> fillSimpleDoubleVariantMap(
            PropertyDispatch.C2<MultiVariant, Direction, DoubleBlockHalf> variantMap,
            DoubleBlockHalf targetHalf,
            Identifier baseModelId
    ) {
        return variantMap
                .select(Direction.NORTH, targetHalf, plainVariant(baseModelId))
                .select(Direction.EAST, targetHalf, plainVariant(baseModelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
                .select(Direction.SOUTH, targetHalf, plainVariant(baseModelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))
                .select(Direction.WEST, targetHalf, plainVariant(baseModelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)));
    }

    public void registerPaperSlidingDoor(Block doorBlock) {
        MultiVariant weightedVariant = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_left"));
        MultiVariant weightedVariant2 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_left_open"));
        MultiVariant weightedVariant3 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_right"));
        MultiVariant weightedVariant4 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_right_open"));
        MultiVariant weightedVariant5 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_left"));
        MultiVariant weightedVariant6 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_left_open"));
        MultiVariant weightedVariant7 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_right"));
        MultiVariant weightedVariant8 = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_right_open"));
        generator.blockStateOutput.accept(createDoorBlockState(doorBlock, weightedVariant, weightedVariant2, weightedVariant3, weightedVariant4, weightedVariant5, weightedVariant6, weightedVariant7, weightedVariant8));
    }

    public static BlockModelDefinitionGenerator createDoorBlockState(Block doorBlock, MultiVariant bottomLeftClosedModel, MultiVariant bottomLeftOpenModel, MultiVariant bottomRightClosedModel, MultiVariant bottomRightOpenModel, MultiVariant topLeftClosedModel, MultiVariant topLeftOpenModel, MultiVariant topRightClosedModel, MultiVariant topRightOpenModel) {
        return MultiVariantGenerator.dispatch(doorBlock)
                .with(PropertyDispatch
                        .initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN)
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel.with(Y_ROT_180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel.with(Y_ROT_270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel.with(Y_ROT_90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel.with(Y_ROT_180)));
    }


    public void registerTV(Block block) {
        MultiVariant identifier = plainVariant(ModelLocationUtils.getModelLocation(block));
        MultiVariant identifier2 = plainVariant(ModelLocationUtils.getModelLocation(block,"_open"));
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(createBooleanModelDispatch(BlockStateProperties.LIT, identifier2, identifier)).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
    }

    public static PropertyDispatch.C2<MultiVariant, Direction, BedPart> fillSimpleDoubleVariantMap(
            PropertyDispatch.C2<MultiVariant, Direction, BedPart> variantMap,
            BedPart targetHalf,
            Identifier baseModelId
    ) {
        return variantMap
                .select(Direction.NORTH, targetHalf, plainVariant(baseModelId))
                .select(Direction.EAST, targetHalf, plainVariant(baseModelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
                .select(Direction.SOUTH, targetHalf, plainVariant(baseModelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))
                .select(Direction.WEST, targetHalf, plainVariant(baseModelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)));
    }

    static {
        NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING).select(Direction.EAST, Y_ROT_90).select(Direction.SOUTH, Y_ROT_180).select(Direction.WEST, Y_ROT_270).select(Direction.NORTH, NOP);
    }
}