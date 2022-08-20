package phanastrae.soul_under_sculk.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.text.Text;

public class VerumDamageSource extends EntityDamageSource {
	public VerumDamageSource(Entity entity) {
		super("soul_under_sculk.verum_siphon", entity);
		this.setBypassesArmor();
	}

	@Override
	public Text getDeathMessage(LivingEntity entity) {
		String string = "death.attack." + this.name;
		return entity.equals(this.source)
				? Text.translatable(string + ".self", entity.getDisplayName())
				: Text.translatable(string, entity.getDisplayName(), this.source.getDisplayName());
	}

	public static VerumDamageSource getVerumDamageSource(Entity attacker) {
		return new VerumDamageSource(attacker);
	}
}
