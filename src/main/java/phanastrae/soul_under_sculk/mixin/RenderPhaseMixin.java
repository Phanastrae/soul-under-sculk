package phanastrae.soul_under_sculk.mixin;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import phanastrae.soul_under_sculk.render.RenderPhaseExtension;

@Mixin(RenderPhase.class)
public class RenderPhaseMixin implements RenderPhaseExtension {
	@Shadow
	private static RenderPhase.Shader ENTITY_GLINT_SHADER;
	@Shadow
	private static RenderPhase.Shader END_PORTAL_SHADER;
	@Shadow
	private static RenderPhase.WriteMaskState COLOR_MASK;
	@Shadow
	private static RenderPhase.Cull DISABLE_CULLING;
	@Shadow
	private static RenderPhase.DepthTest EQUAL_DEPTH_TEST;
	@Shadow
	private static RenderPhase.Transparency GLINT_TRANSPARENCY;
	@Shadow
	private static RenderPhase.Target ITEM_TARGET;
	@Shadow
	private static RenderPhase.Texturing ENTITY_GLINT_TEXTURING;

	@Override
	public RenderPhase.Shader getENTITY_GLINT_SHADER() {
		return ENTITY_GLINT_SHADER;
	}

	@Override
	public RenderPhase.Shader getEND_PORTAL_SHADER() {
		return END_PORTAL_SHADER;
	}

	@Override
	public RenderPhase.WriteMaskState getCOLOR_MASK() {
		return COLOR_MASK;
	}

	@Override
	public RenderPhase.Cull getDISABLE_CULLING() {
		return DISABLE_CULLING;
	}

	@Override
	public RenderPhase.DepthTest getEQUAL_DEPTH_TEST() {
		return EQUAL_DEPTH_TEST;
	}

	@Override
	public RenderPhase.Transparency getGLINT_TRANSPARENCY() {
		return GLINT_TRANSPARENCY;
	}

	@Override
	public RenderPhase.Target getITEM_TARGET() {
		return ITEM_TARGET;
	}

	@Override
	public RenderPhase.Texturing getENTITY_GLINT_TEXTURING() {
		return ENTITY_GLINT_TEXTURING;
	}
}
