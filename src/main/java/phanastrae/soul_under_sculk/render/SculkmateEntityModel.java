package phanastrae.soul_under_sculk.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.transformation.SculkmateTransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;

public class SculkmateEntityModel<T extends LivingEntity> extends EntityModel<T> {
	public final ModelPart root;
	public final ModelPart rootFloor;
	public final ModelPart lowerTorso;
	public final ModelPart head;
	public final ModelPart leftLeg;
	public final ModelPart rightLeg;
	public final ModelPart frame;
	public final ModelPart frame2;
	public final ModelPart ears;
	public final ModelPart backpack;
	public final ModelPart backpackTop;
	public final ModelPart toothUpperLeftFront;
	public final ModelPart toothUpperRightFront;
	public final ModelPart toothUpperLeftBack;
	public final ModelPart toothUpperRightBack;
	public final ModelPart toothLowerLeftFront;
	public final ModelPart toothLowerRightFront;
	public final ModelPart toothLowerLeftBack;
	public final ModelPart toothLowerRightBack;

	public final ModelPart headEnderback;
	public final ModelPart innerHead;
	public final ModelPart innerHat;
	public float leaningPitch;

	public SculkmateEntityModel(ModelPart modelPart) {
		this.root = modelPart;
		this.rootFloor = modelPart.getChild("root_floor");
		this.lowerTorso = rootFloor.getChild("lower_torso");
		this.leftLeg = rootFloor.getChild("left_leg");
		this.rightLeg = rootFloor.getChild("right_leg");
		this.head = lowerTorso.getChild("head");
		this.frame = head.getChild("frame");
		this.frame2 = frame.getChild("frame2");
		this.ears = head.getChild("ears");
		this.backpack = lowerTorso.getChild("backpack");
		this.backpackTop = head.getChild("backpack_top");
		this.toothUpperLeftFront = head.getChild("tooth_left_front");
		this.toothUpperRightFront = head.getChild("tooth_right_front");
		this.toothUpperLeftBack = head.getChild("tooth_left_back");
		this.toothUpperRightBack = head.getChild("tooth_right_back");
		this.toothLowerLeftFront = lowerTorso.getChild("tooth_left_front");
		this.toothLowerRightFront = lowerTorso.getChild("tooth_right_front");
		this.toothLowerLeftBack = lowerTorso.getChild("tooth_left_back");
		this.toothLowerRightBack = lowerTorso.getChild("tooth_right_back");

		this.headEnderback = modelPart.getChild("head_enderback");
		this.innerHead = modelPart.getChild("inner_head");
		this.innerHat = modelPart.getChild("inner_hat");
	}

	@Override
	public void animateModel(T entity, float limbAngle, float limbDistance, float tickDelta) {
		for(ModelPart part : new ModelPart[]{this.root, this.rootFloor, this.leftLeg, this.rightLeg, this.lowerTorso}) {
			part.resetTransform();
		}

		this.leaningPitch = entity.getLeaningPitch(tickDelta);

		if(entity.handSwinging) {
			float time = entity.handSwingTicks / (entity.handSwingTicks / entity.handSwingProgress + tickDelta);
			time = Math.min(time, 1);
			float timeButSine = MathHelper.sin((time) * (float) Math.PI);
			this.head.pitch = -timeButSine * (float) Math.PI * 0.3333F;

			for(ModelPart tooth : this.getTeeth()) {
				tooth.visible = true;
				tooth.scaleY = Math.min(1, timeButSine * 3);
			}
		} else {
			this.head.pitch = 0;
			for (ModelPart tooth : this.getTeeth()) {
				tooth.visible = false;
			}
		}

		TransformationHandler transHandler = TransformationHandler.getFromEntity(entity);
		if(transHandler != null) {
			TransformationData transData = transHandler.getTransformationData();
			if(transData instanceof SculkmateTransformationData) {
				SculkmateTransformationData sculkmateTransData = (SculkmateTransformationData)transData;

				this.rootFloor.scaleY = 1 + sculkmateTransData.getDistortionFactorLerp(tickDelta);
				this.rootFloor.scaleX = this.rootFloor.scaleZ = 1 - sculkmateTransData.getDistortionFactorLerp(tickDelta);
			}
		}
	}

	@Override
	public void setAngles(T livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		animateLimbs(limbAngle, limbDistance);
		runningSway(limbAngle, limbDistance, headYaw, headPitch);
		passiveSway(animationProgress);
		eyeJiggle(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		adjustPosing(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.innerHead.scaleX = this.innerHead.scaleY = this.innerHead.scaleZ = 0.25F;
	}

	public void animateLimbs(float limbAngle, float limbDistance) {
		float limbFreq = 0.6662F;
		this.rightLeg.pitch = MathHelper.cos(limbAngle * limbFreq) * 0.3F * limbDistance;
		this.leftLeg.pitch = MathHelper.cos(limbAngle * limbFreq + (float) Math.PI) * 0.3F * limbDistance;
		this.rightLeg.pivotZ = MathHelper.sin(limbAngle * limbFreq) * limbDistance * 1.9F;
		this.leftLeg.pivotZ = MathHelper.sin(limbAngle * limbFreq + (float) Math.PI) * limbDistance * 1.9F;
		this.rightLeg.pivotY = -(float)Math.pow(Math.min(0, MathHelper.cos(limbAngle * limbFreq)),2) * limbDistance * 2.5F - 0.5F;
		this.leftLeg.pivotY = -(float)Math.pow(Math.min(0, MathHelper.cos(limbAngle * limbFreq + (float) Math.PI)),2) * limbDistance * 2.5F - 0.5F;
		this.lowerTorso.roll = - MathHelper.sin(limbAngle * limbFreq) * 0.1F * limbDistance;
	}

	public void runningSway(float limbAngle, float limbDistance, float headYaw, float headPitch) {
		float swayFreq = 0.6662F * 0.2F;

		this.lowerTorso.pivotY = (Math.min(this.rightLeg.pivotY, this.leftLeg.pivotY) + 0.5F) * 0.5F - 9.5F;
		this.lowerTorso.roll += MathHelper.cos(limbAngle * swayFreq) * 0.15F * limbDistance;
		this.lowerTorso.pitch = headPitch * (float) (Math.PI / 180.0) / 3 -(1.5F + MathHelper.cos(limbAngle * swayFreq * 2) ) * 0.225F * limbDistance;
		this.lowerTorso.yaw = headYaw * (float) (Math.PI / 180.0);
	}

	public void eyeJiggle(T livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float runningMultiplier = limbDistance * limbDistance * limbDistance * 2;
		this.innerHead.pitch = headPitch * (float) (Math.PI / 180.0) * 2/ 3 + MathHelper.cos(animationProgress * 0.13f) * 0.15f + MathHelper.cos(limbAngle * 1.5F * 0.23f) * runningMultiplier;
		this.innerHead.yaw = MathHelper.cos(animationProgress * 0.17f) * 0.1f + MathHelper.cos(limbAngle * 1.5F * 0.29f) * runningMultiplier;
		this.innerHead.roll = MathHelper.cos(animationProgress * 0.19f) * 0.1f + MathHelper.cos(limbAngle * 1.5F * 0.31f) * runningMultiplier;

		this.innerHead.pivotX = this.lowerTorso.roll * 4;
		this.innerHead.pivotY = (this.lowerTorso.pivotY + 9.5F) - 5.5F;
		this.innerHead.pivotZ = Math.abs(this.lowerTorso.pitch) * 2 - 4.5F;
	}

	public void passiveSway(float animationProgress) {
		float f = animationProgress * 0.01F;
		float g = MathHelper.cos(f * 2);
		float h = MathHelper.sin(f);
		this.lowerTorso.roll += 0.04F * g;
		this.lowerTorso.pitch += 0.04F * h;
	}

	public void adjustPosing(T livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		if (this.riding) {
			this.rightLeg.pitch = -1.4137167F;
			this.rightLeg.yaw = (float) (Math.PI / 10);
			this.rightLeg.roll = 0.07853982F;
			this.leftLeg.pitch = -1.4137167F;
			this.leftLeg.yaw = (float) (-Math.PI / 10);
			this.leftLeg.roll = -0.07853982F;

			this.rightLeg.pivotZ += -5;
			this.rightLeg.pivotY += -5;
			this.rightLeg.pivotX = -5;
			this.leftLeg.pivotZ += -5;
			this.leftLeg.pivotY += -5;
			this.leftLeg.pivotX = 5;
		}
		if (livingEntity.isSneaking()) {
			this.lowerTorso.pitch += 0.2F;
			this.lowerTorso.pivotY += 2F;

			this.rightLeg.pivotZ += 3;
			this.leftLeg.pivotZ += 3;
		}
	}

	protected float lerpAngle(float angleOne, float angleTwo, float magnitude) {
		float f = (magnitude - angleTwo) % (float) (Math.PI * 2);
		if (f < (float) -Math.PI) {
			f += (float) (Math.PI * 2);
		}

		if (f >= (float) Math.PI) {
			f -= (float) (Math.PI * 2);
		}

		return angleTwo + angleOne * f;
	}

	public static ModelData getTexturedModelData(Dilation dilation, boolean slim) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData rootFloor = modelPartData.addChild(
				"root_floor", ModelPartBuilder.create(), ModelTransform.pivot(0.0F,  1.501F * 16, 0.0F)
		);
		ModelPartData lowerTorso = rootFloor.addChild(
				"lower_torso", centeredCuboid(0, 0, 0, 14, 10, 12, 0, 23), ModelTransform.pivot(0,  -9.5F, 0)
		);
		ModelPartData head = lowerTorso.addChild(
				"head", centeredCuboid(0, -5.5F, -4.5F, 14, 11, 12, 0, 0), ModelTransform.pivot(0,  -5, 4.5F)
		);
		head.addChild(
				"tooth_left_front", centeredCuboid(0, 1.5F, 0, 2, 3, 2, 40, 0), ModelTransform.pivot(5F,  0, -8.5F)
		);
		head.addChild(
				"tooth_right_front", centeredCuboid(0, 1.5F, 0, 2, 3, 2, 40, 0).mirrored(), ModelTransform.pivot(-5F,  0, -8.5F)
		);
		head.addChild(
				"tooth_left_back", centeredCuboid(0, 1F, 0, 1, 2, 1, 48, 0), ModelTransform.pivot(5.5F,  0, -6F)
		);
		head.addChild(
				"tooth_right_back", centeredCuboid(0, 1F, 0, 1, 2, 1, 48, 0).mirrored(), ModelTransform.pivot(-5.5F,  0, -6F)
		);
		lowerTorso.addChild(
				"tooth_left_front", centeredCuboid(0, -1F, 0, 1, 2, 1, 40, 5), ModelTransform.pivot(5.5F,  -5, -4F)
		);
		lowerTorso.addChild(
				"tooth_right_front", centeredCuboid(0, -1F, 0, 1, 2, 1, 40, 5).mirrored(), ModelTransform.pivot(-5.5F,  -5, -4F)
		);
		lowerTorso.addChild(
				"tooth_left_back", centeredCuboid(0, -0.5F, 0, 1, 1, 1, 44, 5), ModelTransform.pivot(5.5F,  -5F, 0F)
		);
		lowerTorso.addChild(
				"tooth_right_back", centeredCuboid(0, -0.5F, 0, 1, 1, 1, 44, 5).mirrored(), ModelTransform.pivot(-5F,  -5.5F, 0F)
		);
		ModelPartData frame = head.addChild(
				"frame", centeredCuboid(0, 0, 0, 8, 9, 2, 0, 58), ModelTransform.pivot(0,  -5.5F, -10.5F)
		);
		frame.addChild(
				"frame2", centeredCuboid(0, 0, 0.05F, 12, 7, 2, 20, 58), ModelTransform.pivot(0,  0, 0)
		);
		head.addChild(
				"ears", centeredCuboid(0, 0, 0, 32, 16, 0, 64, 0), ModelTransform.pivot(0,  -8F, -4.5F)
		);
		rootFloor.addChild(
				"left_leg", centeredCuboid(0, -2.5F, 0, 6, 6, 7, 0, 45), ModelTransform.pivot(3.5F, -0.5F, 0)
		);
		rootFloor.addChild(
				"right_leg", centeredCuboid(0, -2.5F, 0, 6, 6, 7, 26, 45), ModelTransform.pivot(-3.5F, -0.5F, 0)
		);
		lowerTorso.addChild(
				"backpack", centeredCuboid(0, 0, 0, 12, 9, 4, 0, 81), ModelTransform.pivot(0,  -0.5F, 8)
		);

		head.addChild(
				"backpack_top", centeredCuboid(0, 0, 0, 11, 9, 3, 0, 69), ModelTransform.pivot(0,  -4.5F, 3)
		);

		modelPartData.addChild(
				"head_enderback", ModelPartBuilder.create()
						//.cuboid(-6.5F, -5F, -5.5F, 13, 10, 11)
						.cuboid(-6.5F, -5F, -5.5F, 13, 0, 11)
						.cuboid(-6.5F, 5F, -5.5F, 13, 0, 11)
						.cuboid(-6.5F, -5F, -5.5F, 0, 10, 11)
						.cuboid(6.5F, -5F, -5.5F, 0, 10, 11)
						.cuboid(-6.5F, -5F, 5.5F, 13, 10, 0)
				, ModelTransform.pivot(0,  -5.5F, -4.5F)
		);

		modelPartData.addChild( // xyz are 16 not 8 because player texture is a different size
				"inner_head", centeredCuboid(0, 0, 0, 16, 16, 16, 0, 0), ModelTransform.pivot(0,  -5.5F, -4.5F)
		);

		modelPartData.addChild( // xyz are 16 not 8 because player texture is a different size
				"inner_hat", ModelPartBuilder.create().uv(64, 0).cuboid(-8f, -8f, -8f, 16f, 16f, 16f, dilation.add(1)), ModelTransform.pivot(0,  -5.5F, -4.5F)
		);

		return modelData;
	}

	public static ModelPartBuilder centeredCuboid(float offsetX, float offsetY, float offsetZ, float scaleX, float scaleY, float scaleZ, int uvx, int uvy) {
		return ModelPartBuilder.create().uv(uvx, uvy).cuboid(offsetX - scaleX / 2, offsetY - scaleY / 2, offsetZ - scaleZ / 2, scaleX, scaleY, scaleZ);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.rootFloor.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public ModelPart getRoot() {
		return this.root;
	}

	public Iterable<ModelPart> getTeeth() {
		return ImmutableList.of(this.toothUpperLeftFront, this.toothUpperRightFront, this.toothUpperLeftBack, this.toothUpperRightBack, this.toothLowerLeftFront, this.toothLowerRightFront, this.toothLowerLeftBack, this.toothLowerRightBack);
	}

	public Iterable<ModelPart> getHasEmissive() {
		return ImmutableList.of(this.lowerTorso, this.head, this.ears, this.leftLeg, this.rightLeg, this.backpack, this.backpackTop);
	}

	public Iterable<ModelPart> getHasSculk() {
		return ImmutableList.of(this.lowerTorso, this.head, this.leftLeg, this.rightLeg);
	}

	public Iterable<ModelPart> getHasGlowSculk() {
		return ImmutableList.of(this.lowerTorso, this.head, this.ears, this.leftLeg, this.rightLeg, this.backpack, this.backpackTop);
	}

	public Iterable<ModelPart> getHasGlowSculkNoEar() {
		return ImmutableList.of(this.lowerTorso, this.head, this.leftLeg, this.rightLeg, this.backpack, this.backpackTop);
	}

	public Iterable<ModelPart> getHasBone() {
		return ImmutableList.of(this.lowerTorso, this.head, this.leftLeg, this.rightLeg, this.frame, this.frame2, this.toothUpperLeftFront, this.toothUpperRightFront, this.toothUpperLeftBack, this.toothUpperRightBack, this.toothLowerLeftFront, this.toothLowerRightFront, this.toothLowerLeftBack, this.toothLowerRightBack);
	}

	public Iterable<ModelPart> getHasObsidian() {
		return ImmutableList.of(this.backpack, this.backpackTop);
	}

	public Iterable<ModelPart> getHasGlowstone() {
		return ImmutableList.of(this.backpack, this.backpackTop);
	}

	public Iterable<ModelPart> getHasCrying() {
		return ImmutableList.of(this.backpack, this.backpackTop);
	}

	public Iterable<ModelPart> getEars() {
		return ImmutableList.of(this.ears);
	}
}
