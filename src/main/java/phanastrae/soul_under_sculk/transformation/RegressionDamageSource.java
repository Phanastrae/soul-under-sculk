package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;

public class RegressionDamageSource extends DamageSource {
	public RegressionDamageSource() {
		super("soul_under_sculk.regression");
		this.setBypassesArmor();
		this.setUsesMagic();
	}

	@Override
	public Text getDeathMessage(LivingEntity entity) {
		String string = "death." + this.name;
		return Text.translatable(string, entity.getDisplayName());
	}

	public static RegressionDamageSource getRegressionDamageSource() {
		return new RegressionDamageSource();
	}
}
