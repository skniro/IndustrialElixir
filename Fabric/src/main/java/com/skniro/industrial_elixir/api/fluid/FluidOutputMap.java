package com.skniro.industrial_elixir.api.fluid;

import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Map;

public class FluidOutputMap {

    public static final Map<Fluid, List<ContainerInfo>> FLUID_CONTAINERS = Map.of(
            Fluids.WATER,
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            Items.WATER_BUCKET
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL,
                            GrowableOresItems.WATER_CELL
                    )
            ),

            Fluids.LAVA,
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            Items.LAVA_BUCKET
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL,
                            GrowableOresItems.LAVA_CELL
                    )
            ),

            IndustrialElixirFluids.STILL_Fluid_UU,
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            IndustrialElixirFluidItems.Fluid_UU_BUCKET
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL,
                            IndustrialElixirFluidItems.UU_CELL
                    )
            ),

            IndustrialElixirFluids.STILL_Fluid_AIR,
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            IndustrialElixirFluidItems.Fluid_AIR_BUCKET
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL,
                            IndustrialElixirFluidItems.AIR_CELL
                    )
            )
    );

    public static ContainerInfo getContainerInfo(Fluid fluid, Item emptyContainer) {
        List<ContainerInfo> infos = FLUID_CONTAINERS.get(fluid);

        if (infos == null) {
            return null;
        }

        for (ContainerInfo info : infos) {
            if (info.emptyItem() == emptyContainer) {
                return info;
            }
        }

        return null;
    }
}
