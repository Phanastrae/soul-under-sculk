package phanastrae.soul_under_sculk.advancement;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.LocationCriterion;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class ModAdvancements {

	public static LocationCriterion BECOME_SCULKMATE = new LocationCriterion(SoulUnderSculk.id("become_sculkmate"));

	public static void init() {
		Criteria.register(BECOME_SCULKMATE);
	}
}
