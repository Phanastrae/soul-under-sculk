package phanastrae.soul_under_sculk.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.networking.TransformationSyncHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.util.function.Consumer;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerMixin {

	@Shadow @Final
	Entity entity;
	@Shadow
	abstract void sendSyncPacket(Packet<?> packet);

	@Inject(method = "sendPackets", at = @At("TAIL"))
	public void SoulUnderSculk_sendPackets(Consumer<Packet<?>> sender, CallbackInfo ci) {
		if(entity instanceof TransformableEntity) {
			TransformationSyncHandler.syncDataOnStart((TransformableEntity)entity , sender);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void SoulUnderSculk_tick(CallbackInfo ci) {
		if(entity instanceof TransformableEntity) {
			TransformationSyncHandler.syncDataIfNeeded((TransformableEntity)entity, this::sendSyncPacket);
		}
	}

	@Inject(method = "syncEntityData", at = @At("HEAD"))
	public void SoulUnderSculk_syncEntityData(CallbackInfo ci) {
		if(entity instanceof TransformableEntity) {
			TransformationSyncHandler.syncDataIfNeeded((TransformableEntity)entity, this::sendSyncPacket);
		}
	}
}
