package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import phanastrae.soul_under_sculk.render.ElytraEntityModelExtension;

@Mixin(ElytraEntityModel.class)
public class ElytraEntityModelMixin implements ElytraEntityModelExtension {
	@Shadow
	private ModelPart rightWing;
	@Shadow
	private ModelPart leftWing;

	@Override
	public ModelPart SoulUnderSculk_getRightWing() {
		return this.rightWing;
	}
	@Override
	public ModelPart SoulUnderSculk_getLeftWing() {
		return this.leftWing;
	}
}
