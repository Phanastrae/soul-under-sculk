package phanastrae.soul_under_sculk.transformation;

import net.minecraft.util.registry.Registry;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class ModTransformations {

	public static final TransformationType SCULKMATE = new SculkmateTransformation();
	public static final TransformationType EGG = new EggTransformation();

	public static void init() {
		register(SCULKMATE, "sculkmate");
		register(EGG, "egg");
	}

	public static <T extends TransformationType> void register(T transformation, String name) {
		Registry.register(SoulUnderSculk.TRANSFORMATIONS, SoulUnderSculk.id(name), transformation);
	}
}
