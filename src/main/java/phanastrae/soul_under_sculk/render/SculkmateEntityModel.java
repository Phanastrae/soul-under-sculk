package phanastrae.soul_under_sculk.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class SculkmateEntityModel<T extends LivingEntity> extends EntityModel<T> {
	public final ModelPart head;

	public SculkmateEntityModel(ModelPart modelPart, boolean bl) {
		this.head = modelPart.getChild("head");
	}

	@Override
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
	}

	public static ModelData getTexturedModelData(Dilation dilation, boolean slim) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(
				"head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, 0.0F + 0.0F, 0.0F)
		);

		return modelData;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.getHeadParts().forEach(headPart -> headPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
		this.getBodyParts().forEach(bodyPart -> bodyPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
	}

	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.<ModelPart>of(this.head);
	}

	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.<ModelPart>of();
	}
}
