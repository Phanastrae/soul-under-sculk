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
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.random.RandomGenerator;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Environment(EnvType.CLIENT)
public class SculkmateFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public SculkmateFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	private static final Identifier TEXTURE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate.png");

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		/*
		if(!(livingEntity instanceof TransformableEntity)) return;
		if(!((TransformableEntity)livingEntity).getTransHandler().isTransformed()) return;
		RandomGenerator randomGenerator = RandomGenerator.createLegacy((long) livingEntity.getId());
		for (int n = 0; n < 420; ++n) {
			matrixStack.push();
			ModelPart modelPart = this.getContextModel().getRandomPart(randomGenerator);
			ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(randomGenerator);
			modelPart.rotate(matrixStack);
			float o = randomGenerator.nextFloat();
			float p = randomGenerator.nextFloat();
			float q = randomGenerator.nextFloat();
			float r = MathHelper.lerp(o, cuboid.minX, cuboid.maxX) / 16.0F;
			float s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0F;
			float t = MathHelper.lerp(q, cuboid.minZ, cuboid.maxZ) / 16.0F;
			matrixStack.translate((double) r, (double) s, (double) t);
			o = -1.0F * (o * 2.0F - 1.0F);
			p = -1.0F * (p * 2.0F - 1.0F);
			q = -1.0F * (q * 2.0F - 1.0F);
			this.renderObject(matrixStack, vertexConsumerProvider, light, livingEntity, o, p, q, tickDelta);
			matrixStack.pop();
		}
		*/
	}

	protected void renderObject(
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			int light,
			Entity entity,
			float directionX,
			float directionY,
			float directionZ,
			float tickDelta
	) {
		float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
		float g = (float)(Math.atan2((double)directionX, (double)directionZ) * 180.0F / (float)Math.PI);
		float h = (float)(Math.atan2((double)directionY, (double)f) * 180.0F / (float)Math.PI);
		matrices.translate(0.0, 0.0, 0.0);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(g - 90.0F));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(h));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
		matrices.scale(0.03125F, 0.03125F, 0.03125F);
		matrices.translate(2.5, 0.0, 0.0);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));

		for(int n = 0; n < 4; ++n) {
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
			MatrixStack.Entry entry = matrices.peek();
			Matrix4f matrix4f = entry.getPosition();
			Matrix3f matrix3f = entry.getNormal();
			produceVertex(vertexConsumer, matrix4f, matrix3f, -4.5F, -1, 0.0F, 0.0F, light);
			produceVertex(vertexConsumer, matrix4f, matrix3f, 4.5F, -1, 0.125F, 0.0F, light);
			produceVertex(vertexConsumer, matrix4f, matrix3f, 4.5F, 1, 0.125F, 0.0625F, light);
			produceVertex(vertexConsumer, matrix4f, matrix3f, -4.5F, 1, 0.0F, 0.0625F, light);
		}

	}

	private static void produceVertex(
			VertexConsumer vertexConsumer, Matrix4f vertexTransform, Matrix3f normalTransform, float x, int y, float u, float v, int light
	) {
		vertexConsumer.vertex(vertexTransform, x, (float)y, 0.0F)
				.color(255, 255, 255, 255)
				.uv(u, v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(normalTransform, 0.0F, 1.0F, 0.0F)
				.next();
	}
}
