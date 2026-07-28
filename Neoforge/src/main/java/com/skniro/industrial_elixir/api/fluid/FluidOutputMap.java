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
                            GrowableOresItems.EMPTY_CELL.get(),
                            GrowableOresItems.WATER_CELL.get()
                    )
            ),

            Fluids.LAVA,
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            Items.LAVA_BUCKET
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL.get(),
                            GrowableOresItems.LAVA_CELL.get()
                    )
            ),

            IndustrialElixirFluids.STILL_Fluid_UU.get(),
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            IndustrialElixirFluidItems.Fluid_UU_BUCKET.get()
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL.get(),
                            IndustrialElixirFluidItems.UU_CELL.get()
                    )
            ),

            IndustrialElixirFluids.STILL_Fluid_AIR.get(),
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            IndustrialElixirFluidItems.Fluid_AIR_BUCKET.get()
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL.get(),
                            IndustrialElixirFluidItems.AIR_CELL.get()
                    )
            ),

            IndustrialElixirFluids.STILL_Hot_Spring.get(),
            List.of(
                    new ContainerInfo(
                            Items.BUCKET,
                            IndustrialElixirFluidItems.Hot_Spring_BUCKET.get()
                    ),
                    new ContainerInfo(
                            GrowableOresItems.EMPTY_CELL.get(),
                            IndustrialElixirFluidItems.Hot_Spring_CELL.get()
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
