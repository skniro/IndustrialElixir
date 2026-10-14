package com.skniro.industrial_elixir.api.data.recipe;

import com.skniro.industrial_elixir.item.MapleArmorItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.List;

import static net.minecraft.data.recipes.RecipeProvider.getHasName;

/**
 * Recipe builder factory shared by the datagen generators.
 *
 * <p>26.1 made this a {@code RecipeProvider} subclass; 1.21.1 keeps the
 * providers separate, so this is now a plain helper that carries the item
 * lookup and the recipe output for the duration of one generator run.</p>
 */
public class ModRecipeGenerator {
    private final HolderGetter<Item> itemLookup;
    protected final RecipeOutput output;

    protected ModRecipeGenerator(HolderGetter<Item> itemLookup, RecipeOutput output) {
        this.itemLookup = itemLookup;
        this.output = output;
    }

    public void buildRecipes() {
    }

    /*
     * 26.1 made these RecipeProvider helpers instance methods; 1.21.1 only has them as
     * statics taking the RecipeOutput first, so re-expose them here for the generators.
     */

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
        return RecipeProvider.has(item);
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
        return RecipeProvider.has(tag);
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(MinMaxBounds.Ints count, ItemLike item) {
        return RecipeProvider.has(count, item);
    }

    public Ingredient tag(TagKey<Item> tag) {
        return Ingredient.of(tag);
    }

    public void woodFromLogs(ItemLike wood, ItemLike log) {
        RecipeProvider.woodFromLogs(this.output, wood, log);
    }

    public void woodenBoat(ItemLike boat, ItemLike planks) {
        RecipeProvider.woodenBoat(this.output, boat, planks);
    }

    public void chestBoat(ItemLike chestBoat, ItemLike boat) {
        RecipeProvider.chestBoat(this.output, chestBoat, boat);
    }

    public void hangingSign(ItemLike hangingSign, ItemLike strippedLog) {
        RecipeProvider.hangingSign(this.output, hangingSign, strippedLog);
    }

    public void shelf(ItemLike shelf, ItemLike strippedLog) {
        shaped(RecipeCategory.DECORATIONS, shelf, 6).define('#', strippedLog)
                .pattern("###").pattern("   ").pattern("###").group("shelf")
                .unlockedBy(getHasName(strippedLog), this.has(strippedLog)).save(this.output);
    }

    public void generateRecipes(BlockFamily family, FeatureFlagSet enabledFeatures) {
        RecipeProvider.generateRecipes(this.output, family, enabledFeatures);
    }

    public void oreSmelting(List<ItemLike> ingredients, RecipeCategory category, CookingBookCategory bookCategory, ItemLike result, float experience, int cookingTime, String group) {
        RecipeProvider.oreSmelting(this.output, ingredients, category, result, experience, cookingTime, group);
    }

    public <T extends AbstractCookingRecipe> void simpleCookingRecipe(String source, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> factory, int cookingTime, ItemLike base, ItemLike result, float experience) {
        RecipeProvider.simpleCookingRecipe(this.output, source, serializer, factory, cookingTime, base, result, experience);
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
        shapeless(RecipeCategory.MISC, outputItem, count).requires(MapleArmorItems.CUTTING).requires(input).unlockedBy(getHasName(input), has(input)).save(this.output);
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
        shapeless(RecipeCategory.MISC, outputItem, count).requires(MapleArmorItems.ROLLING).requires(input).unlockedBy(getHasName(input), has(input)).save(this.output);
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

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike output) {
        return ShapedRecipeBuilder.shaped(category, output);
    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(category, output, count);
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike output) {
        return ShapelessRecipeBuilder.shapeless(category, output);
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike output, int count) {
        return ShapelessRecipeBuilder.shapeless(category, output, count);
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

    public CoffeeMachineRecipeJsonBuilder createCoffeeMachine(ItemLike output, int count) {
        return CoffeeMachineRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public CoffeeMachineRecipeJsonBuilder createCoffeeMachine(ItemLike output) {
        return CoffeeMachineRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public CoffeeMachineRecipeJsonBuilder createCoffeeMachine(ItemStack output) {
        return CoffeeMachineRecipeJsonBuilder.create(this.itemLookup, output);
    }

    public CropFarmRecipeJsonBuilder createCropFarm(ItemLike output, int count) {
        return CropFarmRecipeJsonBuilder.create(this.itemLookup, output, count);
    }

    public CropFarmRecipeJsonBuilder createCropFarm(ItemLike output) {
        return CropFarmRecipeJsonBuilder.create(this.itemLookup, output);
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
