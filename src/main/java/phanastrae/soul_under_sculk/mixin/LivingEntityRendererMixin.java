package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.SoulUnderSculkClient;
import phanastrae.soul_under_sculk.render.SculkmateEntityModel;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin <T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@Shadow
	protected M model;

	@Inject(method = "render", at = @At("HEAD"))
	protected void SoulUnderSculk_render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if(!(livingEntity instanceof TransformableEntity)) return;
		if(!(livingEntity instanceof PlayerEntity)) return;
		TransformationHandler transformationHandler = ((TransformableEntity)livingEntity).getTransHandler();
		if(transformationHandler == null) return;
		if(transformationHandler.shouldClientReloadModel()) {
			transformationHandler.setShouldClientReloadModel(false);
			if (transformationHandler.isTransformed()) {
				SculkmateEntityModel model = new SculkmateEntityModel<PlayerEntity>(EntityModels.getModels().get(SoulUnderSculkClient.MODEL_SCULKMATE_LAYER).createModel(), false);
				this.model = ((M) model);
			} else {
				PlayerEntityModel model = new PlayerEntityModel(EntityModels.getModels().get(EntityModelLayers.PLAYER).createModel(), false);
				this.model = ((M) model);
			}
		}
	}

	@ModifyVariable(method = "getRenderLayer", at = @At("STORE"))
	protected Identifier SoulUnderSculk_getRenderLayer(Identifier identifier, T entity, boolean showBody, boolean translucent, boolean showOutline) {
		if(!(entity instanceof TransformableEntity)) return identifier;
		if(!(entity instanceof PlayerEntity)) return identifier;
		TransformationHandler transHandler = ((TransformableEntity) entity).getTransHandler();
		if(transHandler == null) return identifier;
		if(!transHandler.isTransformed()) return identifier;
		return SoulUnderSculk.id("textures/entity/sculkmate/sculkmate.png");
	}

	/*
	//TODO: is redirect bad?
	@Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
	protected void SoulUnderSculk_render_color(M model, MatrixStack matrixStack, VertexConsumer vertexConsumer, int i, int p, float r, float g, float b, float a) {
		model.render(matrixStack, vertexConsumer, i, p, r, g, b, a);
	}

	 */
}
