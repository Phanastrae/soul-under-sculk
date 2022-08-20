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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.render.PlayerEntityRendererExtension;
import phanastrae.soul_under_sculk.render.RenderHelper;
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
	public void SoulUnderSculk_render_head(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
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

	@Inject(method = "render", at = @At("TAIL"))
	public void SoulUnderSculk_render_tail(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if(!(livingEntity instanceof PlayerEntity && livingEntity instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(transHandler.shouldClientReloadModel());
		transHandler.setShouldClientReloadModel(false);
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;setAngles(Lnet/minecraft/entity/Entity;FFFFF)V", shift = At.Shift.AFTER))
	public void SoulUnderSculk_render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci, float h, float j, float k, float m, float l, float n, float o) {
		if(!(livingEntity instanceof PlayerEntity && livingEntity instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity) livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;
		if(!(this.model instanceof PlayerEntityModel)) return;
		PlayerEntityModel model = (PlayerEntityModel)this.model;

		SculkmateFeatureRenderer sculkmateFeatureRenderer = ((PlayerEntityRendererExtension)this).getSculkmateFeatureRenderer();
		SculkmateEntityModel sculkmateModel = sculkmateFeatureRenderer.model;
		sculkmateModel.animateModel(livingEntity, o, n, g);
		sculkmateModel.setAngles(livingEntity, o, n, l, k, m);

		this.adjustModel(model, sculkmateModel);
	}

	public void adjustModel(PlayerEntityModel model, SculkmateEntityModel sculkmateModel) {
		MatrixStack stack = new MatrixStack();

		stack.push(); //rootfloor
		sculkmateModel.rootFloor.rotate(stack);

		stack.push(); //lowertorso
		sculkmateModel.lowerTorso.rotate(stack);
		stack.push(); //adjustbody
		RenderHelper.doTranslation(stack, 0, 0, 0, 0, -6, 0);
		RenderHelper.doScale(stack, 14, 10, 12, 8, 12, 4);
		RenderHelper.writeStackToModelPartTransform(stack, model.body);
		stack.pop(); //adjustbody

		stack.push(); //head
		sculkmateModel.head.rotate(stack);
		stack.push(); //adjusthead
		RenderHelper.doTranslation(stack, 0, -5.5F, -4.5F, 0, 4, 0);
		RenderHelper.doScale(stack, 14, 11, 12, 8, 8, 8);
		RenderHelper.writeStackToModelPartTransform(stack, model.head);
		stack.pop(); //adjusthead
		stack.pop(); //head

		stack.push(); //leftarm
		stack.push(); //adjustleftarm
		RenderHelper.doTranslation(stack, 4.5F, -5.5F, 2F, 0, 0, 0);
		RenderHelper.doScale(stack, 0.85F, 0.85F, 0.85F, 1, 1, 1);
		model.leftArm.scaleX = model.leftArm.scaleY = model.leftArm.scaleZ = 1;
		model.leftArm.rotate(stack);
		RenderHelper.writeStackToModelPartTransform(stack, model.leftArm);
		stack.pop(); //adjustleftarm
		stack.pop(); //leftarm

		stack.push(); //rightarm
		stack.push(); //adjustrighttarm
		RenderHelper.doTranslation(stack, -4.5F, -5.5F, 2F, 0, 0, 0);
		RenderHelper.doScale(stack, 0.85F, 0.85F, 0.85F, 1, 1, 1);
		model.rightArm.scaleX = model.rightArm.scaleY = model.rightArm.scaleZ = 1;
		model.rightArm.rotate(stack);
		RenderHelper.writeStackToModelPartTransform(stack, model.rightArm);
		stack.pop(); //adjustrightarm
		stack.pop(); //rightarm

		stack.pop(); //lowertorso

		stack.push(); //leftleg
		sculkmateModel.leftLeg.rotate(stack);
		stack.push(); //adjustleftleg
		RenderHelper.doTranslation(stack, 0, -2.5F, 0, 0, -3, 0);
		RenderHelper.doScale(stack, 6, 6, 7, 4, 12, 4);
		RenderHelper.writeStackToModelPartTransform(stack, model.leftLeg);
		stack.pop(); //adjustleftleg
		stack.pop(); //leftleg

		stack.push(); //rightleg
		sculkmateModel.rightLeg.rotate(stack);
		stack.push(); //adjustrightleg
		RenderHelper.doTranslation(stack, 0, -2.5F, 0, 0, -3, 0);
		RenderHelper.doScale(stack, 6, 6, 7, 4, 12, 4);
		RenderHelper.writeStackToModelPartTransform(stack, model.rightLeg);
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
}
