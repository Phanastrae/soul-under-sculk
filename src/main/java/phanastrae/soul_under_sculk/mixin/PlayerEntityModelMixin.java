package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.render.PlayerEntityModelExtension;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> implements PlayerEntityModelExtension {
	@Shadow
	private ModelPart cloak;
	@Shadow
	private ModelPart ear;

	@Inject(method = "setAngles", at = @At("TAIL"))
	public void SoulUnderSculk_setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(!(livingEntity instanceof TransformableEntity && livingEntity instanceof PlayerEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		((PlayerEntityModel)(Object)this).setVisible(false);
	}

	@Override
	public ModelPart getEar() {
		return this.ear;
	}

	@Override
	public ModelPart getCloak() {
		return this.cloak;
	}
}
