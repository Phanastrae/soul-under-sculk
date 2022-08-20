package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class EggTransformation extends TransformationType {
	@Override
	public Identifier getIconId() {
		return SoulUnderSculk.id("textures/gui/soul_under_sculk/transformation_icon/egg.png");
	}

	@Override
	public boolean shouldRenderIcon() {
		return true;
	}

	@Override
	public void handleIsInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable cir) {
		//none
	}

	@Override
	public void onTransform(TransformationHandler transHandler) {
		//none
	}
}
