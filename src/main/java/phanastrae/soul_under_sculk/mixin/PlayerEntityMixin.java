package phanastrae.soul_under_sculk.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
}
