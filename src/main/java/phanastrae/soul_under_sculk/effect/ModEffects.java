package phanastrae.soul_under_sculk.effect;

import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class ModEffects {

	public static RegressionEffect regression = new RegressionEffect(StatusEffectType.HARMFUL, 0x3F3FFF);

	public static void init() {
		Registry.register(Registry.STATUS_EFFECT, SoulUnderSculk.id("regression"), regression);
	}
}
