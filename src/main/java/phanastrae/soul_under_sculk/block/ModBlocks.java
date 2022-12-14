package phanastrae.soul_under_sculk.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class ModBlocks {
	//public static final Block SCULK_VENT = new SculkVentBlock(QuiltBlockSettings.of(Material.SCULK).strength(0.2F).sounds(BlockSoundGroup.SCULK));
	public static final Block CREATIVE_VERUM_CHARGER = new CreativeVerumChargerBlock(QuiltBlockSettings.of(Material.SCULK).sounds(BlockSoundGroup.GLASS).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning((a, b, c, d) -> false).luminance(15));

	public static void init() {
		//register(SCULK_VENT, "sculk_vent");
		register(CREATIVE_VERUM_CHARGER, "creative_verum_charger");
	}

	public static <T extends Block> void register(T block, String name) {
		Registry.register(Registry.BLOCK, SoulUnderSculk.id(name), block);
	}
}
