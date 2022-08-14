package phanastrae.soul_under_sculk.block;

import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.intprovider.ConstantIntProvider;

public class SculkVentBlock extends ExperienceDroppingBlock {
	public SculkVentBlock(Settings settings) {
		super(settings, ConstantIntProvider.create(5));
	}
}
