package phanastrae.soul_under_sculk.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.util.Collection;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
	public InGameHudMixin(MinecraftClient client) {
		this.client = client;
	}
	@Shadow
	private final MinecraftClient client;
	@Shadow
	private int scaledWidth;
	@Shadow
	private int scaledHeight;

	// causes potion status hud to also render if player is transformed
	@Redirect(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
	protected boolean SoulUnderSculk_renderStatusEffectOverlayIf(Collection collection, MatrixStack matrices) {
		if(!collection.isEmpty()) return false;

		ClientPlayerEntity player = this.client.player;
		if (player == null) return true;
		if (!(player instanceof TransformableEntity)) return true;
		TransformationHandler transHandler = ((TransformableEntity) player).getTransHandler();
		if (transHandler == null) return true;

		return !transHandler.isTransformed();
	}

	// when i (positive effects counter) is assigned try rendering the transformation thingy, and if so add 1 to i.
	@ModifyVariable(method = "renderStatusEffectOverlay", at = @At("STORE"), ordinal = 0)
	protected int SoulUnderSculk_renderStatusEffectOverlayInt(int i, MatrixStack matrices) {
		ClientPlayerEntity player = this.client.player;
		if(player == null) return i;
		if(!(player instanceof TransformableEntity)) return i;
		TransformationHandler transHandler = ((TransformableEntity) player).getTransHandler();
		if(transHandler == null) return i;
		if(!transHandler.isTransformed()) return i;
		if(!transHandler.transformationType.shouldRenderIcon()) return i;

		Screen j = this.client.currentScreen;
		if (j instanceof AbstractInventoryScreen abstractInventoryScreen && abstractInventoryScreen.hideStatusEffectHud()) {
			return i;
		}

		i++;
		int k = this.scaledWidth - 25 * i;
		int l = 1;
		if (this.client.isDemo()) {
			l += 15;
		}

		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		Matrix4f matrix4f = matrices.peek().getPosition();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();

		RenderSystem.setShaderTexture(0, new Identifier("soul_under_sculk", "textures/gui/soul_under_sculk/transformation_icon/guiback.png"));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix4f, (float)k, (float)l+24, (float)0).uv(0, 1).next();
		bufferBuilder.vertex(matrix4f, (float)k+24, (float)l+24, (float)0).uv(1, 1).next();
		bufferBuilder.vertex(matrix4f, (float)k+24, (float)l, (float)0).uv(1, 0).next();
		bufferBuilder.vertex(matrix4f, (float)k, (float)l, (float)0).uv(0, 0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());

		Identifier iconId = transHandler.getTransformation().getIconId();
		if(iconId != null) {
			RenderSystem.setShaderTexture(0, iconId);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			bufferBuilder.vertex(matrix4f, (float) k, (float) l + 24, (float) 0).uv(0, 1).next();
			bufferBuilder.vertex(matrix4f, (float) k + 24, (float) l + 24, (float) 0).uv(1, 1).next();
			bufferBuilder.vertex(matrix4f, (float) k + 24, (float) l, (float) 0).uv(1, 0).next();
			bufferBuilder.vertex(matrix4f, (float) k, (float) l, (float) 0).uv(0, 0).next();
			BufferRenderer.drawWithShader(bufferBuilder.end());
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		return i;
	}
}
