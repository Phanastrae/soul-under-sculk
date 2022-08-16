package phanastrae.soul_under_sculk.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(method = "onPlayerConnect", at = @At("TAIL"))
	public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		((TransformableEntity)player).getTransHandler().setShouldSyncData(true);
	}
}
