package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.render.PlayerEntityRendererExtension;
import phanastrae.soul_under_sculk.render.SculkmateFeatureRenderer;

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
}
