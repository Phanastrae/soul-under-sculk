package phanastrae.soul_under_sculk.transformation;

import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class SculkmateTransformation extends TransformationType {
	@Override
	public Identifier getIconId() {
		return SoulUnderSculk.id("textures/gui/soul_under_sculk/transformation_icon/sculkmate.png");
	}

	@Override
	public boolean shouldRenderIcon() {
		return true;
	}
}
