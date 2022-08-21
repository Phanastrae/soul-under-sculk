package phanastrae.soul_under_sculk.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class ModBlockEntities {
	public static final BlockEntityType<CreativeVerumChargerBlockEntity> CREATIVE_VERUM_CHARGER = Registry.register(Registry.BLOCK_ENTITY_TYPE, SoulUnderSculk.id("creative_verum_charger_entity"),
			FabricBlockEntityTypeBuilder.create(CreativeVerumChargerBlockEntity::new, ModBlocks.CREATIVE_VERUM_CHARGER).build());

	public static void init() {
	}
}
