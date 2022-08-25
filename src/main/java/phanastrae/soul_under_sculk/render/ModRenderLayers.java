package phanastrae.soul_under_sculk.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public final class ModRenderLayers extends RenderPhase {

	public static Identifier WHITE_GLINT = SoulUnderSculk.id("textures/misc/white_glint.png");
	public static Identifier AMOGUS_PORTAL_TEXTURE = SoulUnderSculk.id("textures/entity/amogus_portal.png");

	private static final RenderLayer LAYER_WHITE_GLINT = RenderLayer.of(
			"soul_under_sculk_entity_glint_white",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhase.ENTITY_GLINT_SHADER)
					.texture(new RenderPhase.Texture(ModRenderLayers.WHITE_GLINT, true, false))
					.writeMaskState(RenderPhase.COLOR_MASK)
					.cull(DISABLE_CULLING)
					.depthTest(RenderPhase.EQUAL_DEPTH_TEST)
					.transparency(RenderPhase.GLINT_TRANSPARENCY)
					.target(RenderPhase.ITEM_TARGET)
					.texturing(RenderPhase.ENTITY_GLINT_TEXTURING)
					.build(false)
	);

	private static final RenderLayer LAYER_AMOGUS_PORTAL = RenderLayer.of(
			"soul_under_sculk_amogus_portal",
			VertexFormats.POSITION,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(END_PORTAL_SHADER)
					.texture(
							RenderPhase.Textures.create()
									.add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
									.add(ModRenderLayers.AMOGUS_PORTAL_TEXTURE, false, false)
									.build()
					)
					.build(false)
	);

	public static RenderLayer get_WHITE_GLINT() {
		return LAYER_WHITE_GLINT;
	}

	public static RenderLayer get_AMOGUS_PORTAL() {
		return LAYER_AMOGUS_PORTAL;
	}

	private ModRenderLayers() {
		super(null, null, null);
	}
}
