package phanastrae.soul_under_sculk;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phanastrae.soul_under_sculk.block.ModBlocks;
import phanastrae.soul_under_sculk.item.ModItems;

public class SoulUnderSculk implements ModInitializer {

	public static String MOD_ID = "soul_under_sculk";

	public static ItemGroup ITEM_GROUP = QuiltItemGroup.builder(id("general")).icon(() -> new ItemStack(ModItems.SCULK_VENT)).build();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Soul Under Sculk");

	@Override
	public void onInitialize(ModContainer mod) {
		ModBlocks.init();
		ModItems.init();
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
