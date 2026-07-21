package com.skniro.industrial_elixir.datagen;

import com.skniro.industrial_elixir.block.GeneralBlocks;
import com.skniro.industrial_elixir.block.GrowableOresBlocks;
import com.skniro.industrial_elixir.block.MapleSignBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidBlocks;
import com.skniro.industrial_elixir.fluid.IndustrialElixirFluidItems;
import com.skniro.industrial_elixir.init.FurnitureStrings;
import com.skniro.industrial_elixir.item.AdvancedItems;
import com.skniro.industrial_elixir.item.GrowableOresItems;
import com.skniro.industrial_elixir.item.MapleArmorItems;
import com.skniro.industrial_elixir.item.ModCreativeTab;
import com.skniro.growableoresir.block.GrowableICOresBlocks;
import com.skniro.industrial_elixir.keybind.ModKeyMappings;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class IndustrialElixirSimplifiedChineseLanguageProvider extends FabricLanguageProvider {
    public IndustrialElixirSimplifiedChineseLanguageProvider(FabricPackOutput dataGenerator, CompletableFuture<HolderLookup.Provider> registryLookup){
        super(dataGenerator,"zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder){
        translationBuilder.add(FurnitureStrings.Macerator, "粉碎机");
        translationBuilder.add(FurnitureStrings.CaneConverter, "甘蔗转换机");
        translationBuilder.add(FurnitureStrings.Compressor, "压缩机");
        translationBuilder.add(FurnitureStrings.MetalFormer, "金属成型机");
        translationBuilder.add(FurnitureStrings.GeneratorWindMill, "风力发电机");
        translationBuilder.add(FurnitureStrings.CoalGenerator, "火力发电机");
        translationBuilder.add(FurnitureStrings.NuclearReactor, "核反应堆");
        translationBuilder.add(FurnitureStrings.Extractor, "提取机");
        translationBuilder.add(FurnitureStrings.ROLLING, "压板模式");
        translationBuilder.add(FurnitureStrings.CUTTING, "切割模式");
        translationBuilder.add(FurnitureStrings.EXTRUDING, "挤压模式");
        translationBuilder.add(FurnitureStrings.ERROR, "无效");
        translationBuilder.add(GrowableOresBlocks.GrowableOres_Block, "甘蔗转换机");
        translationBuilder.add(FurnitureStrings.ElectricFurnace, "电炉");
        translationBuilder.add(FurnitureStrings.InductionFurnace, "感应炉");
        translationBuilder.add(FurnitureStrings.Block_Cutter, "方块切割机");
        translationBuilder.add(FurnitureStrings.Recycler, "回收机");

        translationBuilder.add(FurnitureStrings.Charge_Pad, "充电座");
        translationBuilder.add(FurnitureStrings.Charge_Pad_CESU, "CESU充电座");
        translationBuilder.add(FurnitureStrings.Charge_Pad_MFE, "MFE充电座");
        translationBuilder.add(FurnitureStrings.Charge_Pad_MFSU, "MFSU充电座");

        translationBuilder.add(FurnitureStrings.Energy_Box, "储电箱");
        translationBuilder.add(FurnitureStrings.Energy_Box_CESU, "CESU储电箱");
        translationBuilder.add(FurnitureStrings.Energy_Box_MFE, "MFE储电箱");
        translationBuilder.add(FurnitureStrings.Energy_Box_MFSU, "MFSU储电箱");

        translationBuilder.add(FurnitureStrings.GENERATOR_SolarPanel, "太阳能发电机");
        translationBuilder.add(FurnitureStrings.GENERATOR_ADVANCED_SOLAR_PANEL, "高级太阳能发电机");
        translationBuilder.add(FurnitureStrings.GENERATOR_HYBRID_SOLAR_PANEL, "混合太阳能发电机");
        translationBuilder.add(FurnitureStrings.GENERATOR_ULTIMATE_SOLAR_PANEL, "终极太阳能发电机");
        translationBuilder.add(FurnitureStrings.GENERATOR_QUANTUM_SOLAR_PANEL, "量子太阳能发电机");

        translationBuilder.add(FurnitureStrings.MolecularTransformer, "分子重组仪");

        translationBuilder.add(FurnitureStrings.IRON_FURNACE, "铁炉");

        translationBuilder.add(FurnitureStrings.Fluid_Cell, "流体单元");
        translationBuilder.add(FurnitureStrings.BrewReactor, "酿造反应器");
        translationBuilder.add(FurnitureStrings.OreWashing, "洗矿机");
        translationBuilder.add(FurnitureStrings.Fluid_Tank, "流体罐");
        translationBuilder.add(FurnitureStrings.Blast_Furnace, "高炉");
        translationBuilder.add(FurnitureStrings.Electric_Heater, "电力加热机");
        translationBuilder.add(FurnitureStrings.Heat_Centrifuge, "热能离心机");
        translationBuilder.add(FurnitureStrings.Solid_Fuel_Heater, "固体加热机");
        translationBuilder.add(FurnitureStrings.Pattern_Storage, "模式存储机");
        translationBuilder.add(FurnitureStrings.Replicator, "复制机");
        translationBuilder.add(FurnitureStrings.Fluid_Generator, "流体发电机");
        translationBuilder.add(FurnitureStrings.ChunkLoader, "区块加载器");
        translationBuilder.add(FurnitureStrings.Matter_Generator, "物质生成机");

        translationBuilder.add("gui.industrial_elixir.pattern_storage.copy", "复制");
        translationBuilder.add("gui.industrial_elixir.pattern_storage.uu_tooltip", "每次复制消耗的液态UU");
        translationBuilder.add("gui.industrial_elixir.pattern_storage.energy_tooltip", "每次复制消耗的电量");

        translationBuilder.add("tooltip.industrial_elixir.pattern_storage.stored_item", "存储物品");
        translationBuilder.add("tooltip.industrial_elixir.pattern_storage.energy_cost", "能量消耗");
        translationBuilder.add("tooltip.industrial_elixir.pattern_storage.uu_cost", "UU物质消耗");

        translationBuilder.add("gui.industrial_elixir.replicator.stop", "停止");
        translationBuilder.add("gui.industrial_elixir.replicator.single", "合成1次");
        translationBuilder.add("gui.industrial_elixir.replicator.loop", "循环合成");

        translationBuilder.add("gui.industrial_elixir.sacred_generator", "神圣石发电机");

        translationBuilder.add(GeneralBlocks.Lead_Ore, "铅矿石");
        translationBuilder.add(GeneralBlocks.Tin_Ore, "锡矿石");
        translationBuilder.add(GeneralBlocks.SACRED_Ore, "神圣石矿石");
        translationBuilder.add(GeneralBlocks.Deepslate_Lead_Ore, "深板岩铅矿石");
        translationBuilder.add(GeneralBlocks.Deepslate_Tin_Ore, "深板岩锡矿石");
        translationBuilder.add(GeneralBlocks.Deepslate_SACRED_Ore, "深板岩神圣石矿石");

        translationBuilder.add(GeneralBlocks.Raw_Lead_Block, "粗铅块");
        translationBuilder.add(GeneralBlocks.Raw_Tin_Block, "粗锡块");
        translationBuilder.add(GeneralBlocks.Raw_SACRED_Block, "粗神圣矿石块");

        translationBuilder.add(GeneralBlocks.Lead_Block, "铅块");
        translationBuilder.add(GeneralBlocks.Tin_Block, "锡块");
        translationBuilder.add(GeneralBlocks.SACRED_Block, "神圣石块");

        translationBuilder.add(GeneralBlocks.Silver_Block, "银块");
        translationBuilder.add(GeneralBlocks.Bronze_Block, "青铜块");
        translationBuilder.add(GeneralBlocks.Steel_Block, "钢块");

        translationBuilder.add(GeneralBlocks.Super_Machine, "超级机器外壳");
        translationBuilder.add(GeneralBlocks.Advanced_Machine, "高级机器外壳");
        translationBuilder.add(GeneralBlocks.Machine, "基础机器外壳");

        translationBuilder.add(GeneralBlocks.Rubber_SAPLING, "橡胶树树苗");
        translationBuilder.add(GeneralBlocks.POTTED_Rubber_SAPLING, "盆栽橡胶树树苗");
        translationBuilder.add(GeneralBlocks.Rubber_LEAVES, "橡胶树树叶");

        translationBuilder.add(GeneralBlocks.Rubber_Rubber_LOG, "产胶橡胶木");

        translationBuilder.add(GeneralBlocks.Rubber_LOG, "橡胶木");
        translationBuilder.add(GeneralBlocks.STRIPPED_Rubber_LOG, "去皮橡胶木");
        translationBuilder.add(GeneralBlocks.STRIPPED_Rubber_WOOD, "去皮橡胶原木");
        translationBuilder.add(GeneralBlocks.Rubber_WOOD, "橡胶原木");

        translationBuilder.add(GeneralBlocks.Rubber_PLANKS, "橡胶木板");
        translationBuilder.add(GeneralBlocks.Rubber_BUTTON, "橡胶木按钮");
        translationBuilder.add(GeneralBlocks.Rubber_STAIRS, "橡胶木楼梯");
        translationBuilder.add(GeneralBlocks.Rubber_SLAB, "橡胶木台阶");
        translationBuilder.add(GeneralBlocks.Rubber_FENCE_GATE, "橡胶木栅栏门");
        translationBuilder.add(GeneralBlocks.Rubber_FENCE, "橡胶木栅栏");
        translationBuilder.add(GeneralBlocks.Rubber_DOOR, "橡胶木门");
        translationBuilder.add(GeneralBlocks.Rubber_TRAPDOOR, "橡胶木活板门");
        translationBuilder.add(GeneralBlocks.Rubber_PRESSURE_PLATE, "橡胶木压力板");
        translationBuilder.add(GeneralBlocks.Rubber_SHELF, "橡胶木展示格");
        translationBuilder.add(GeneralBlocks.Reinforced_Glass, "防爆玻璃");
        translationBuilder.add(GeneralBlocks.Reinforced_Stone, "防爆石");
        translationBuilder.add(GeneralBlocks.Reinforced_DOOR, "防爆门");

        translationBuilder.add(GrowableOresBlocks.Macerator_Block, "粉碎机");
        translationBuilder.add(GrowableOresBlocks.Compressor_Block, "压缩机");
        translationBuilder.add(GrowableOresBlocks.MetalFormerBlock, "金属成型机");
        translationBuilder.add(GrowableOresBlocks.Ore_Washing_Block, "洗矿机");

        translationBuilder.add(GrowableOresBlocks.COPPER_CABLE, "铜电缆");
        translationBuilder.add(GrowableOresBlocks.TIN_CABLE, "锡电缆");
        translationBuilder.add(GrowableOresBlocks.GOLD_CABLE, "金电缆");
        translationBuilder.add(GrowableOresBlocks.HV_CABLE, "高压电缆");
        translationBuilder.add(GrowableOresBlocks.GLASSFIBER_CABLE, "玻璃纤维电缆");

        translationBuilder.add(GrowableOresBlocks.INSULATED_TIN_CABLE, "绝缘锡电缆");
        translationBuilder.add(GrowableOresBlocks.INSULATED_COPPER_CABLE, "绝缘铜电缆");
        translationBuilder.add(GrowableOresBlocks.INSULATED_GOLD_CABLE, "绝缘金电缆");
        translationBuilder.add(GrowableOresBlocks.INSULATED_HV_CABLE, "绝缘高压电缆");

        translationBuilder.add(GrowableOresBlocks.COAL_GENERATOR, "火力发电机");
        translationBuilder.add(GrowableOresBlocks.NUCLEAR_REACTOR, "核反应堆");
        translationBuilder.add(GrowableOresBlocks.SACRED_GENERATOR, "神圣石发电机");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_Wind_Mill, "风力发电机");

        translationBuilder.add(GrowableOresBlocks.EnergyBox, "储能箱");
        translationBuilder.add(GrowableOresBlocks.ChargePad, "充电板");

        translationBuilder.add(GrowableOresBlocks.CESU, "CESU 储能单元");
        translationBuilder.add(GrowableOresBlocks.MFE, "MFE 储能单元");
        translationBuilder.add(GrowableOresBlocks.MFSU, "MFSU 储能单元");

        translationBuilder.add(GrowableOresBlocks.CESU_CHARGE_PAD, "CESU 充电板");
        translationBuilder.add(GrowableOresBlocks.MFE_CHARGE_PAD, "MFE 充电板");
        translationBuilder.add(GrowableOresBlocks.MFSU_CHARGE_PAD, "MFSU 充电板");

        translationBuilder.add(GrowableOresBlocks.MolecularTransformerBlock, "分子转换机");

        translationBuilder.add(GrowableOresBlocks.GENERATOR_SolarPanel, "太阳能发电机");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_ADVANCED_SOLAR_PANEL, "高级太阳能发电机");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_HYBRID_SOLAR_PANEL, "混合太阳能发电机");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_ULTIMATE_SOLAR_PANEL, "终极太阳能发电机");
        translationBuilder.add(GrowableOresBlocks.GENERATOR_QUANTUM_SOLAR_PANEL, "量子太阳能发电机");

        translationBuilder.add(GrowableOresBlocks.Extractor_Block, "提取机");

        translationBuilder.add(GrowableOresBlocks.EV_TRANSFORMER, "超高压变压器（仅用做合成材料）");
        translationBuilder.add(GrowableOresBlocks.HV_TRANSFORMER, "高压变压器（仅用做合成材料）");
        translationBuilder.add(GrowableOresBlocks.MV_TRANSFORMER, "中压变压器（仅用做合成材料）");
        translationBuilder.add(GrowableOresBlocks.LV_TRANSFORMER, "低压变压器（仅用做合成材料）");

        translationBuilder.add(GrowableOresBlocks.Iron_Furnace_Block, "铁炉");
        translationBuilder.add(GrowableOresBlocks.INDUCTION_FURNACE, "感应炉");
        translationBuilder.add(GrowableOresBlocks.Brew_Reactor_BLOCK, "酿造反应器");
        translationBuilder.add(GrowableOresBlocks.Electric_Heater_Block, "电加热器");
        translationBuilder.add(GrowableOresBlocks.BLAST_FURNACE_BLOCK, "热高炉");
        translationBuilder.add(GrowableOresBlocks.HEAT_CENTRIFUGE, "热能离心机");
        translationBuilder.add(GrowableOresBlocks.SOLID_FUEL_HEATER_GENERATOR, "固体加热机");
        translationBuilder.add(GrowableOresBlocks.PATTERN_STORAGE, "模式存储机");
        translationBuilder.add(GrowableOresBlocks.Replicator, "复制机");
        translationBuilder.add(GrowableOresBlocks.FLUID_GENERATOR, "流体发电机");
        translationBuilder.add(GrowableOresBlocks.CHUNK_LOADER, "区块加载器");
        translationBuilder.add(GrowableOresBlocks.FLUID_TANK_BLOCK, "流体储罐");
        translationBuilder.add(GrowableOresBlocks.Pipe_Stone_Fluid_Block, "石制流体管道");
        translationBuilder.add(GrowableOresBlocks.Pipe_Stone_Item_Block, "石制物品管道");
        translationBuilder.add(GrowableOresBlocks.Pipe_Wooden_Fluid_Block, "木制流体管道");
        translationBuilder.add(GrowableOresBlocks.Pipe_Wooden_Iten_Block, "木制物品管道");
        translationBuilder.add(GrowableOresBlocks.MATTER_GENERATOR, "物质生成机");

        translationBuilder.add(MapleSignBlocks.Rubber_SIGN, "橡胶木告示牌");
        translationBuilder.add(MapleSignBlocks.Rubber_WALL_SIGN, "橡胶木墙上告示牌");
        translationBuilder.add(MapleSignBlocks.Rubber_HANGING_SIGN, "悬挂式橡胶木告示牌");
        translationBuilder.add(MapleSignBlocks.Rubber_WALL_HANGING_SIGN, "墙上悬挂式橡胶木告示牌");

        translationBuilder.add(AdvancedItems.IRRADIANT_SEPTRIN_INGOT, "光辉神圣石锭");
        translationBuilder.add(AdvancedItems.IRRADIANT_GLASS_PANE, "光辉玻璃板");
        translationBuilder.add(AdvancedItems.LUMINITE, "辉光石");
        translationBuilder.add(AdvancedItems.ENRICHED_LUMINITE, "富集辉光石");
        translationBuilder.add(AdvancedItems.LUMINITE_ALLOY, "辉光合金锭");
        translationBuilder.add(AdvancedItems.ENRICHED_LUMINITE_ALLOY, "富集辉光合金锭");
        translationBuilder.add(AdvancedItems.IRIDIUM_AMETHYST_PLATE, "铱紫水晶板");
        translationBuilder.add(AdvancedItems.REINFORCED_IRIDIUM_AMETHYST_PLATE, "强化铱紫水晶板");
        translationBuilder.add(AdvancedItems.IRRADIANT_REINFORCED_PLATE, "光辉强化铱紫水晶板");
        translationBuilder.add(AdvancedItems.LUMINITE_Part, "小型辉光化合物");
        translationBuilder.add(AdvancedItems.Iridium_INGOT, "铱锭");
        translationBuilder.add(AdvancedItems.Quantum_Core, "量子核心");
        translationBuilder.add(AdvancedItems.MT_Core, "分子转换核心");
        translationBuilder.add(AdvancedItems.PIPE_PLUG, "管道塞");

        translationBuilder.add(GrowableOresItems.RE_BATTERY, "充电电池");
        translationBuilder.add(GrowableOresItems.ADVANCED_RE_BATTERY, "高级充电电池");
        translationBuilder.add(GrowableOresItems.ENERGY_CRYSTAL, "能量水晶");
        translationBuilder.add(GrowableOresItems.LAPOTRON_CRYSTAL, "拉普顿水晶");

        translationBuilder.add(GrowableOresItems.Rubber, "橡胶");
        translationBuilder.add(GrowableOresItems.RUBBER_BOAT, "橡胶船");
        translationBuilder.add(GrowableOresItems.RUBBER_CHEST_BOAT, "橡胶箱船");

        translationBuilder.add(GrowableOresItems.OVERCLOCKER, "超频升级");
        translationBuilder.add(GrowableOresItems.ENERGY_STORAGE, "储能升级");
        translationBuilder.add(GrowableOresItems.TRANSFORMER, "高压升级");
        translationBuilder.add(GrowableOresItems.REDSTONE_INVERTER, "红石反相升级");
        translationBuilder.add(GrowableOresItems.EMPTY_CELL, "空单元");
        translationBuilder.add(GrowableOresItems.SACRED_ESSENCE, "神圣石燃料棒");
        translationBuilder.add(GrowableOresItems.SACRED_SHARD, "神圣石碎片");
        translationBuilder.add(GrowableOresItems.SACRED_CORE, "神圣核心");
        translationBuilder.add(GrowableOresItems.EMPTY_VESSEL, "空容器");
        translationBuilder.add(GrowableOresItems.IMPURE_SACRED_STONE, "低纯神圣石");
        translationBuilder.add(GrowableOresItems.COOLANT_CELL_10K, "10k冷却单元");
        translationBuilder.add(GrowableOresItems.COOLANT_CELL_30K, "30k冷却单元");
        translationBuilder.add(GrowableOresItems.COOLANT_CELL_60K, "60k冷却单元");
        translationBuilder.add(GrowableOresItems.HEAT_VENT, "散热片");
        translationBuilder.add(GrowableOresItems.ADVANCED_HEAT_VENT, "高级散热片");
        translationBuilder.add(GrowableOresItems.OVERCLOCKED_HEAT_VENT, "超频散热片");
        translationBuilder.add(GrowableOresItems.REACTOR_PLATING, "反应堆隔板");
        translationBuilder.add(GrowableOresItems.NEUTRON_REFLECTOR, "中子反射板");
        translationBuilder.add(GrowableOresItems.HEAT_EXCHANGER, "热交换器");

        translationBuilder.add(GrowableOresItems.Raw_Lead, "粗铅");
        translationBuilder.add(GrowableOresItems.Raw_Tin, "粗锡");
        translationBuilder.add(GrowableOresItems.Raw_SACRED, "粗神圣矿石");

        translationBuilder.add(GrowableOresItems.CRUSHED_COPPER, "粉碎铜矿");
        translationBuilder.add(GrowableOresItems.CRUSHED_GOLD, "粉碎金矿");
        translationBuilder.add(GrowableOresItems.CRUSHED_IRON, "粉碎铁矿");
        translationBuilder.add(GrowableOresItems.CRUSHED_LEAD, "粉碎铅矿");
        translationBuilder.add(GrowableOresItems.CRUSHED_SILVER, "粉碎银矿");
        translationBuilder.add(GrowableOresItems.CRUSHED_TIN, "粉碎锡矿");
        translationBuilder.add(GrowableOresItems.CRUSHED_SACRED, "粉碎神圣石矿");

        translationBuilder.add(GrowableOresItems.PURIFIED_COPPER, "纯净铜矿");
        translationBuilder.add(GrowableOresItems.PURIFIED_GOLD, "纯净金矿");
        translationBuilder.add(GrowableOresItems.PURIFIED_IRON, "纯净铁矿");
        translationBuilder.add(GrowableOresItems.PURIFIED_LEAD, "纯净铅矿");
        translationBuilder.add(GrowableOresItems.PURIFIED_SILVER, "纯净银矿");
        translationBuilder.add(GrowableOresItems.PURIFIED_TIN, "纯净锡矿");
        translationBuilder.add(GrowableOresItems.PURIFIED_SACRED, "纯净神圣石矿");

        translationBuilder.add(GrowableOresItems.BRONZE_INGOT, "青铜锭");
        translationBuilder.add(GrowableOresItems.LEAD_INGOT, "铅锭");
        translationBuilder.add(GrowableOresItems.SILVER_INGOT, "银锭");
        translationBuilder.add(GrowableOresItems.STEEL_INGOT, "钢锭");
        translationBuilder.add(GrowableOresItems.TIN_INGOT, "锡锭");
        translationBuilder.add(GrowableOresItems.SACRED_INGOT, "神圣石锭");
        translationBuilder.add(GrowableOresItems.CARBON_FIBRE, "碳纤维");
        translationBuilder.add(GrowableOresItems.CARBON_MESH, "碳纤维网");
        translationBuilder.add(GrowableOresItems.CARBON_PLATE, "碳板");
        translationBuilder.add(GrowableOresItems.COAL_BALL, "煤球");
        translationBuilder.add(GrowableOresItems.COAL_BLOCK, "煤块");
        translationBuilder.add(GrowableOresItems.COAL_CHUNK, "煤块");
        translationBuilder.add(GrowableOresItems.INDUSTRIAL_DIAMOND, "工业钻石");

        translationBuilder.add(GrowableOresItems.Circuit, "电路板");
        translationBuilder.add(GrowableOresItems.Advanced_Circuit, "高级电路板");
        translationBuilder.add(GrowableOresItems.Coil, "线圈");

        translationBuilder.add(GrowableOresItems.IRIDIUM_SHARD, "铱碎片");
        translationBuilder.add(GrowableOresItems.IRIDIUM_ORE, "铱矿石");
        translationBuilder.add(GrowableOresItems.IRIDIUM_PLATE, "强化铱板");

        translationBuilder.add(GrowableOresItems.ALLOY_INGOT, "高级合金锭");
        translationBuilder.add(GrowableOresItems.ALLOY_PLATE, "高级合金板");

        translationBuilder.add(GrowableOresItems.Sticky_Resin, "粘性树脂");

        translationBuilder.add(MapleArmorItems.ROLLING, "锻造锤");
        translationBuilder.add(MapleArmorItems.CUTTING, "板材切割剪刀");

        translationBuilder.add(MapleArmorItems.Quantum_HELMET, "量子头盔");
        translationBuilder.add(MapleArmorItems.Quantum_CHESTPLATE, "量子胸甲");
        translationBuilder.add(MapleArmorItems.Quantum_LEGGINGS, "量子护腿");
        translationBuilder.add(MapleArmorItems.Quantum_BOOTS, "量子靴子");

        translationBuilder.add(MapleArmorItems.Electric_Jetpack, "电力喷气背包");

        translationBuilder.add(GrowableOresItems.BRONZE_DUST, "青铜粉");
        translationBuilder.add(GrowableOresItems.CLAY_DUST, "黏土粉");
        translationBuilder.add(GrowableOresItems.COAL_DUST, "煤粉");
        translationBuilder.add(GrowableOresItems.COPPER_DUST, "铜粉");
        translationBuilder.add(GrowableOresItems.DIAMOND_DUST, "钻石粉");
        translationBuilder.add(GrowableOresItems.ENERGIUM_DUST, "能量粉");
        translationBuilder.add(GrowableOresItems.GOLD_DUST, "金粉");
        translationBuilder.add(GrowableOresItems.IRON_DUST, "铁粉");
        translationBuilder.add(GrowableOresItems.LAPIS_DUST, "青金石粉");
        translationBuilder.add(GrowableOresItems.LEAD_DUST, "铅粉");
        translationBuilder.add(GrowableOresItems.LITHIUM_DUST, "锂粉");
        translationBuilder.add(GrowableOresItems.OBSIDIAN_DUST, "黑曜石粉");
        translationBuilder.add(GrowableOresItems.SILICON_DIOXIDE_DUST, "二氧化硅粉");
        translationBuilder.add(GrowableOresItems.SILVER_DUST, "银粉");
        translationBuilder.add(GrowableOresItems.STONE_DUST, "石粉");
        translationBuilder.add(GrowableOresItems.SULFUR_DUST, "硫磺粉");
        translationBuilder.add(GrowableOresItems.TIN_DUST, "锡粉");

        translationBuilder.add(GrowableOresItems.SMALL_BRONZE_DUST, "小撮青铜粉");
        translationBuilder.add(GrowableOresItems.SMALL_COPPER_DUST, "小撮铜粉");
        translationBuilder.add(GrowableOresItems.SMALL_GOLD_DUST, "小撮金粉");
        translationBuilder.add(GrowableOresItems.SMALL_IRON_DUST, "小撮铁粉");
        translationBuilder.add(GrowableOresItems.SMALL_LAPIS_DUST, "小撮青金石粉");
        translationBuilder.add(GrowableOresItems.SMALL_LEAD_DUST, "小撮铅粉");
        translationBuilder.add(GrowableOresItems.SMALL_LITHIUM_DUST, "小撮锂粉");
        translationBuilder.add(GrowableOresItems.SMALL_OBSIDIAN_DUST, "小撮黑曜石粉");
        translationBuilder.add(GrowableOresItems.SMALL_SILVER_DUST, "小撮银粉");
        translationBuilder.add(GrowableOresItems.SMALL_SULFUR_DUST, "小撮硫磺粉");
        translationBuilder.add(GrowableOresItems.SMALL_TIN_DUST, "小撮锡粉");

        translationBuilder.add(GrowableOresItems.BRONZE_PLATE, "青铜板");
        translationBuilder.add(GrowableOresItems.COPPER_PLATE, "铜板");
        translationBuilder.add(GrowableOresItems.GOLD_PLATE, "金板");
        translationBuilder.add(GrowableOresItems.IRON_PLATE, "铁板");
        translationBuilder.add(GrowableOresItems.LAPIS_PLATE, "青金石板");
        translationBuilder.add(GrowableOresItems.LEAD_PLATE, "铅板");
        translationBuilder.add(GrowableOresItems.OBSIDIAN_PLATE, "黑曜石板");
        translationBuilder.add(GrowableOresItems.STEEL_PLATE, "钢板");
        translationBuilder.add(GrowableOresItems.TIN_PLATE, "锡板");

        translationBuilder.add(GrowableOresItems.DENSE_BRONZE_PLATE, "致密青铜板");
        translationBuilder.add(GrowableOresItems.DENSE_COPPER_PLATE, "致密铜板");
        translationBuilder.add(GrowableOresItems.DENSE_GOLD_PLATE, "致密金板");
        translationBuilder.add(GrowableOresItems.DENSE_IRON_PLATE, "致密铁板");
        translationBuilder.add(GrowableOresItems.DENSE_LAPIS_PLATE, "致密青金石板");
        translationBuilder.add(GrowableOresItems.DENSE_LEAD_PLATE, "致密铅板");
        translationBuilder.add(GrowableOresItems.DENSE_OBSIDIAN_PLATE, "致密黑曜石板");
        translationBuilder.add(GrowableOresItems.DENSE_STEEL_PLATE, "致密钢板");
        translationBuilder.add(GrowableOresItems.DENSE_TIN_PLATE, "致密锡板");

        translationBuilder.add(GrowableOresItems.BRONZE_CASING, "青铜外壳");
        translationBuilder.add(GrowableOresItems.COPPER_CASING, "铜外壳");
        translationBuilder.add(GrowableOresItems.GOLD_CASING, "金外壳");
        translationBuilder.add(GrowableOresItems.IRON_CASING, "铁外壳");
        translationBuilder.add(GrowableOresItems.LEAD_CASING, "铅外壳");
        translationBuilder.add(GrowableOresItems.STEEL_CASING, "钢外壳");
        translationBuilder.add(GrowableOresItems.TIN_CASING, "锡外壳");

        translationBuilder.add(ModCreativeTab.General, "基本");
        translationBuilder.add(ModCreativeTab.Generators_And_Wiring, "发电机与导线");
        translationBuilder.add(ModCreativeTab.Reactor, "神圣反应堆");
        translationBuilder.add(ModCreativeTab.Machine, "机器");
        translationBuilder.add(ModCreativeTab.Tool_And_Utilities, "工具与实用物品");
        translationBuilder.add(ModCreativeTab.Combat, "战斗");
        translationBuilder.add(ModCreativeTab.Materials, "材料");

        translationBuilder.add(GrowableOresBlocks.ElectricFurnace_Block, "电炉");
        translationBuilder.add(GrowableOresBlocks.RECYCLER_Block, "回收机");
        translationBuilder.add(GrowableOresBlocks.CUTTING_Block, "方块切割机");
        translationBuilder.add(GrowableOresItems.Electric_Motor, "电动马达");
        translationBuilder.add(GrowableOresItems.Scrap, "废料");
        translationBuilder.add(GrowableOresItems.Scrap_box, "废料盒");

        translationBuilder.add(MapleArmorItems.BRONZE_BOOTS, "青铜靴");
        translationBuilder.add(MapleArmorItems.BRONZE_CHESTPLATE, "青铜胸甲");
        translationBuilder.add(MapleArmorItems.BRONZE_HELMET, "青铜头盔");
        translationBuilder.add(MapleArmorItems.BRONZE_LEGGINGS, "青铜护腿");

        translationBuilder.add(MapleArmorItems.BRONZE_PICKAXE, "青铜镐");
        translationBuilder.add(MapleArmorItems.BRONZE_AXE, "青铜斧");
        translationBuilder.add(MapleArmorItems.BRONZE_SHOVEL, "青铜锹");
        translationBuilder.add(MapleArmorItems.BRONZE_SWORD, "青铜剑");
        translationBuilder.add(MapleArmorItems.BRONZE_HOE, "青铜锄");

        translationBuilder.add(GrowableOresItems.SLAG, "炉渣");
        translationBuilder.add(GrowableOresItems.ASHES, "小堆灰烬");
        translationBuilder.add(GrowableOresItems.PATTERN_STORAGE_CRYSTAL, "模式存储水晶");
        translationBuilder.add(GrowableOresItems.RAW_PATTERN_STORAGE_CRYSTAL, "粗制模式存储水晶");
        translationBuilder.add(GrowableOresItems.WATER_CELL, "水单元");
        translationBuilder.add(GrowableOresItems.LAVA_CELL, "熔岩单元");
        translationBuilder.add(IndustrialElixirFluidItems.UU_CELL, "UU物质单元");

        translationBuilder.add(IndustrialElixirFluidItems.Fluid_UU_BUCKET, "UU物质桶");
        translationBuilder.add(IndustrialElixirFluidItems.Fluid_AIR_BUCKET, "压缩空气桶");
        translationBuilder.add(IndustrialElixirFluidItems.AIR_CELL, "压缩空气单元");
        translationBuilder.add(GrowableOresItems.HEAT_CONDUCTOR, "热传导器");

        translationBuilder.add(GrowableICOresBlocks.IER_Bronze_Cane, "青铜甘蔗(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_silver_Cane, "银甘蔗(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_Tin_Cane, "锡甘蔗(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_SACRED_Cane, "神圣石甘蔗(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_steel_Cane, "钢甘蔗(Industrial Elixir)");
        translationBuilder.add(GrowableICOresBlocks.IER_LEAD_Cane, "铅甘蔗(Industrial Elixir)");

        //PLASTER
        translationBuilder.add(GeneralBlocks.GREEN_PLASTER,"绿色灰浆");
        translationBuilder.add(GeneralBlocks.PLASTER,"灰浆");
        translationBuilder.add(GeneralBlocks.ORANGE_PLASTER,"橙色灰浆");
        translationBuilder.add(GeneralBlocks.MAGENTA_PLASTER,"品红色灰浆");
        translationBuilder.add(GeneralBlocks.LIGHT_BLUE_PLASTER,"淡蓝色灰浆");
        translationBuilder.add(GeneralBlocks.YELLOW_PLASTER,"黄色灰浆");
        translationBuilder.add(GeneralBlocks.LIME_PLASTER,"黄绿色灰浆");
        translationBuilder.add(GeneralBlocks.PINK_PLASTER,"粉色灰浆");
        translationBuilder.add(GeneralBlocks.GRAY_PLASTER,"灰色灰浆");
        translationBuilder.add(GeneralBlocks.LIGHT_GRAY_PLASTER,"淡灰色灰浆");
        translationBuilder.add(GeneralBlocks.CYAN_PLASTER,"青色灰浆");
        translationBuilder.add(GeneralBlocks.PURPLE_PLASTER,"紫色灰浆");
        translationBuilder.add(GeneralBlocks.BLUE_PLASTER,"蓝色灰浆");
        translationBuilder.add(GeneralBlocks.BROWN_PLASTER,"棕色灰浆");
        translationBuilder.add(GeneralBlocks.RED_PLASTER,"红色灰浆");

        translationBuilder.add(IndustrialElixirFluidBlocks.Fluid_UU_BLOCK,"UU流体");
        translationBuilder.add("block.industrial_elixir.fluid_uu","UU流体");
        translationBuilder.add("fluid.industrial_elixir.fluid_air_block","空气流体");
        translationBuilder.add("block.industrial_elixir.fluid_air","空气流体");

        translationBuilder.add("key.category.industrial_elixir.industrial_elixir.keybinds", "工业灵药");
        translationBuilder.add("key.industrial_elixir.alt_key", "ALT键");
        translationBuilder.add("key.industrial_elixir.mode_switch_key", "模式切换键");
        translationBuilder.add("key.industrial_elixir.boost_key", "加速键");
        translationBuilder.add("industrial_elixir.tooltip.liquid.amount.with.capacity", "液体容量:");
    }
}
