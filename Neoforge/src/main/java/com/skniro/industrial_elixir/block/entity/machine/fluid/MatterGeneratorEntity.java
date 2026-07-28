package com.skniro.industrial_elixir.block.entity.machine.fluid;

import com.skniro.industrial_elixir.api.fluid.ContainerInfo;
import com.skniro.industrial_elixir.api.fluid.FluidOutputMap;
import com.skniro.industrial_elixir.block.entity.AlchemyBlockEntityType;
import com.skniro.industrial_elixir.block.init.machine.AbstractMachineblock;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluids;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.screen.handler.machine.fluid.MatterGeneratorScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * BlockEntity for the Matter Generator (UU matter producer).
 * Basic skeleton: extends AbstractMachineEntity so energy storage and inventory helpers are available.
 * The user will implement the detailed UU-production logic and fluid output.
 */
public class MatterGeneratorEntity extends AbstractFluidMachineEntity {
	private static final long BUCKET_VOLUME_MB = FluidConstants.BUCKET / 81;
	private static final long MB_UNIT = 1L; // 1 mB unit for insertion
	private long progressEU = 0;
	private static final long EU_PER_MB = 1_000_000;
	private long scrapAmplifier = 0;
	private static final long SCRAP_VALUE = 5000;
	private static final int SCRAP_BOOST = 5;
	private static final int SCRAP_CONSUME_INTERVAL = 600;
	private int scrapConsumeTimer = 0;
	protected static final int OUTPUT_EMPTY_FLUID_ITEM_SLOT = 9;
	protected static final int FLUID_ITEM_OUTPUT_SLOT = 10;

	public MatterGeneratorEntity(BlockPos pos, BlockState state) {
		super(AlchemyBlockEntityType.MATTER_GENERATOR_BE.get(), pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(FurnitureStrings.Matter_Generator);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
		// No menu by default - implement if you want a GUI
		return new MatterGeneratorScreenHandler(syncId, playerInventory, this, propertyDelegate);
	}

	@Override
	public RecipeType<?> getCurrentRecipeType() {
		return null;
	}

	public int getProgressPercent() {
		return (int) Math.min(100L, progressEU * 100L / EU_PER_MB);
	}

	private boolean generateMatter() {
		FluidVariant variant = FluidVariant.of(IndustrialElixirFluids.STILL_Fluid_UU.get());
		try (Transaction tx = Transaction.openOuter()) {
			long inserted = fluidContainer.insert(variant, MB_UNIT, tx);
			if (inserted == MB_UNIT) {
				tx.commit();
				return true;
			}
			return false;
		}
	}

	private void updateScrapAmplifier() {
		ItemStack stack = inventory.get(INPUT_SLOT);
		if (++scrapConsumeTimer < SCRAP_CONSUME_INTERVAL) {
			return;
		}
		scrapConsumeTimer = 0;
		if (!stack.is(GrowableOresItems.Scrap.get()))
			return;
		if (scrapAmplifier >= 5000)
			return;
		stack.shrink(1);
		scrapAmplifier += SCRAP_VALUE;

		setChanged();
	}

	@Override
	public void tick(net.minecraft.world.level.Level world, BlockPos pos, BlockState state) {
		if (world == null || world.isClientSide()) return;
		boolean working = false;

		charge(ENERGY_ITEM_SLOT);
		fillFluidContainer();

		if (hasFluidStackInFluidSlot()) {
			fillUpFluidTank();
		}

		updateScrapAmplifier();

		if (this.fluidContainer.getAmount() >= this.fluidContainer.getCapacity()) {
			return;
		}

		try (Transaction tx = Transaction.openOuter()) {

			long extracted = energyContainer.getSideStorage(null).extract(getEffectiveTier().getMaxInput(), tx);
			if (extracted > 0) {
				long gained = extracted;
				if (scrapAmplifier > 0) {
					long used = Math.min(scrapAmplifier, extracted);
					gained += used * SCRAP_BOOST;
					scrapAmplifier -= used;
				}

				progressEU += gained;
				tx.commit();
				working = true;
			} else {
				working = false;

			}
		}

		// When we have accumulated enough energy for 1 mB, attempt to insert 1 mB into tank
		while (progressEU >= EU_PER_MB) {
			if (!generateMatter())
				break;
			progressEU -= EU_PER_MB;
		}

		if(state.getValue(AbstractMachineblock.LIT)!=working){
			level.setBlock(worldPosition, state.setValue(AbstractMachineblock.LIT,working), 3);
		}

		setChanged(world, pos, state);
	}

	public void fillFluidContainer() {
		if (fluidContainer.isResourceBlank() || fluidContainer.getAmount() < BUCKET_VOLUME_MB) {
			return;
		}

		Fluid fluid = fluidContainer.getResource().getFluid();
		ItemStack emptyContainerStack = inventory.get(OUTPUT_EMPTY_FLUID_ITEM_SLOT);
		if (emptyContainerStack.isEmpty()) {
			return;
		}

		ContainerInfo containerInfo = FluidOutputMap.getContainerInfo(fluid, emptyContainerStack.getItem());

		if (containerInfo == null) {
			return;
		}

		ItemStack fullContainer = new ItemStack(containerInfo.fullItem());

		if (!canStoreFilledContainer(fullContainer)) {
			return;
		}

		try (Transaction transaction = Transaction.openOuter()) {

			long extracted = fluidContainer.extract(
					FluidVariant.of(fluid),
					BUCKET_VOLUME_MB,
					transaction
			);

			if (extracted != BUCKET_VOLUME_MB) {
				return;
			}

			emptyContainerStack.shrink(1);
			storeFilledContainer(fullContainer);

			transaction.commit();
		}
	}

	private boolean canStoreFilledContainer(ItemStack stack) {
		ItemStack output = inventory.get(FLUID_ITEM_OUTPUT_SLOT);

		return output.isEmpty()
				|| (ItemStack.isSameItemSameComponents(output, stack)
				&& output.getCount() + stack.getCount() <= output.getMaxStackSize());
	}

	private void storeFilledContainer(ItemStack stack) {
		ItemStack output = inventory.get(FLUID_ITEM_OUTPUT_SLOT);

		if (output.isEmpty()) {
			inventory.set(FLUID_ITEM_OUTPUT_SLOT, stack.copy());
		} else {
			output.grow(stack.getCount());
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putLong("matter_generator.ProgressEU", progressEU);
		output.putLong("matter_generator.scrapConsumeTimer", scrapConsumeTimer);
		output.putLong("matter_generator.scrap_amplifier", scrapAmplifier);
		SingleVariantStorage.writeValue(fluidContainer, FluidVariant.CODEC, output);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		progressEU = input.getLongOr("matter_generator.ProgressEU", 0);
		scrapConsumeTimer = input.getIntOr("matter_generator.scrapConsumeTimer", 0);
		scrapAmplifier = input.getLongOr("matter_generator.scrap_amplifier", 0L);
		SingleVariantStorage.readValue(fluidContainer, FluidVariant.CODEC, FluidVariant::blank, input);
	}
}



