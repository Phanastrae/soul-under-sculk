package phanastrae.soul_under_sculk.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
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
	public final SculkmateEntityModel<AbstractClientPlayerEntity> model;

	public SculkmateFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext, EntityModelLoader entityModelLoader) {
		super(featureRendererContext);
		ModelPart mp = entityModelLoader.getModelPart(SoulUnderSculkClient.MODEL_SCULKMATE_LAYER);
		this.model = mp == null ? null : new SculkmateEntityModel<>(mp);
	}

	public static final Identifier SCULKMATE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate.png");
	public static final Identifier SCULKMATE_EMISSIVE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_emissive.png");
	public static final Identifier WHITE = SoulUnderSculk.id("textures/entity/sculkmate/white.png");
	public static final int TEXTURE_WIDTH = 128;
	public static final int TEXTURE_HEIGHT = 128;

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(livingEntity.isInvisible()) return;
		if(!(livingEntity instanceof TransformableEntity && livingEntity instanceof PlayerEntity)) return;
		TransformationHandler transformationHandler = ((TransformableEntity)livingEntity).getTransHandler();
		if(!transformationHandler.isTransformed()) return;

		this.getContextModel().copyStateTo(this.model);
		//this.model.animateModel(livingEntity, limbAngle, limbDistance, tickDelta);
		//this.model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		int overlay = LivingEntityRenderer.getOverlay(livingEntity, 0.0F);

		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE));
		this.model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_EMISSIVE)); //TODO: consider only rendering parts that actually have emissive
		this.model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 0.8F);

		matrixStack.push();
		this.model.rootFloor.rotate(matrixStack);
		this.model.lowerTorso.rotate(matrixStack);
		this.model.head.rotate(matrixStack);

		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEndPortal()); // TODO: see if i can add a custom similar renderlayer
		this.model.headEnderback.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

		this.model.innerHead.scaleX = this.model.innerHead.scaleY = this.model.innerHead.scaleZ = 0.25F;
		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(WHITE)); // ideally we'd just render without a texture here but i have no idea if minecraft has that and i don't really want to have to do it myself
		this.model.innerHead.render(matrixStack, vertexConsumer, light, overlay, 1F, 0.4F, 0.8F, 0.6F);
		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityGlint());
		this.model.innerHead.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);
		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(livingEntity.getSkinTexture()));
		this.model.innerHead.render(matrixStack, vertexConsumer, light, overlay, 1F, 0.2F, 0.7F, 0.25F);
		matrixStack.pop();
	}
}
