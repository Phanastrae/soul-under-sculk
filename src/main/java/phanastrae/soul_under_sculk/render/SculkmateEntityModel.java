package phanastrae.soul_under_sculk.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;

public class SculkmateEntityModel<T extends LivingEntity> extends PlayerEntityModel<T> {
	public SculkmateEntityModel(ModelPart modelPart, boolean bl) {
		super(modelPart, bl);
	}

	@Override
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
		super.setAngles(livingEntity, f, g, h, i, j);
		this.head.scaleX = 2f;
		this.head.scaleY = 2f;
		this.head.scaleZ = 2f;
	}
}
