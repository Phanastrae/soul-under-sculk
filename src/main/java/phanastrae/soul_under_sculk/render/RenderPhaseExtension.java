package phanastrae.soul_under_sculk.render;

import net.minecraft.client.render.RenderPhase;

public interface RenderPhaseExtension {
	RenderPhase.Shader getENTITY_GLINT_SHADER();
	RenderPhase.Shader getEND_PORTAL_SHADER();
	RenderPhase.WriteMaskState getCOLOR_MASK();
	RenderPhase.Cull getDISABLE_CULLING();
	RenderPhase.DepthTest getEQUAL_DEPTH_TEST();
	RenderPhase.Transparency getGLINT_TRANSPARENCY();
	RenderPhase.Target getITEM_TARGET();
	RenderPhase.Texturing getENTITY_GLINT_TEXTURING();
}
