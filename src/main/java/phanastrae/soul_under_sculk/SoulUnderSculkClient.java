package phanastrae.soul_under_sculk;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import phanastrae.soul_under_sculk.packets.ModPackets;
import phanastrae.soul_under_sculk.render.SculkmateEntityModel;

public class SoulUnderSculkClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_SCULKMATE_LAYER = new EntityModelLayer(SoulUnderSculk.id("sculkmate"), "main");

    @Override
	public void onInitializeClient(ModContainer mod) {
		ModPackets.init();
		EntityModelLayerRegistry.registerModelLayer(MODEL_SCULKMATE_LAYER,
				() -> TexturedModelData.of(SculkmateEntityModel.getTexturedModelData(Dilation.NONE, true), 64, 64));
	}


}
