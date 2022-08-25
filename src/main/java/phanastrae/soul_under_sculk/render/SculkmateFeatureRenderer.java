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
import phanastrae.soul_under_sculk.transformation.CompositeColorEntry;
import phanastrae.soul_under_sculk.transformation.SculkmateTransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.awt.*;
import java.util.List;
import java.util.Locale;

@Environment(EnvType.CLIENT)
public class SculkmateFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public final SculkmateEntityModel<AbstractClientPlayerEntity> model;

	public SculkmateFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext, EntityModelLoader entityModelLoader) {
		super(featureRendererContext);
		ModelPart mp = entityModelLoader.getModelPart(SoulUnderSculkClient.MODEL_SCULKMATE_LAYER);
		this.model = mp == null ? null : new SculkmateEntityModel<>(mp);
	}

	public static final int TEXTURE_WIDTH = 128;
	public static final int TEXTURE_HEIGHT = 128;
	public static final Identifier WHITE = SoulUnderSculk.id("textures/entity/sculkmate/white.png");
	//public static final Identifier SCULKMATE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate.png");
	//public static final Identifier SCULKMATE_EMISSIVE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_emissive.png");

	public static final Identifier SCULKMATE_SCULK = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_sculk.png");
	public static final Identifier SCULKMATE_GLOWSCULK = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_glowsculk.png");
	public static final Identifier SCULKMATE_BONE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_bone.png");
	public static final Identifier SCULKMATE_OBSIDIAN = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_obsidian.png");
	public static final Identifier SCULKMATE_GLOWSTONE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_glowstone.png");
	public static final Identifier SCULKMATE_CRYING = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_crying.png");
	public static final Identifier SCULKMATE_SCULK_WHITE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_sculk_white.png");
	public static final Identifier SCULKMATE_GLOWSCULK_WHITE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_glowsculk_white.png");
	public static final Identifier SCULKMATE_BONE_WHITE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_bone_white.png");
	public static final Identifier SCULKMATE_OBSIDIAN_WHITE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_obsidian_white.png");
	public static final Identifier SCULKMATE_GLOWSTONE_WHITE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_glowstone_white.png");
	public static final Identifier SCULKMATE_CRYING_WHITE = SoulUnderSculk.id("textures/entity/sculkmate/sculkmate_crying_white.png");

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(livingEntity.isInvisible()) return;
		if(!(livingEntity instanceof TransformableEntity && livingEntity instanceof PlayerEntity)) return;
		TransformationHandler transformationHandler = ((TransformableEntity)livingEntity).getTransHandler();
		if(transformationHandler == null) return;
		if(!transformationHandler.isTransformed()) return;

		this.getContextModel().copyStateTo(this.model);
		//don't need to animatemodel or setangles because we do that elsewhere in LivingEntityRendererMixin
		int overlay = LivingEntityRenderer.getOverlay(livingEntity, 0.0F);

		float[] eyeColor = new float[]{1F, 0.3F, 0.8F};
		float[] sculkColor = null;
		float[] glowSculkColor = null;
		float[] boneColor = null;
		float[] obsidianColor = null;
		float[] glowstoneColor = null;
		float[] cryingColor = null;
		Identifier earTexture = SCULKMATE_GLOWSCULK;
		Identifier earTextureWhite = SCULKMATE_GLOWSCULK_WHITE;
		TransformationData transData = transformationHandler.getTransformationData();
		if(transData instanceof SculkmateTransformationData) {
			SculkmateTransformationData sculkmateTransData = (SculkmateTransformationData)transData;
			eyeColor = tryGetColor(sculkmateTransData.getEyeColor(), animationProgress, tickDelta, limbAngle, eyeColor);
			sculkColor = tryGetColor(sculkmateTransData.getSculkColor(), animationProgress, tickDelta, limbAngle, sculkColor);
			glowSculkColor = tryGetColor(sculkmateTransData.getGlowSculkColor(), animationProgress, tickDelta, limbAngle, glowSculkColor);
			boneColor = tryGetColor(sculkmateTransData.getBoneColor(), animationProgress, tickDelta, limbAngle, boneColor);
			obsidianColor = tryGetColor(sculkmateTransData.getObsidianColor(), animationProgress, tickDelta, limbAngle, obsidianColor);
			glowstoneColor = tryGetColor(sculkmateTransData.getGlowstoneColor(), animationProgress, tickDelta, limbAngle, glowstoneColor);
			cryingColor = tryGetColor(sculkmateTransData.getCryingColor(), animationProgress, tickDelta, limbAngle, cryingColor);

			String ear = sculkmateTransData.getEarType();
			if(ear != null && !ear.equals("")) {
				try {
					earTexture = SoulUnderSculk.id("textures/entity/sculkmate/ear_types/" + ear.toLowerCase() + ".png");
					earTextureWhite = SoulUnderSculk.id("textures/entity/sculkmate/ear_types/" + ear.toLowerCase() + "_white.png");
				} catch (Exception e) {
					earTexture = SCULKMATE_GLOWSCULK;
					earTextureWhite = SCULKMATE_GLOWSCULK_WHITE;
				}
			}
		}
		VertexConsumer vertexConsumer;

		this.setDrawAllParts(model);
		//vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE));
		//this.model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

		//this.setDrawOnlyParts(model, model.getHasEmissive());
		//vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_EMISSIVE));
		//this.model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 0.8F);

		this.setDrawOnlyParts(model, model.getHasSculk());
		if(sculkColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_SCULK));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_SCULK_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, sculkColor[0], sculkColor[1], sculkColor[2], 1F);
		}

		this.setDrawOnlyParts(model, model.getHasGlowSculkNoEar());
		if(glowSculkColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_GLOWSCULK));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_GLOWSCULK));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 0.7F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_GLOWSCULK_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, glowSculkColor[0], glowSculkColor[1], glowSculkColor[2], 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_GLOWSCULK_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, glowSculkColor[0], glowSculkColor[1], glowSculkColor[2], 0.7F);
		}

		this.setDrawOnlyParts(model, model.getEars());
		if(glowSculkColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(earTexture));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(earTexture));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 0.7F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(earTextureWhite));
			this.model.render(matrixStack, vertexConsumer, light, overlay, glowSculkColor[0], glowSculkColor[1], glowSculkColor[2], 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(earTextureWhite));
			this.model.render(matrixStack, vertexConsumer, light, overlay, glowSculkColor[0], glowSculkColor[1], glowSculkColor[2], 0.7F);
		}

		this.setDrawOnlyParts(model, model.getHasBone());
		if(boneColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_BONE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_BONE_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, boneColor[0], boneColor[1], boneColor[2], 1F);
		}

		this.setDrawOnlyParts(model, model.getHasObsidian());
		if(obsidianColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_OBSIDIAN));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_OBSIDIAN_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, obsidianColor[0], obsidianColor[1], obsidianColor[2], 1F);
		}

		this.setDrawOnlyParts(model, model.getHasGlowstone());
		if(glowstoneColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_GLOWSTONE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_GLOWSTONE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_GLOWSTONE_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, glowstoneColor[0], glowstoneColor[1], glowstoneColor[2], 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_GLOWSTONE_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, glowstoneColor[0], glowstoneColor[1], glowstoneColor[2], 1F);
		}

		this.setDrawOnlyParts(model, model.getHasCrying());
		if(cryingColor == null) {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_CRYING));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_CRYING));
			this.model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1F);
		} else {
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SCULKMATE_CRYING_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, cryingColor[0], cryingColor[1], cryingColor[2], 1F);
			vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(SCULKMATE_CRYING_WHITE));
			this.model.render(matrixStack, vertexConsumer, light, overlay, cryingColor[0], cryingColor[1], cryingColor[2], 1F);
		}


		this.setDrawAllParts(model);
		matrixStack.push();
		this.model.rootFloor.rotate(matrixStack);
		this.model.lowerTorso.rotate(matrixStack);
		this.model.head.rotate(matrixStack);

		vertexConsumer = vertexConsumerProvider.getBuffer(ModRenderLayers.get_AMOGUS_PORTAL());
		this.model.headEnderback.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1.0F);

		this.model.innerHat.copyTransform(this.model.innerHead);
		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(WHITE)); // ideally we'd just render without a texture here but i have no idea if minecraft has that and i don't really want to have to do it myself
		this.model.innerHead.render(matrixStack, vertexConsumer, light, overlay, eyeColor[0], eyeColor[1], eyeColor[2], 0.6F);
		vertexConsumer = vertexConsumerProvider.getBuffer(ModRenderLayers.get_WHITE_GLINT());
		this.model.innerHead.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, eyeColor[0], eyeColor[1], eyeColor[2], 1F);
		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentEmissive(livingEntity.getSkinTexture()));
		this.model.innerHead.render(matrixStack, vertexConsumer, light, overlay, eyeColor[0], eyeColor[1], eyeColor[2], 0.2F);
		this.model.innerHat.render(matrixStack, vertexConsumer, light, overlay, eyeColor[0], eyeColor[1], eyeColor[2], 0.15F);
		matrixStack.pop();
	}

	private float[] tryGetColor(CompositeColorEntry colorEntry, float animationProgress, float tickDelta, float limbAngle, float[] color) {
		if(colorEntry != null && colorEntry.getColorEntries().size() > 0) {
			float t = (animationProgress + tickDelta);
			if(colorEntry.getScaleWithSpeed()) {
				t += limbAngle * 2;
			}
			int c = colorEntry.getColorAtTime(t);
			color = new Color(c).getRGBColorComponents(color);
		}
		return color;
	}

	private void setDrawOnlyParts(SculkmateEntityModel model, Iterable<ModelPart> parts) {
		model.getRoot().traverse().forEach(modelPart -> modelPart.skipDraw = true);
		parts.forEach(modelPart -> modelPart.skipDraw = false);
	}

	private void setDrawAllParts(SculkmateEntityModel model) {
		model.getRoot().traverse().forEach(modelPart -> modelPart.skipDraw = false);
	}
}
