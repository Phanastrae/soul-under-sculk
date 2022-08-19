package phanastrae.soul_under_sculk.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

	@Inject(method = "copyFrom", at = @At("HEAD"))
	public void SoulUnderSculk_copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		if(!(oldPlayer instanceof TransformableEntity)) return;
		((TransformableEntity) (ServerPlayerEntity)(Object)this).getTransHandler().loadFromOnDeath(((TransformableEntity) oldPlayer).getTransHandler(), alive);
	}
}
