package com.skniro.industrial_elixir.world.Tree;

import com.skniro.industrial_elixir.world.feature.AgreeTreeConfiguredFeatures;
import java.util.Optional;

import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;

public class RubberSaplingGenerator {
    public static final TreeGrower RubberSapling =
            new TreeGrower("rubber_sapling",
                    WeightedList.of(AgreeTreeConfiguredFeatures.Rubber_TREE),
                    WeightedList.of(),
                    WeightedList.of(),
                    AgreeTreeConfiguredFeatures.Rubber_TREE);
}