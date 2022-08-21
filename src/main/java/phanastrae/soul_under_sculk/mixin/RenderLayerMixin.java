package phanastrae.soul_under_sculk.mixin;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import phanastrae.soul_under_sculk.render.ModRenderLayers;
import phanastrae.soul_under_sculk.render.RenderLayerExtension;
import phanastrae.soul_under_sculk.render.RenderPhaseExtension;

@Mixin(RenderLayer.class)
public class RenderLayerMixin implements RenderLayerExtension {
	private static RenderPhaseExtension renderPhase = (RenderPhaseExtension)(Object)new RenderPhase("", () -> {return;}, () -> {return;}) {
		@Override
		public void startDrawing() {
			super.startDrawing();
		}
	};

	private static final RenderLayer WHITE_GLINT = RenderLayerMixin.of(
			"entity_glint",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(renderPhase.getENTITY_GLINT_SHADER())
					.texture(new RenderPhase.Texture(ModRenderLayers.WHITE_GLINT, true, false))
					.writeMaskState(renderPhase.getCOLOR_MASK())
					.cull(renderPhase.getDISABLE_CULLING())
					.depthTest(renderPhase.getEQUAL_DEPTH_TEST())
					.transparency(renderPhase.getGLINT_TRANSPARENCY())
					.target(renderPhase.getITEM_TARGET())
					.texturing(renderPhase.getENTITY_GLINT_TEXTURING())
					.build(false)
	);

	private static final RenderLayer AMOGUS_PORTAL = of(
			"end_portal",
			VertexFormats.POSITION,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(renderPhase.getEND_PORTAL_SHADER())
					.texture(
							RenderPhase.Textures.create()
									.add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
									.add(ModRenderLayers.AMOGUS_PORTAL_TEXTURE, false, false)
									.build()
					)
					.build(false)
	);

	@Shadow
	private static RenderLayer.MultiPhase of(
			String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, RenderLayer.MultiPhaseParameters phaseData
	) {
		return null;
	}

	@Shadow
	private static RenderLayer.MultiPhase of(
			String name,
			VertexFormat vertexFormat,
			VertexFormat.DrawMode drawMode,
			int expectedBufferSize,
			boolean hasCrumbling,
			boolean translucent,
			RenderLayer.MultiPhaseParameters phases
	) {
		return null;
	}

	@Override
	public RenderLayer GET_WHITE_GLINT() {
		return WHITE_GLINT;
	}

	@Override
	public RenderLayer GET_AMOGUS_PORTAL() {
		return AMOGUS_PORTAL;
	}
}
