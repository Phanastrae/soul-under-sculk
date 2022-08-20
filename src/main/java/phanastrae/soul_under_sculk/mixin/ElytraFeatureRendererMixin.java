package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phanastrae.soul_under_sculk.render.*;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin <T extends LivingEntity, M extends EntityModel<T>> {

	@Shadow
	private ElytraEntityModel<T> elytra;

	@Inject(method = "render", at = @At("HEAD"))
	public void SoulUnderSculk_renderHead(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if(!(livingEntity instanceof PlayerEntity && livingEntity instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity)livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.shouldClientReloadModel()) return;

		ModelPart leftWing = ((ElytraEntityModelExtension)elytra).SoulUnderSculk_getLeftWing();
		ModelPart rightWing = ((ElytraEntityModelExtension)elytra).SoulUnderSculk_getRightWing();
		leftWing.resetTransform();
		rightWing.resetTransform();
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ElytraEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER))
	public void SoulUnderSculk_render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if(!(livingEntity instanceof PlayerEntity && livingEntity instanceof TransformableEntity)) return;
		TransformationHandler transHandler = ((TransformableEntity)livingEntity).getTransHandler();
		if(transHandler == null) return;
		if(!transHandler.isTransformed()) return;

		PlayerEntityRenderer PER = (PlayerEntityRenderer)((FeatureRendererMixin)(Object)this).Soul_Under_Sculk_getContext();
		SculkmateFeatureRenderer SFR = ((PlayerEntityRendererExtension)PER).getSculkmateFeatureRenderer();
		SculkmateEntityModel sculkmateModel = SFR.model;

		SoulUnderSculk_setElytraAngles(elytra, sculkmateModel);
	}

	private static void SoulUnderSculk_setElytraAngles(ElytraEntityModel elytraModel, SculkmateEntityModel sculkmateModel) {
		//for some reason there's some odd offset on the elytra? it's good enough though so i don't really care.
		MatrixStack stack = new MatrixStack();
		ModelPart leftWing = ((ElytraEntityModelExtension)elytraModel).SoulUnderSculk_getLeftWing();
		ModelPart rightWing = ((ElytraEntityModelExtension)elytraModel).SoulUnderSculk_getRightWing();

		stack.push(); //rootfloor
		sculkmateModel.rootFloor.rotate(stack);

		stack.push(); //lowertorso
		sculkmateModel.lowerTorso.rotate(stack);

		stack.push(); //leftwing
		stack.push(); //adjustleftwing
		RenderHelper.doTranslation(stack, 4.5F, -10, 6F, 0, 0, 0);
		//RenderHelper.doScale(stack, 0.85F, 0.85F, 0.85F, 1, 1, 1);
		leftWing.scaleX = leftWing.scaleY = leftWing.scaleZ = 1;
		leftWing.pivotX = leftWing.pivotY = leftWing.pivotZ = 0;
		leftWing.rotate(stack);
		RenderHelper.writeStackToModelPartTransform(stack, leftWing);
		stack.pop(); //adjustleftwing
		stack.pop(); //leftwing

		stack.push(); //rightwing
		stack.push(); //adjustrighttwing
		RenderHelper.doTranslation(stack, -4.5F, -10, 6F, 0, 0, 0);
		//RenderHelper.doScale(stack, 0.85F, 0.85F, 0.85F, 1, 1, 1);
		rightWing.scaleX = rightWing.scaleY = rightWing.scaleZ = 1;
		rightWing.pivotX = rightWing.pivotY = rightWing.pivotZ = 0;
		rightWing.rotate(stack);
		RenderHelper.writeStackToModelPartTransform(stack, rightWing);
		stack.pop(); //adjustrightwing
		stack.pop(); //rightwing

		stack.pop(); //lowertorso
		stack.pop(); //rootfloor
	}
}
