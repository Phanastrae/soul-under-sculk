package phanastrae.soul_under_sculk.transformation;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public abstract class TransformationType {

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
}
