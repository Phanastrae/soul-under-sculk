package phanastrae.soul_under_sculk.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.SoulUnderSculkClient;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Environment(EnvType.CLIENT)
public class SculkmateFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private final SculkmateEntityModel<AbstractClientPlayerEntity> model;

	public SculkmateFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext, EntityModelLoader entityModelLoader) {
		super(featureRendererContext);
		ModelPart mp = entityModelLoader.getModelPart(SoulUnderSculkClient.MODEL_SCULKMATE_LAYER);
		this.model = mp == null ? null : new SculkmateEntityModel<>(mp, false);
	}

	public static final Identifier TEXTURE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate.png");
	public static final int TEXTURE_WIDTH = 128;
	public static final int TEXTURE_HEIGHT = 128;

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(!(livingEntity instanceof TransformableEntity && livingEntity instanceof PlayerEntity)) return;
		TransformationHandler transformationHandler = ((TransformableEntity)livingEntity).getTransHandler();
		if(!transformationHandler.isTransformed()) return;

		Identifier identifier = TEXTURE;
		this.getContextModel().copyStateTo(this.model);
		this.model.setAngles(livingEntity, limbAngle, limbDistance, tickDelta, headYaw, headPitch);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier));
		this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
