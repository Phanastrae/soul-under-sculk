package phanastrae.soul_under_sculk.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.block.ModBlocks;

public class ModItems {
	public static final Item VERUM = new VerumItem(getSettings().maxCount(1).rarity(Rarity.UNCOMMON));
	public static final Item REGRESSION_VIAL = new RegressionVialItem(getSettings());
	public static final Item BIOMASS = new BiomassItem(getSettings());
	public static final Item CREATIVE_VERUM_CHARGER = new BlockItem(ModBlocks.CREATIVE_VERUM_CHARGER, getSettings().rarity(Rarity.EPIC));
	//public static final Item SCULK_VENT = new SculkVentItem(ModBlocks.SCULK_VENT, getSettings());

	public static QuiltItemSettings getSettings() {
		return new QuiltItemSettings().group(SoulUnderSculk.ITEM_GROUP);
	}

	public static void init() {
		registerItem(VERUM, "verum");
		registerItem(REGRESSION_VIAL, "regression_vial");
		registerItem(BIOMASS, "biomass");
		registerItem(CREATIVE_VERUM_CHARGER, "creative_verum_charger");
		//registerItem(SCULK_VENT, "sculk_vent");
	}

	public static <T extends Item> void registerItem(T item, String name) {
		Registry.register(Registry.ITEM, SoulUnderSculk.id(name), item);
	}
}
