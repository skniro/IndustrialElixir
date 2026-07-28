package com.skniro.industrial_elixir.world.Tree;

import com.skniro.industrial_elixir.world.feature.AgreeTreeConfiguredFeatures;
import java.util.Optional;
import net.minecraft.world.level.block.grower.TreeGrower;

public class RubberSaplingGenerator {
    public static final TreeGrower RubberSapling =
            new TreeGrower("rubber_sapling", 0.5f, Optional.empty(),
                    Optional.empty(),
                    Optional.of(AgreeTreeConfiguredFeatures.Rubber_TREE),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty());
    }