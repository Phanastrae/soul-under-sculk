package phanastrae.soul_under_sculk.effect;

import net.minecraft.client.particle.SoulParticle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import phanastrae.soul_under_sculk.transformation.RegressionDamageSource;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.transformation.TransformationType;

public class RegressionEffect extends StatusEffect {
	protected RegressionEffect(StatusEffectType statusEffectType, int i) {
		super(statusEffectType, i);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if(!entity.world.isClient && entity.world.getTime() % 4 == 0) {
			((ServerWorld)entity.world).spawnParticles(ParticleTypes.SOUL, entity.getX(), entity.getY() + entity.getHeight() / 2f, entity.getZ(), 5, entity.getWidth() / 2f, entity.getHeight() / 2f, entity.getWidth() / 2f, 0.05F);
			((ServerWorld)entity.world).spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + entity.getHeight() / 2f, entity.getZ(), 5, entity.getWidth() / 2f, entity.getHeight() / 2f, entity.getWidth() / 2f, 0.1F);
		}
		TransformationHandler handler = TransformationHandler.getFromEntity(entity);
		TransformationType type = TransformationHandler.getTypeFromEntity(entity);
		boolean isCreativePlayer = (entity instanceof PlayerEntity && ((PlayerEntity)entity).isCreative());
		if((handler == null || type == null) && !isCreativePlayer) {
			entity.damage(RegressionDamageSource.getRegressionDamageSource(), 3F);
		} else {
			float damage = 4;
			if(entity.getHealth() - damage <= 0.5F || isCreativePlayer) {
				damage = Math.max(entity.getHealth() - 0.5F, 0);
				handler.setTransformation(null);
				entity.removeStatusEffect(this);
				if(!entity.world.isClient) {
					((ServerWorld)entity.world).spawnParticles(ParticleTypes.SCULK_SOUL, entity.getX(), entity.getY() + entity.getHeight() / 2f, entity.getZ(), 120, entity.getWidth() / 2f, entity.getHeight() / 2f, entity.getWidth() / 2f, 0.5F);
					((ServerWorld)entity.world).playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1, 0.5F);
					((ServerWorld)entity.world).playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.PLAYERS, 1, 1.5F);
				}
			}
			entity.damage(RegressionDamageSource.getRegressionDamageSource(), damage);
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
