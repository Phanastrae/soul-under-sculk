package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.render.PlayerEntityRendererExtension;
import phanastrae.soul_under_sculk.render.SculkmateFeatureRenderer;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin extends LivingEntityRenderer implements PlayerEntityRendererExtension {

	private SculkmateFeatureRenderer sculkmateFeatureRenderer;

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void SoulUnderSculk_init(EntityRendererFactory.Context context, boolean bl, CallbackInfo ci) {
		this.sculkmateFeatureRenderer = new SculkmateFeatureRenderer((FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) this, context.getModelLoader());
		this.addFeature(this.sculkmateFeatureRenderer);
	}

	//just here so i can extend LivingEntityRenderer
	@Shadow
	public Identifier getTexture(Entity entity) {
		return null;
	}
	public PlayerEntityRendererMixin(EntityRendererFactory.Context context, EntityModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Override
	public SculkmateFeatureRenderer getSculkmateFeatureRenderer() {
		return this.sculkmateFeatureRenderer;
	}

	@Inject(method = "getPositionOffset", at = @At("HEAD"), cancellable = true)
	public void SoulUnderSculk_getPositionOffset(AbstractClientPlayerEntity player, float f, CallbackInfoReturnable cir) {
		if(!(player instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity)player).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		cir.setReturnValue(new Vec3d(0, 0, 0));
	}
}
