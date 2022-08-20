package phanastrae.soul_under_sculk;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import phanastrae.soul_under_sculk.item.ModItems;
import phanastrae.soul_under_sculk.item.VerumItem;
import phanastrae.soul_under_sculk.packets.ModPackets;
import phanastrae.soul_under_sculk.render.SculkmateEntityModel;
import phanastrae.soul_under_sculk.render.SculkmateFeatureRenderer;

public class SoulUnderSculkClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_SCULKMATE_LAYER = new EntityModelLayer(SoulUnderSculk.id("sculkmate"), "main");

    @Override
	public void onInitializeClient(ModContainer mod) {
		ModPackets.init();


		EntityModelLayerRegistry.registerModelLayer(MODEL_SCULKMATE_LAYER,
				() -> TexturedModelData.of(SculkmateEntityModel.getTexturedModelData(Dilation.NONE, true), SculkmateFeatureRenderer.TEXTURE_WIDTH, SculkmateFeatureRenderer.TEXTURE_HEIGHT));
	}

	static {
		ModelPredicateProviderRegistry.register(ModItems.VERUM, new Identifier("active"), (stack, world, entity, seed) -> VerumItem.getIsTransCharged(stack) ? 1.0F : 0.0F);
	}
}
