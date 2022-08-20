package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.mob.warden.WardenEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.advancement.ModAdvancements;

public class SculkmateTransformation extends TransformationType {
	@Override
	public Identifier getIconId() {
		return SoulUnderSculk.id("textures/gui/soul_under_sculk/transformation_icon/sculkmate.png");
	}

	@Override
	public boolean shouldRenderIcon() {
		return true;
	}

	@Override
	public void handleIsInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable cir) {
		if(damageSource.isFromFalling()) {
			cir.setReturnValue(true);
			return;
		}
		if(damageSource.name.equals("sonic_boom")) {
			cir.setReturnValue(true);
			return;
		}
		if(damageSource instanceof EntityDamageSource) {
			Entity attacker = ((EntityDamageSource) damageSource).getAttacker();
			if(attacker instanceof WardenEntity) {
				cir.setReturnValue(true);
			}
		}
	}

	@Override
	public void onTransform(TransformationHandler transHandler) {
		if(!transHandler.player.world.isClient) {
			ModAdvancements.BECOME_SCULKMATE.trigger((ServerPlayerEntity)transHandler.player);
		}
	}
}
