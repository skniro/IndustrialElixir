package com.skniro.industrial_elixir.recipe;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.PatternStorageCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.CoffeeMachineCraftingRecipe;
import com.skniro.industrial_elixir.recipe.machine.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public interface AlchemyRecipeType<T extends Recipe<?>> {
    public static final RecipeRegistration<AlchemyCraftingRecipe> CANE_CONVERTER =
            register("cane_converter", AlchemyCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<MaceratorCraftingRecipe> MACERATOR =
            register("macerator", MaceratorCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<CompressorCraftingRecipe> COMPRESSOR =
            register("compressor", CompressorCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<MetalFormerRollingCraftingRecipe> METALFORMER_ROLLING =
            register("metal_former_rolling", MetalFormerRollingCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<MetalFormerCuttingCraftingRecipe> METALFORMER_CUTTING =
            register("metal_former_cutting", MetalFormerCuttingCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<MetalFormerExtrudingCraftingRecipe> METALFORMER_EXTRUDING =
            register("metal_former_extruding", MetalFormerExtrudingCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<MolecularTransformerCraftingRecipe> MOLECULAR_TRANSFORMER =
            register("molecular_transformer", MolecularTransformerCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<ExtractorCraftingRecipe> EXTRACTOR =
            register("extractor", ExtractorCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<CuttingCraftingRecipe> CUTTING =
            register("cutting", CuttingCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<RecyclerCraftingRecipe> RECYCLER =
            register("recycler", RecyclerCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<BrewReactorCraftingRecipe> BREW_REACTOR =
            register("brew_reactor", BrewReactorCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<OreWashingCraftingRecipe> ORE_WASHING =
            register("ore_washing", OreWashingCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<ModBlastFurnaceCraftingRecipe> MOD_BLAST_FURNACE =
            register("mod_blast_furnace", ModBlastFurnaceCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<HeatCentrifugeCraftingRecipe> HEAT_CENTRIFUGE =
            register("heat_centrifuge", HeatCentrifugeCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<PatternStorageCraftingRecipe> PATTERN_STORAGE =
            register("pattern_storage", PatternStorageCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<CoffeeMachineCraftingRecipe> COFFEE_MACHINE =
            register("coffee_machine", CoffeeMachineCraftingRecipe.SERIALIZER);
    public static final RecipeRegistration<CropFarmCraftingRecipe> CROP_FARM =
            register("crop_farm", CropFarmCraftingRecipe.SERIALIZER);


    public static <R extends Recipe<?>> RecipeRegistration<R> register(String idName, RecipeSerializer<R> serializer) {
        Identifier id = Identifier.fromNamespaceAndPath(IndustrialElixir.MOD_ID, idName);

        RecipeType<R> type = Registry.register(BuiltInRegistries.RECIPE_TYPE, id, new RecipeType<R>() {
            @Override
            public String toString() {
                return idName;
            }
        });
        RecipeSerializer<R> ser = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);

        return new RecipeRegistration<>(type, ser);
    }

    public static class RecipeRegistration<R extends Recipe<?>> {
        public final RecipeType<R> type;
        public final RecipeSerializer<R> serializer;

        public RecipeRegistration(RecipeType<R> type, RecipeSerializer<R> serializer) {
            this.type = type;
            this.serializer = serializer;
        }
    }

    public static void registerRecipes() {
        IndustrialElixir.LOGGER.info("Registering Custom Recipes for " + IndustrialElixir.MOD_ID);
    }
}

