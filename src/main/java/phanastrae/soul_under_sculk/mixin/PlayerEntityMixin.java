package phanastrae.soul_under_sculk.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.item.VerumItem;
import phanastrae.soul_under_sculk.transformation.CompositeColorEntry;
import phanastrae.soul_under_sculk.transformation.SculkmateTransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationData;
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
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		float eyeHeight = (float)cir.getReturnValue() * 1.2F / PlayerEntity.DEFAULT_EYE_HEIGHT;
		cir.setReturnValue(eyeHeight);
	}

	@Inject(method = "getDimensions(Lnet/minecraft/entity/EntityPose;)Lnet/minecraft/entity/EntityDimensions;", at = @At("RETURN"), cancellable = true)
	public void SoulUnderSculk_getDimensions(EntityPose pose, CallbackInfoReturnable cir) {
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		EntityDimensions normalDims = (EntityDimensions)cir.getReturnValue();
		float width = normalDims.width * 0.6F / PlayerEntity.DEFAULT_BOUNDING_BOX_WIDTH;
		float height = normalDims.height * 1.45F / PlayerEntity.DEFAULT_BOUNDING_BOX_HEIGHT;
		cir.setReturnValue(EntityDimensions.changing(width, height));
	}

	@Inject(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	public void SoulUnderSculk_attack(Entity entity, CallbackInfo ci) {
		if(!(entity instanceof PlayerEntity)) return;
		if(((PlayerEntity)(Object)this).getInventory().getMainHandStack().getItem() instanceof VerumItem) {
			VerumItem.yoinkXp((LivingEntity)(Object)this, (PlayerEntity)entity, ((PlayerEntity)(Object)this).getInventory().getMainHandStack(), 1);
		}
	}

	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	public void SoulUnderSculk_isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable cir) {
		if(transHandler == null) return;

		if(!transHandler.isTransformed()) return;
		transHandler.getTransformation().handleIsInvulnerableTo(damageSource, cir);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void SoulUnderSculk_tick(CallbackInfo ci){
		PlayerEntity player = (PlayerEntity)(Object)this;
		if(player == null) return;
		World world = player.getWorld();
		if(world == null) return;
		if(world.isClient) {
			TransformationHandler transformationHandler = TransformationHandler.getFromEntity(player);
			if(transformationHandler != null) {
				TransformationData transData = transHandler.getTransformationData();
				if(transData instanceof SculkmateTransformationData) {
					CompositeColorEntry cce = ((SculkmateTransformationData)transData).getParticleColor();
					if(cce != null) {
						if (cce.getColorEntries().size() > 0) {
							int color = cce.getColorAtTime(player.age);
							int rCurrent = (color & 0xFF0000) >> 16;
							int gCurrent = (color & 0xFF00) >> 8;
							int bCurrent = (color & 0xFF);
							Vec3d vel = player.getVelocity();
							int count = (int) Math.max(1, Math.min(20, 10 * vel.lengthSquared()));
							RandomGenerator random = player.getWorld().getRandom();
							for (int i = 0; i < count; i++) {
								float yaw = (float) (player.bodyYaw * Math.PI / 180);
								world.addParticle(new DustParticleEffect(new Vec3f(rCurrent / 255F, gCurrent / 255F, bCurrent / 255F), 0.5f), player.getX() + 0.3F * Math.cos(yaw) + player.getWidth() / 2 * (random.nextFloat() * 2 - 1), player.getY(), player.getZ() + 0.3F * Math.sin(yaw) + player.getWidth() / 2 * (random.nextFloat() * 2 - 1), (random.nextFloat() * 2 - 1) * 0.2f - vel.x / 4F, (random.nextFloat() * 2 - 1) * 0.2f - vel.y / 4F, (random.nextFloat() * 2 - 1) * 0.2f - vel.z / 4F);
								world.addParticle(new DustParticleEffect(new Vec3f(rCurrent / 255F, gCurrent / 255F, bCurrent / 255F), 0.5f), player.getX() - 0.3F * Math.cos(yaw) + player.getWidth() / 2 * (random.nextFloat() * 2 - 1), player.getY(), player.getZ() - 0.3F * Math.sin(yaw) + player.getWidth() / 2 * (random.nextFloat() * 2 - 1), (random.nextFloat() * 2 - 1) * 0.2f - vel.x / 4F, (random.nextFloat() * 2 - 1) * 0.2f - vel.y / 4F, (random.nextFloat() * 2 - 1) * 0.2f - vel.z / 4F);
							}
						}
					}

					((SculkmateTransformationData) transData).updateDistortion();
				}
			}
		}
	}

	@Inject(method = "jump", at = @At("HEAD"))
	public void SoulUnderSculk_jump(CallbackInfo ci){
		PlayerEntity player = (PlayerEntity)(Object)this;
		TransformationHandler transHandler = TransformationHandler.getFromEntity(player);
		if(transHandler == null) return;
		TransformationData transData = transHandler.getTransformationData();
		if(!(transData instanceof SculkmateTransformationData)) return;
		float amount = player.isSneaking() ? 1.5F : 1.0F;

		World world = player.getWorld();
		if(world == null) return;
		if(world.isClient) {
			((SculkmateTransformationData)transData).setDistortionFactorTarget(amount);
		} else {
			((SculkmateTransformationData)transData).sendDistortionPacket(player, amount);
		}
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"))
	public void SoulUnderSculk_handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable ci){
		PlayerEntity player = (PlayerEntity)(Object)this;
		TransformationHandler transHandler = TransformationHandler.getFromEntity(player);
		if(transHandler == null) return;
		TransformationData transData = transHandler.getTransformationData();
		if(!(transData instanceof SculkmateTransformationData)) return;
		float amount = -(Math.max(0, Math.min(1, player.fallDistance * 0.15F)) * 0.8F + 0.2F);

		World world = player.getWorld();
		if(world == null) return;
		if(world.isClient) {
			((SculkmateTransformationData)transData).setDistortionFactorTarget(amount);
		} else {
			((SculkmateTransformationData)transData).sendDistortionPacket(player, amount);
		}
	}
}
