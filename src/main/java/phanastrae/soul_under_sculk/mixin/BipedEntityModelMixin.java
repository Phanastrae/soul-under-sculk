package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.item.ModItems;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin <T extends LivingEntity> {
	@Shadow
	public ModelPart rightArm;
	@Shadow
	public ModelPart leftArm;

	@Inject(method = "positionRightArm", at = @At("TAIL"))
	public void SoulUnderSculk_positionRightArm(T entity, CallbackInfo ci) {
		SoulUnderSculk_positionArm(rightArm, leftArm, entity, true);
	}

	@Inject(method = "positionLeftArm", at = @At("TAIL"))
	public void SoulUnderSculk_positionLeftArm(T entity, CallbackInfo ci) {
		SoulUnderSculk_positionArm(rightArm, leftArm, entity, false);
	}

	public void SoulUnderSculk_positionArm(ModelPart rightArm, ModelPart leftArm, T entity, boolean rightArmed) {
		if(!entity.isUsingItem()) return;
		if(entity.getActiveItem().getItem().equals(ModItems.VERUM)) {
			ModelPart usingArm = rightArmed ? rightArm : leftArm;
			float flip = rightArmed ? 1 : -1;
			float progress = entity.getItemUseTime() / (float)entity.getActiveItem().getMaxUseTime();
			float pitch = 0;
			float yaw = 0;
			float roll = 0;
			float f1 = 0.65f;
			float f2 = 0.9f;
			float subProgress;
			if(progress < f1) {
				subProgress = progress / f1;
				pitch = - 1.8F * subProgress;
				yaw = 0.8F * subProgress;
			} else if (progress < f2) {
				subProgress = (progress - f1) / (f2 - f1);
				pitch = -1.8F + 1.2F * subProgress;
				yaw = 0.8F - 1.5F * subProgress;
				roll = 0.3F * subProgress;
			} else {
				subProgress = (progress - f2) / (1 - f2);
				pitch = -0.6F + 0.025F * (float)Math.sin(subProgress * 11f);
				yaw = -0.7F + 0.025F *  (float)Math.sin(subProgress * 13f);
				roll = 0.3F + 0.025F *  (float)Math.sin(subProgress * 7f);
			}

			usingArm.pitch = pitch;
			usingArm.yaw = yaw * flip;
			usingArm.roll = roll * flip;
		}
	}
}
