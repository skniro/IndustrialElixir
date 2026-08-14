package com.skniro.industrial_elixir.recipe;

import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.recipe.machine.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface AlchemyRecipeType<T extends Recipe<?>> {
    DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, IndustrialElixir.MOD_ID);
    DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, IndustrialElixir.MOD_ID);

    RecipeRegistration<AlchemyCraftingRecipe> CANE_CONVERTER =
            register("cane_converter", AlchemyCraftingRecipe.SERIALIZER);
    RecipeRegistration<MaceratorCraftingRecipe> MACERATOR =
            register("macerator", MaceratorCraftingRecipe.SERIALIZER);
    RecipeRegistration<CompressorCraftingRecipe> COMPRESSOR =
            register("compressor", CompressorCraftingRecipe.SERIALIZER);
    RecipeRegistration<MetalFormerRollingCraftingRecipe> METALFORMER_ROLLING =
            register("metal_former_rolling", MetalFormerRollingCraftingRecipe.SERIALIZER);
    RecipeRegistration<MetalFormerCuttingCraftingRecipe> METALFORMER_CUTTING =
            register("metal_former_cutting", MetalFormerCuttingCraftingRecipe.SERIALIZER);
    RecipeRegistration<MetalFormerExtrudingCraftingRecipe> METALFORMER_EXTRUDING =
            register("metal_former_extruding", MetalFormerExtrudingCraftingRecipe.SERIALIZER);
    RecipeRegistration<MolecularTransformerCraftingRecipe> MOLECULAR_TRANSFORMER =
            register("molecular_transformer", MolecularTransformerCraftingRecipe.SERIALIZER);
    RecipeRegistration<ExtractorCraftingRecipe> EXTRACTOR =
            register("extractor", ExtractorCraftingRecipe.SERIALIZER);
    RecipeRegistration<CuttingCraftingRecipe> CUTTING =
            register("cutting", CuttingCraftingRecipe.SERIALIZER);
    RecipeRegistration<RecyclerCraftingRecipe> RECYCLER =
            register("recycler", RecyclerCraftingRecipe.SERIALIZER);
    RecipeRegistration<BrewReactorCraftingRecipe> BREW_REACTOR =
            register("brew_reactor", BrewReactorCraftingRecipe.SERIALIZER);
    RecipeRegistration<OreWashingCraftingRecipe> ORE_WASHING =
            register("ore_washing", OreWashingCraftingRecipe.SERIALIZER);
    RecipeRegistration<ModBlastFurnaceCraftingRecipe> MOD_BLAST_FURNACE =
            register("mod_blast_furnace", ModBlastFurnaceCraftingRecipe.SERIALIZER);
    RecipeRegistration<HeatCentrifugeCraftingRecipe> HEAT_CENTRIFUGE =
            register("heat_centrifuge", HeatCentrifugeCraftingRecipe.SERIALIZER);
    RecipeRegistration<PatternStorageCraftingRecipe> PATTERN_STORAGE =
            register("pattern_storage", PatternStorageCraftingRecipe.SERIALIZER);
    RecipeRegistration<CoffeeMachineCraftingRecipe> COFFEE_MACHINE =
            register("coffee_machine", CoffeeMachineCraftingRecipe.SERIALIZER);
    RecipeRegistration<CropFarmCraftingRecipe> CROP_FARM =
            register("crop_farm", CropFarmCraftingRecipe.SERIALIZER);

    static <R extends Recipe<?>> RecipeRegistration<R> register(String idName, RecipeSerializer<R> serializer) {
        Supplier<RecipeType<R>> type = TYPES.register(idName, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return idName;
            }
        });
        Supplier<RecipeSerializer<R>> ser = SERIALIZERS.register(idName, () -> serializer);
        return new RecipeRegistration<>(type, ser);
    }

    class RecipeRegistration<R extends Recipe<?>> {
        public final Supplier<RecipeType<R>> type;
        public final Supplier<RecipeSerializer<R>> serializer;

        RecipeRegistration(Supplier<RecipeType<R>> type, Supplier<RecipeSerializer<R>> serializer) {
            this.type = type;
            this.serializer = serializer;
        }
    }

    static void registerRecipes(IEventBus eventBus) {
        IndustrialElixir.LOGGER.info("Registering Custom Recipes for " + IndustrialElixir.MOD_ID);
        TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
