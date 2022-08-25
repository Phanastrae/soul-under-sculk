package phanastrae.soul_under_sculk.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;

@Environment(EnvType.CLIENT)
public class ModRenderLayers extends RenderPhase {

	public static Identifier WHITE_GLINT = SoulUnderSculk.id("textures/misc/white_glint.png");
	public static Identifier AMOGUS_PORTAL_TEXTURE = SoulUnderSculk.id("textures/entity/amogus_portal.png");

	public static RenderLayer get_WHITE_GLINT() {
		return LAYER_WHITE_GLINT;
	}

	public static RenderLayer get_AMOGUS_PORTAL() {
		return LAYER_AMOGUS_PORTAL;
	}

	private static final RenderLayer LAYER_WHITE_GLINT = RenderLayer.MultiPhase.of(
			"entity_glint",
			VertexFormats.POSITION_TEXTURE,
			VertexFormat.DrawMode.QUADS,
			256,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(ModRenderLayers.getENTITY_GLINT_SHADER())
					.texture(new RenderPhase.Texture(ModRenderLayers.WHITE_GLINT, true, false))
					.writeMaskState(ModRenderLayers.getCOLOR_MASK())
					.cull(ModRenderLayers.getDISABLE_CULLING())
					.depthTest(ModRenderLayers.getEQUAL_DEPTH_TEST())
					.transparency(ModRenderLayers.getGLINT_TRANSPARENCY())
					.target(ModRenderLayers.getITEM_TARGET())
					.texturing(ModRenderLayers.getENTITY_GLINT_TEXTURING())
					.build(false)
	);

	private static final RenderLayer LAYER_AMOGUS_PORTAL = RenderLayer.MultiPhase.of(
			"end_portal",
			VertexFormats.POSITION,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
					.shader(ModRenderLayers.getEND_PORTAL_SHADER())
					.texture(
							RenderPhase.Textures.create()
									.add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
									.add(ModRenderLayers.AMOGUS_PORTAL_TEXTURE, false, false)
									.build()
					)
					.build(false)
	);


	public static RenderPhase.Shader getENTITY_GLINT_SHADER() {
		return ENTITY_GLINT_SHADER;
	}
	public static RenderPhase.Shader getEND_PORTAL_SHADER() {
		return END_PORTAL_SHADER;
	}
	public static RenderPhase.WriteMaskState getCOLOR_MASK() {
		return COLOR_MASK;
	}
	public static RenderPhase.Cull getDISABLE_CULLING() {
		return DISABLE_CULLING;
	}
	public static RenderPhase.DepthTest getEQUAL_DEPTH_TEST() {
		return EQUAL_DEPTH_TEST;
	}
	public static RenderPhase.Transparency getGLINT_TRANSPARENCY() {
		return GLINT_TRANSPARENCY;
	}
	public static RenderPhase.Target getITEM_TARGET() {
		return ITEM_TARGET;
	}
	public static RenderPhase.Texturing getENTITY_GLINT_TEXTURING() {
		return ENTITY_GLINT_TEXTURING;
	}

	public ModRenderLayers(String string, Runnable runnable, Runnable runnable2) {
		super(string, runnable, runnable2);
	}
}
