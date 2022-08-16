package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.util.List;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
	public PlayerEntityModelMixin(ModelPart modelPart, List<ModelPart> parts, ModelPart leftSleeve, ModelPart rightSleeve, ModelPart leftPants, ModelPart rightPants, ModelPart jacket, ModelPart cloak, ModelPart ear, boolean thinArms) {
		super(modelPart);
		this.parts = parts;
	}

	@Inject(method = "setAngles", at = @At("TAIL"))
	public void SoulUnderSculk_setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(!(livingEntity instanceof TransformableEntity && livingEntity instanceof PlayerEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		for(ModelPart part : this.parts) {
			part.visible = false;
		}
		//TODO: try to resize model parts to make elytra, arrows, etc. actually match?
	}

	@Shadow private final List<ModelPart> parts;
}
