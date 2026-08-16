package com.skniro.industrial_elixir.api.data.model;


import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import java.util.Map;


public class MapleModelDatagenHelper {
    private final BlockModelGenerators generator;

    public MapleModelDatagenHelper(BlockModelGenerators generator) {
        this.generator = generator;
    }

    private static Variant plainVariant(ResourceLocation modelLocation) {
        return Variant.variant().with(VariantProperties.MODEL, modelLocation);
    }

    public void registerModLogs(Block block) {

        ResourceLocation[] verticalModels = new ResourceLocation[3];
        ResourceLocation[] horizontalModels = new ResourceLocation[3];

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
                MultiVariantGenerator.multiVariant(block)
                        .with(PropertyDispatch.properties(BlockStateProperties.AGE_2, BlockStateProperties.AXIS)
                                .generate((age, axis) -> {

                                    if (axis == Direction.Axis.Y) {
                                        return plainVariant(verticalModels[age]);
                                    }

                                    Variant variant = plainVariant(horizontalModels[age]);

                                    if (axis == Direction.Axis.X) {
                                        return variant.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
                                    }

                                    // Z
                                    return variant
                                            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                                })
                        )
        );

        generator.delegateItemModel(block, verticalModels[0]);
    }


    public void registerBaseMachineBlock(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_left"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_right"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_left"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom_active"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back_active"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_left_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_right_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_left"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
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

        ResourceLocation baseModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
            });
        }).create(cooker, generator.modelOutput);

        ResourceLocation litModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                if (key == TextureSlot.NORTH) {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_on"));
                } else {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                }
            });
        }).createWithSuffix(cooker, "_on", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(cooker)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
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

        ResourceLocation baseModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
            });
        }).create(cooker, generator.modelOutput);

        ResourceLocation litModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                if (key == TextureSlot.NORTH) {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_on"));
                } else {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                }
            });
        }).createWithSuffix(cooker, "_on", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(cooker)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
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

        ResourceLocation baseModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
            });
        }).create(cooker, generator.modelOutput);

        ResourceLocation litModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                if (key == TextureSlot.NORTH) {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_on"));
                } else {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                }
            });
        }).createWithSuffix(cooker, "_on", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(cooker)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
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

        ResourceLocation baseModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
            });
        }).create(cooker, generator.modelOutput);

        ResourceLocation litModel = modelFactory.get(cooker).updateTextures(textures -> {
            faceTextureSuffixes.forEach((key, suffix) -> {
                if (key == TextureSlot.NORTH) {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, "_front_active"));
                } else {
                    textures.put(key, TextureMapping.getBlockTexture(cooker, suffix));
                }
            });
        }).createWithSuffix(cooker, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(cooker)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachine(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineSameNorthSouth(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_front"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_front_active"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineSameSide(Block block) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }


    public void registerMachineDiffBottom(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineExtractor(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineTransformer(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top_active"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineDiffBottomBack(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }


    public void registerMachineElectricHeater(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back_active"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineBlastFurnace(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineHeatCentrifuge(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top_active"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineSolidHeater(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back_active"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerMachineChunkLoader(Block block, boolean hasActiveTexture) {

        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        if (!hasActiveTexture) {
            generator.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                            .with(BlockModelGenerators.createHorizontalFacingDispatch())
            );
            return;
        }

        ResourceLocation litModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_side_active"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side_active"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).createWithSuffix(block, "_active", generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litModel, baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    public void registerCable(Block block) {
        ResourceLocation baseModel = ModTexturedModel.CUBE.get(block).updateTextures(textures -> {
            textures.put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"));
            textures.put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_top"));

            textures.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"));

            textures.put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"));
            textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side"));
        }).create(block, generator.modelOutput);

        generator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block, plainVariant(baseModel))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }


    public void registerModSweetBerryBush(Item fruititem, Block block) {
        generator.createSimpleFlatItemModel(fruititem);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.AGE_3).generate(stage ->
                        plainVariant(generator.createSuffixedVariant(block, "_stage" + stage, ModelTemplates.CROSS, TextureMapping::cross)
                        )
                ))
        );
    }

    public void registerModBookshelf(Block block, Block plank) {
        TextureMapping textureMap = TextureMapping.column(TextureMapping.getBlockTexture(block), TextureMapping.getBlockTexture(plank));
        ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(block, textureMap, generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, model));
    }

    public void registerLamp(Block block) {
        ResourceLocation identifier = ModelLocationUtils.getModelLocation(block);
        ResourceLocation identifier2 = ModelLocationUtils.getModelLocation(block, "_on");
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, identifier2, identifier)));
    }

    public final void registerBlockState(Block block) {
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, plainVariant(ModelLocationUtils.getModelLocation(block))));
    }

    public void registerFridge(Block block) {
        ResourceLocation bottomModel = ModelLocationUtils.getModelLocation(block, "_bottom");
        ResourceLocation topModel = ModelLocationUtils.getModelLocation(block, "_top");

        PropertyDispatch.C2<Direction, DoubleBlockHalf> variantMap =
                PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF);

        fillSimpleDoubleVariantMap(variantMap, DoubleBlockHalf.LOWER, bottomModel);
        fillSimpleDoubleVariantMap(variantMap, DoubleBlockHalf.UPPER, topModel);

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(variantMap));
    }


    public static PropertyDispatch.C2<Direction, DoubleBlockHalf> fillSimpleDoubleVariantMap(
            PropertyDispatch.C2<Direction, DoubleBlockHalf> variantMap,
            DoubleBlockHalf targetHalf,
            ResourceLocation baseModelId
    ) {
        return variantMap
                .select(Direction.NORTH, targetHalf, plainVariant(baseModelId))
                .select(Direction.EAST, targetHalf, plainVariant(baseModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, targetHalf, plainVariant(baseModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, targetHalf, plainVariant(baseModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }

    public void registerPaperSlidingDoor(Block doorBlock) {
        Variant bottomLeft = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_left"));
        Variant bottomLeftOpen = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_left_open"));
        Variant bottomRight = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_right"));
        Variant bottomRightOpen = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_bottom_right_open"));
        Variant topLeft = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_left"));
        Variant topLeftOpen = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_left_open"));
        Variant topRight = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_right"));
        Variant topRightOpen = plainVariant(ModelLocationUtils.getModelLocation(doorBlock, "_top_right_open"));
        generator.blockStateOutput.accept(createDoorBlockState(doorBlock, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen));
    }

    public static BlockStateGenerator createDoorBlockState(Block doorBlock, Variant bottomLeftClosedModel, Variant bottomLeftOpenModel, Variant bottomRightClosedModel, Variant bottomRightOpenModel, Variant topLeftClosedModel, Variant topLeftOpenModel, Variant topRightClosedModel, Variant topRightOpenModel) {
        return MultiVariantGenerator.multiVariant(doorBlock)
                .with(PropertyDispatch
                        .properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN)
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, bottomLeftClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, bottomRightClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, bottomLeftOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, bottomRightOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, topLeftClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, topRightClosedModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, topLeftOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel)
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, topRightOpenModel.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)));
    }


    public void registerTV(Block block) {
        ResourceLocation identifier = ModelLocationUtils.getModelLocation(block);
        ResourceLocation identifier2 = ModelLocationUtils.getModelLocation(block, "_open");
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, identifier2, identifier))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    public static PropertyDispatch.C2<Direction, BedPart> fillSimpleDoubleVariantMap(
            PropertyDispatch.C2<Direction, BedPart> variantMap,
            BedPart targetHalf,
            ResourceLocation baseModelId
    ) {
        return variantMap
                .select(Direction.NORTH, targetHalf, plainVariant(baseModelId))
                .select(Direction.EAST, targetHalf, plainVariant(baseModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, targetHalf, plainVariant(baseModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, targetHalf, plainVariant(baseModelId).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }
}
