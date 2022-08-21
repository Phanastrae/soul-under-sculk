package phanastrae.soul_under_sculk;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import phanastrae.soul_under_sculk.block.ModBlockEntities;
import phanastrae.soul_under_sculk.block.ModBlocks;
import phanastrae.soul_under_sculk.item.ModItems;
import phanastrae.soul_under_sculk.item.VerumItem;
import phanastrae.soul_under_sculk.networking.ModPacketsClient;
import phanastrae.soul_under_sculk.render.CreativeVerumChargerBlockEntityRenderer;
import phanastrae.soul_under_sculk.render.SculkmateEntityModel;
import phanastrae.soul_under_sculk.render.SculkmateFeatureRenderer;
import phanastrae.soul_under_sculk.transformation.CompositeColorEntry;

public class SoulUnderSculkClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_SCULKMATE_LAYER = new EntityModelLayer(SoulUnderSculk.id("sculkmate"), "main");

    @Override
	public void onInitializeClient(ModContainer mod) {
		BlockEntityRendererRegistry.register(ModBlockEntities.CREATIVE_VERUM_CHARGER, CreativeVerumChargerBlockEntityRenderer::new);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), ModBlocks.CREATIVE_VERUM_CHARGER);

		ModPacketsClient.init();

		EntityModelLayerRegistry.registerModelLayer(MODEL_SCULKMATE_LAYER,
				() -> TexturedModelData.of(SculkmateEntityModel.getTexturedModelData(Dilation.NONE, true), SculkmateFeatureRenderer.TEXTURE_WIDTH, SculkmateFeatureRenderer.TEXTURE_HEIGHT));
	}

	static {
		ModelPredicateProviderRegistry.register(ModItems.VERUM, new Identifier("active"), (stack, world, entity, seed) -> VerumItem.getIsTransCharged(stack) ? 1.0F : 0.0F);
		ColorProviderRegistry.ITEM.register((stack, tintindex) -> {
			if(tintindex > 0) return -1;
			CompositeColorEntry cce = new CompositeColorEntry();
			if(stack.getNbt() == null) return 0x5FFFFF;
			cce.readNbt(stack.getNbt(), "Biomass");
			if(MinecraftClient.getInstance() == null) return 0x5FFFFF;
			if(MinecraftClient.getInstance().player == null) return 0x5FFFFF;
			return cce.getColorAtTime(MinecraftClient.getInstance().player.age);
		}, ModItems.BIOMASS);
	}
}
