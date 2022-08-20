package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public abstract class TransformationType {

	public static final Identifier ID_NO_TRANSFORMATION = SoulUnderSculk.id("none");

	@Nullable
	private String translationKey;

	public Identifier getRegistryId() {
		return SoulUnderSculk.TRANSFORMATIONS.getId(this);
	}

	public MutableText getName() {
		return Text.translatable(this.getTranslationKey());
	}

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("soul_under_sculk.transformation", getRegistryId());
		}

		return this.translationKey;
	}

	public abstract Identifier getIconId();

	public abstract boolean shouldRenderIcon();

	public abstract void handleIsInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable cir);

	public abstract void onTransform(TransformationHandler transHandler);
}
