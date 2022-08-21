package phanastrae.soul_under_sculk.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import phanastrae.soul_under_sculk.block.CreativeVerumChargerBlock;
import phanastrae.soul_under_sculk.block.CreativeVerumChargerBlockEntity;

public class CreativeVerumChargerBlockEntityRenderer implements BlockEntityRenderer<CreativeVerumChargerBlockEntity> {
	public CreativeVerumChargerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(CreativeVerumChargerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Matrix4f matrix4f = matrices.peek().getPosition();
		this.renderSides(entity, matrix4f, vertexConsumers.getBuffer(this.getLayer()));
	}

	private void renderSides(CreativeVerumChargerBlockEntity entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
		float f1 = 1 / 32F;
		float f2 = 1 - f1;
		this.renderSide(entity, matrix, vertexConsumer, f1, f2, f1, f2, f2, f2, f2, f2, Direction.SOUTH);
		this.renderSide(entity, matrix, vertexConsumer, f1, f2, f2, f1, f1, f1, f1, f1, Direction.NORTH);
		this.renderSide(entity, matrix, vertexConsumer, f2, f2, f2, f1, f1, f2, f2, f1, Direction.EAST);
		this.renderSide(entity, matrix, vertexConsumer, f1, f1, f1, f2, f1, f2, f2, f1, Direction.WEST);
		this.renderSide(entity, matrix, vertexConsumer, f1, f2, f1, f1, f1, f1, f2, f2, Direction.DOWN);
		this.renderSide(entity, matrix, vertexConsumer, f1, f2, f2, f2, f2, f2, f1, f1, Direction.UP);
	}

	private void renderSide(
			CreativeVerumChargerBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side
	) {
		if(CreativeVerumChargerBlock.shouldDrawSide(entity.getCachedState(), entity.getWorld(), entity.getPos(), side, entity.getPos().add(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ()))) {
			vertices.vertex(model, x1, y1, z1).next();
			vertices.vertex(model, x2, y1, z2).next();
			vertices.vertex(model, x2, y2, z3).next();
			vertices.vertex(model, x1, y2, z4).next();
		}
	}

	protected RenderLayer getLayer() {
		return ModRenderLayers.get_AMOGUS_PORTAL();
	}
}
