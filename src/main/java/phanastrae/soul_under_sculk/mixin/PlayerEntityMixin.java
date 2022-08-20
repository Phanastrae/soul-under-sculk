package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.item.VerumDamageSource;
import phanastrae.soul_under_sculk.item.VerumItem;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements TransformableEntity {

	public TransformationHandler transHandler;

	@Override
	public TransformationHandler getTransHandler() {
		return transHandler;
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void SoulUnderSculk_playerEntity(CallbackInfo ci) {
		this.transHandler = new TransformationHandler((PlayerEntity)(Object)this);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void SoulUnderSculk_writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		this.transHandler.writeNbt(nbt);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void SoulUnderSculk_readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		this.transHandler.readNbt(nbt);
	}

	@Inject(method = "getActiveEyeHeight", at = @At("RETURN"), cancellable = true)
	public void SoulUnderSculk_getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable cir) {
		TransformationHandler transHandler = this.getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		float eyeHeight = Math.min(1.2F, (float)cir.getReturnValue());
		cir.setReturnValue(eyeHeight);
	}

	@Inject(method = "getDimensions(Lnet/minecraft/entity/EntityPose;)Lnet/minecraft/entity/EntityDimensions;", at = @At("RETURN"), cancellable = true)
	public void SoulUnderSculk_getDimensions(EntityPose pose, CallbackInfoReturnable cir) {
		TransformationHandler transHandler = this.getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		EntityDimensions normalDims = (EntityDimensions)cir.getReturnValue();
		float width = Math.min(normalDims.width, 0.6F);
		float height = Math.min(normalDims.height, 1.45F);
		cir.setReturnValue(EntityDimensions.changing(width, height));
	}

	@Inject(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	public void SoulUnderSculk_attack(Entity entity, CallbackInfo ci) {
		if(!(entity instanceof PlayerEntity)) return;
		if(((PlayerEntity)(Object)this).getInventory().getMainHandStack().getItem() instanceof VerumItem) {
			VerumItem.yoinkXp((LivingEntity)(Object)this, (PlayerEntity)entity, ((PlayerEntity)(Object)this).getInventory().getMainHandStack(), 1);
		}
	}
}
