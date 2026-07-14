package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.item.MapleArmorItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

public class ModRecipeGenerator extends RecipeProvider {
    private final HolderGetter<Item> itemLookup;

    protected ModRecipeGenerator(HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
        this.itemLookup = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    public void buildRecipes() {
    }

    public ExtractorRecipeJsonBuilder createExtractor(ItemLike output, int count) {
        return ExtractorRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public ExtractorRecipeJsonBuilder createExtractor(ItemLike output) {
        return ExtractorRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public MaceratorRecipeJsonBuilder createMacerator(ItemLike output, int count) {
        return MaceratorRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public MaceratorRecipeJsonBuilder createMacerator(ItemLike output) {
        return MaceratorRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public CompressorRecipeJsonBuilder createCompressor(ItemLike output, int count) {
        return CompressorRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public CompressorRecipeJsonBuilder createCompressor(ItemLike output) {
        return CompressorRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public MetalFormerRollingRecipeJsonBuilder createMetalFormerRolling(ItemLike input, ItemLike output) {
        return createMetalFormerRolling(input, output, 1);
    }

    public MetalFormerRollingRecipeJsonBuilder createMetalFormerRolling(ItemLike input, ItemLike output, int count) {
        return createMetalFormerRolling(input, 1, output, count);
    }

    public MetalFormerRollingRecipeJsonBuilder createMetalFormerRolling(ItemLike input, int inputCount, ItemLike output, int count) {
        MetalFormerRollingRecipeJsonBuilder builder = MetalFormerRollingRecipeJsonBuilder.create(this.itemLookup, output, count).input(input, inputCount).unlockedBy(getHasName(input), has(input));
        createWorkbenchRolling(input, output, count);
        return builder;
    }

    private void createWorkbenchCutting(ItemLike input, ItemLike outputItem, int count) {
        shapeless(RecipeCategory.MISC, outputItem, count).requires(MapleArmorItems.CUTTING).requires(input).unlockedBy(getHasName(input), has(input)).save(output);
    }

    public MetalFormerCuttingRecipeJsonBuilder createMetalFormerCutting(ItemLike input, ItemLike output) {
        return createMetalFormerCutting(input, output, 1);
    }

    public MetalFormerCuttingRecipeJsonBuilder createMetalFormerCutting(ItemLike input, ItemLike output, int count) {
        return createMetalFormerCutting(input, 1, output, count);
    }

    public MetalFormerCuttingRecipeJsonBuilder createMetalFormerCutting(ItemLike input, int inputCount, ItemLike output, int count) {
        MetalFormerCuttingRecipeJsonBuilder builder = MetalFormerCuttingRecipeJsonBuilder.create(this.itemLookup, output, count).input(input, inputCount).unlockedBy(getHasName(input), has(input));
        createWorkbenchCutting(input, output, count);
        return builder;
    }

    private void createWorkbenchRolling(ItemLike input, ItemLike outputItem, int count) {
        shapeless(RecipeCategory.MISC, outputItem, count).requires(MapleArmorItems.ROLLING).requires(input).unlockedBy(getHasName(input), has(input)).save(output);
    }

    public MetalFormerExtrudingRecipeJsonBuilder createMetalFormerExtruding(ItemLike input, int inputCount, ItemLike output, int count) {
        return MetalFormerExtrudingRecipeJsonBuilder.create(this.itemLookup, output, count).input(input, inputCount).unlockedBy(getHasName(input), has(input));
    }

    public MetalFormerExtrudingRecipeJsonBuilder createMetalFormerExtruding(ItemLike input, int inputCount, ItemLike output) {
        return createMetalFormerExtruding(input, inputCount, output, 1);
    }

    public MetalFormerExtrudingRecipeJsonBuilder createMetalFormerExtruding(ItemLike input, ItemLike output) {
        return createMetalFormerExtruding(input, 1, output);
    }

    public MetalFormerExtrudingRecipeJsonBuilder createMetalFormerExtruding(ItemLike input, ItemLike output, int count) {
        return createMetalFormerExtruding(input, 1, output, count);
    }

    public MolecularTransformerRecipeJsonBuilder createMolecularTransformer(ItemLike input, int inputCount, ItemLike output, int count, long requiredEnergy) {
        return MolecularTransformerRecipeJsonBuilder.create(this.itemLookup, output, count, requiredEnergy).input(input, inputCount).unlockedBy(getHasName(input), has(input));
    }

    public MolecularTransformerRecipeJsonBuilder createMolecularTransformer(ItemLike input, ItemLike output, int count, long requiredEnergy) {
        return createMolecularTransformer(input, 1, output, count, requiredEnergy);
    }

    public MolecularTransformerRecipeJsonBuilder createMolecularTransformer(ItemLike input, ItemLike output, long requiredEnergy) {
        return createMolecularTransformer(input, 1, output, 1, requiredEnergy);
    }

    public MolecularTransformerRecipeJsonBuilder createMolecularTransformer(ItemLike input, int inputCount, ItemLike output, long requiredEnergy) {
        return createMolecularTransformer(input, inputCount, output, 1, requiredEnergy);
    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(this.itemLookup, category, output, count);
    }

    public CuttingRecipeJsonBuilder createCutting(ItemLike input, ItemLike output) {
        return createCutting(input, output, 1);
    }

    public CuttingRecipeJsonBuilder createCutting(ItemLike input, int inputCount, ItemLike output) {
        return createCutting(input, inputCount, output, 1);
    }

    public CuttingRecipeJsonBuilder createCutting(ItemLike input, ItemLike output, int count) {
        return createCutting(input, 1, output, count);
    }

    public CuttingRecipeJsonBuilder createCutting(ItemLike input, int inputCount, ItemLike output, int count) {
        CuttingRecipeJsonBuilder builder = CuttingRecipeJsonBuilder.create(this.itemLookup, output, count).input(input, inputCount).unlockedBy(getHasName(input), has(input));
        return builder;
    }

    public BrewReactorRecipeJsonBuilder createBrewReactor(ItemLike output, int count) {
        return BrewReactorRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public BrewReactorRecipeJsonBuilder createBrewReactor(ItemLike output) {
        return BrewReactorRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public BrewReactorRecipeJsonBuilder createBrewReactor(ItemStack output) {
        return BrewReactorRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public BrewReactorRecipeJsonBuilder createBrewReactor(ItemStackTemplate output) {
        return BrewReactorRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public OreWashingRecipeJsonBuilder createOreWashing(ItemLike output, int count) {
        return OreWashingRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public OreWashingRecipeJsonBuilder createOreWashing(ItemLike output) {
        return OreWashingRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public HeatCentrifugeRecipeJsonBuilder createHeatCentrifuge(ItemLike output, int count) {
        return HeatCentrifugeRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public HeatCentrifugeRecipeJsonBuilder createHeatCentrifuge(ItemLike output) {
        return HeatCentrifugeRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public ModBlastFurnaceRecipeJsonBuilder createBlastFurnace(ItemLike output, int count) {
        return ModBlastFurnaceRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public ModBlastFurnaceRecipeJsonBuilder createBlastFurnace(ItemLike output) {
        return ModBlastFurnaceRecipeJsonBuilder.create(this.itemLookup, output);
    }


    public void addWoodSet(RecipeOutput exporter, ItemLike log, ItemLike strippedLog, ItemLike wood, ItemLike strippedWood, ItemLike planks, String name) {
        createCutting(log, planks, 6).save(exporter, name + "_log_to_planks");
        createCutting(strippedLog, planks, 6).save(exporter, name + "_stripped_log_to_planks");
        createCutting(wood, planks, 6).save(exporter, name + "_wood_to_planks");
        createCutting(strippedWood, planks, 6).save(exporter, name + "_stripped_wood_to_planks");
    }
}
