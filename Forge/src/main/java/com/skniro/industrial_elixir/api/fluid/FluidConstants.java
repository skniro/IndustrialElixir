package com.skniro.industrial_elixir.api.fluid;

import net.minecraft.world.level.material.FlowingFluid;

public final class FluidConstants {
	///////////////////////////
	// ==== FLUID UNITS ==== //
	///////////////////////////
	public static final int BUCKET = 81000;
	public static final int BOTTLE = 27000;
	public static final int BOWL = 27000;
	public static final int BLOCK = 81000;
	public static final int INGOT = 9000;
	public static final int NUGGET = 1000;
	public static final int DROPLET = 1;

	/**
	 * Convert a fraction of buckets into droplets.
	 *
	 * <p>For example, passing {@code (1, 3)} will return the 1/3 of a bucket as droplets, so 27000.
	 *
	 * @return The amount of droplets that the passed fraction is equivalent to.
	 * @throws IllegalArgumentException If the fraction can't be converted to droplets exactly.
	 */
	public static int fromBucketFraction(int numerator, int denominator) {
		int total = numerator * BUCKET;

		if (total % denominator != 0) {
			throw new IllegalArgumentException("Not a valid number of droplets!");
		} else {
			return total / denominator;
		}
	}

	// ==========================
	// ==== FLUID ATTRIBUTES ====
	// ==========================
	/**
	 * Water temperature, in Kelvin.
	 */
	public static final int WATER_TEMPERATURE = 300;
	/**
	 * Lava temperature, in Kelvin.
	 */
	public static final int LAVA_TEMPERATURE = 1300;

	public static final int WATER_VISCOSITY = 1000;
	public static final int LAVA_VISCOSITY = 6000;
	public static final int LAVA_VISCOSITY_NETHER = 2000;
	/**
	 * For flowing fluids, the viscosity should match {@code VISCOSITY_RATIO} * {@link FlowingFluid#getTickDelay}.
	 */
	public static final int VISCOSITY_RATIO = 200;

	private FluidConstants() {
	}
}
