package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.render.PlayerEntityRendererExtension;
import phanastrae.soul_under_sculk.render.SculkmateEntityModel;
import phanastrae.soul_under_sculk.render.SculkmateFeatureRenderer;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.nio.FloatBuffer;

import static net.minecraft.client.render.entity.LivingEntityRenderer.renderFlipped;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

	@Shadow
	protected M model;

	protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(T entity) {
		return null;
	}

	@Inject(method = "render", at = @At("HEAD"))
	public void SoulUnderSculk_render_top(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if(!(livingEntity instanceof PlayerEntity && livingEntity instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(transHandler.shouldClientReloadModel());
		if(!(this.model instanceof PlayerEntityModel)) return;
		PlayerEntityModel model = (PlayerEntityModel)this.model;
		for(ModelPart part : new ModelPart[]{model.head, model.hat, model.body, model.jacket, model.leftArm, model.leftSleeve, model.rightArm, model.rightSleeve, model.leftLeg, model.leftPants, model.rightLeg, model.rightPants}) {
			part.resetTransform();
		}

	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;setAngles(Lnet/minecraft/entity/Entity;FFFFF)V", shift = At.Shift.AFTER))
	public void SoulUnderSculk_render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if(!(livingEntity instanceof PlayerEntity && livingEntity instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;
		if(!(this.model instanceof PlayerEntityModel)) return;
		PlayerEntityModel model = (PlayerEntityModel)this.model;
		this.adjustModel(model, livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	public void adjustModel(PlayerEntityModel model, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		SculkmateFeatureRenderer sculkmateFeatureRenderer = ((PlayerEntityRendererExtension)this).getSculkmateFeatureRenderer();
		SculkmateEntityModel sculkmateModel = sculkmateFeatureRenderer.model;
		float n = 0.0F;
		float o = 0.0F;
		float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
		float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
		float k = j - h;
		if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity livingEntity2) {
			h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
			k = j - h;
			float l = MathHelper.wrapDegrees(k);
			if (l < -85.0F) {
				l = -85.0F;
			}

			if (l >= 85.0F) {
				l = 85.0F;
			}

			h = j - l;
			if (l * l > 2500.0F) {
				h += l * 0.2F;
			}

			k = j - h;
		}
		float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
		if (renderFlipped(livingEntity)) {
			m *= -1.0F;
			k *= -1.0F;
		}
		if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
			n = MathHelper.lerp(g, livingEntity.lastLimbDistance, livingEntity.limbDistance);
			o = livingEntity.limbAngle - livingEntity.limbDistance * (1.0F - g);
			if (livingEntity.isBaby()) {
				o *= 3.0F;
			}

			if (n > 1.0F) {
				n = 1.0F;
			}
		}
		float l = (float)livingEntity.age + g;
		// these variables are copied from livingentityrenderer, so if another mod changes the calculations there these will be desynced.
		// TODO: try to directly use those variables to avoid potential model desync and the extra calculations
		sculkmateModel.animateModel(livingEntity, o, n, g);
		sculkmateModel.setAngles(livingEntity, o, n, l, k, m);

		MatrixStack stack = new MatrixStack();

		stack.push(); //rootfloor
		sculkmateModel.rootFloor.rotate(stack);

		stack.push(); //lowertorso
		sculkmateModel.lowerTorso.rotate(stack);
		stack.push(); //adjustbody
		doTranslation(stack, 0, 0, 0, 0, -6, 0);
		doScale(stack, 14, 10, 12, 8, 12, 4);
		writeStackToModelPartTransform(stack, model.body);
		stack.pop(); //adjustbody

		stack.push(); //head
		sculkmateModel.head.rotate(stack);
		stack.push(); //adjusthead
		doTranslation(stack, 0, -5.5F, -4.5F, 0, 4, 0);
		doScale(stack, 14, 11, 12, 8, 8, 8);
		writeStackToModelPartTransform(stack, model.head);
		stack.pop(); //adjusthead
		stack.pop(); //head

		stack.push(); //leftarm
		stack.push(); //adjustleftarm
		doTranslation(stack, 4.5F, -5.5F, 2F, 0, 0, 0);
		doScale(stack, 0.85F, 0.85F, 0.85F, 1, 1, 1);
		model.leftArm.scaleX = model.leftArm.scaleY = model.leftArm.scaleZ = 1;
		model.leftArm.rotate(stack);
		writeStackToModelPartTransform(stack, model.leftArm);
		stack.pop(); //adjustleftarm
		stack.pop(); //leftarm

		stack.push(); //rightarm
		stack.push(); //adjustrighttarm
		doTranslation(stack, -4.5F, -5.5F, 2F, 0, 0, 0);
		doScale(stack, 0.85F, 0.85F, 0.85F, 1, 1, 1);
		model.rightArm.scaleX = model.rightArm.scaleY = model.rightArm.scaleZ = 1;
		model.rightArm.rotate(stack);
		writeStackToModelPartTransform(stack, model.rightArm);
		stack.pop(); //adjustrightarm
		stack.pop(); //rightarm

		stack.pop(); //lowertorso

		stack.push(); //leftleg
		sculkmateModel.leftLeg.rotate(stack);
		stack.push(); //adjustleftleg
		doTranslation(stack, 0, -2.5F, 0, 0, -3, 0);
		doScale(stack, 6, 6, 7, 4, 12, 4);
		writeStackToModelPartTransform(stack, model.leftLeg);
		stack.pop(); //adjustleftleg
		stack.pop(); //leftleg

		stack.push(); //rightleg
		sculkmateModel.rightLeg.rotate(stack);
		stack.push(); //adjustrightleg
		doTranslation(stack, 0, -2.5F, 0, 0, -3, 0);
		doScale(stack, 6, 6, 7, 4, 12, 4);
		writeStackToModelPartTransform(stack, model.rightLeg);
		stack.pop(); //adjustrightleg
		stack.pop(); //rightleg

		stack.pop(); //rootfloor

		model.leftPants.copyTransform(model.leftLeg);
		model.rightPants.copyTransform(model.rightLeg);
		model.leftSleeve.copyTransform(model.leftArm);
		model.rightSleeve.copyTransform(model.rightArm);
		model.hat.copyTransform(model.head);
		model.jacket.copyTransform(model.body);
	}

	public void doTranslation(MatrixStack stack, float targetOffsetX, float targetOffsetY, float targetOffsetZ, float additionalOffsetX, float additionalOffsetY, float additionalOffsetZ) {
		stack.translate((additionalOffsetX + targetOffsetX) / 16F, (additionalOffsetY + targetOffsetY) / 16F, (additionalOffsetZ + targetOffsetZ) / 16F);
	}

	public void doScale(MatrixStack stack, float targetScaleX, float targetScaleY, float targetScaleZ, float originalScaleX, float originalScaleY, float originalScaleZ) {
		stack.scale(targetScaleX / originalScaleX, targetScaleY / originalScaleY, targetScaleZ / originalScaleZ);
	}
	public void writeStackToModelPartTransform(MatrixStack stack, ModelPart part) {
		// i hate maths i hate maths i hate maths i hate maths i hate maths i hate maths
		// (i love maths) (this took too long)
		Matrix4f mat = stack.peek().getPosition();
		FloatBuffer buf = FloatBuffer.allocate(16);
		mat.write(buf, true);

		part.pivotX = buf.get(3) * 16;
		part.pivotY = buf.get(7) * 16;
		part.pivotZ = buf.get(11) * 16;
		double sx = Math.sqrt(buf.get(0) * buf.get(0) + buf.get(4) * buf.get(4) + buf.get(8) * buf.get(8));
		double sy = Math.sqrt(buf.get(1) * buf.get(1) + buf.get(5) * buf.get(5) + buf.get(9) * buf.get(9));
		double sz = Math.sqrt(buf.get(2) * buf.get(2) + buf.get(6) * buf.get(6) + buf.get(10) * buf.get(10));
		part.scaleX = (float)sx;
		part.scaleY = (float)sy;
		part.scaleZ = (float)sz;
		if(!(sx < 1E-6 || sy < 1E-6 || sz < 1E-6)) {
			float c = buf.get(8) / (float)sx;
			part.yaw = (float)Math.asin(-c);
			if(Math.abs(c) > 0.9999F) {
				//not 100% sure about this maths, haven't actually tested it lol
				double u = Math.atan2(4 * buf.get(6) / (float)sz - buf.get(1) / (float)sy, buf.get(5) / (float)sy - 2 * buf.get(2) / (float)sz);
				double v = Math.atan2(2*buf.get(1) / (float)sy, buf.get(5) / (float)sy);
				part.pitch = (float) (c * (u+v) / 2);
				part.roll = (float) (c * (u-v) / 2);
			} else {
				part.pitch = (float) Math.atan2(buf.get(9) / (float)sy, buf.get(10) / (float)sz);
				part.roll = (float) Math.atan2(buf.get(4) / (float)sx, buf.get(0) / (float)sx);
			}
		} else {
			// i'm just going to hope this never happens. don't use this system with any 0ish scale transforms please.
			part.pitch = part.yaw = part.roll = 0;
		}
	}
}
