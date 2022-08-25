package phanastrae.soul_under_sculk.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.transformation.SculkmateTransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
	public void SoulUnderSculk_getJumpVelocity(CallbackInfoReturnable cir) {
		LivingEntity l = (LivingEntity)(Object)this;
		if(!(l instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) l).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;
		if(transHandler.getTransformationData() instanceof SculkmateTransformationData) {
			float amount = ((LivingEntity)(Object)this).isSneaking() ? 1.7F : 0.9F;
			cir.setReturnValue((float)cir.getReturnValue() * amount / 0.42f);
		}
	}
}
