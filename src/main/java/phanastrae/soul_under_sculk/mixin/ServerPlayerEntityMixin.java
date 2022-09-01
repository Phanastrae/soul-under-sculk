package phanastrae.soul_under_sculk.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

	@Inject(method = "copyFrom", at = @At("HEAD"))
	public void SoulUnderSculk_copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		TransformationHandler oldTransHandler = TransformationHandler.getFromEntity(oldPlayer);
		TransformationHandler newTransHandler = TransformationHandler.getFromEntity((ServerPlayerEntity)(Object)this);
		if(oldTransHandler == null || newTransHandler == null) return;
		newTransHandler.loadFromOnDeath(oldTransHandler, alive);
	}
}
