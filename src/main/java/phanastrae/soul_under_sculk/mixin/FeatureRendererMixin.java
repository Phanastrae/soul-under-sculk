package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FeatureRenderer.class)
public class FeatureRendererMixin <T extends Entity, M extends EntityModel<T>> {
	@Shadow
	private FeatureRendererContext<T, M> context;

	public FeatureRendererContext<T, M> Soul_Under_Sculk_getContext() {
		return this.context;
	}
}
