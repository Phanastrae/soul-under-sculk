package phanastrae.soul_under_sculk.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;

// this entire process is so cursed wtf
// please tell me there's a better way to do this
// it can't be this complex
@Environment(EnvType.CLIENT)
public class ModRenderLayers {

	public static Identifier WHITE_GLINT = SoulUnderSculk.id("textures/misc/white_glint.png");
	public static Identifier AMOGUS_PORTAL_TEXTURE = SoulUnderSculk.id("textures/entity/amogus_portal.png");

	private static RenderLayerExtension renderLayer = (RenderLayerExtension)(Object)new RenderLayer(null, null, null, 0 ,false, false, () -> {return;}, () -> {return;}) {
		@Override
		public void draw(BufferBuilder buffer, int cameraX, int cameraY, int cameraZ) {
			super.draw(buffer, cameraX, cameraY, cameraZ);
		}
	};

	public static RenderLayer get_WHITE_GLINT() {
		return renderLayer.GET_WHITE_GLINT();
	}

	public static RenderLayer get_AMOGUS_PORTAL() {
		return renderLayer.GET_AMOGUS_PORTAL();
	}


}
