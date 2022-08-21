package phanastrae.soul_under_sculk.transformation;

import net.minecraft.nbt.NbtCompound;

import java.awt.*;

public class SculkmateTransformationData extends TransformationData {

	private CompositeColorEntry eyeColor = new CompositeColorEntry();
	private CompositeColorEntry sculkColor = new CompositeColorEntry();
	private CompositeColorEntry glowSculkColor = new CompositeColorEntry();
	private CompositeColorEntry boneColor = new CompositeColorEntry();
	private CompositeColorEntry obsidianColor = new CompositeColorEntry();
	private CompositeColorEntry glowstoneColor = new CompositeColorEntry();
	private CompositeColorEntry cryingColor = new CompositeColorEntry();

	public SculkmateTransformationData(TransformationHandler transHandler) {
		super(transHandler);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		writeColorNbt(nbt, this.eyeColor, "EyeColor");
		writeColorNbt(nbt, this.sculkColor, "SculkColor");
		writeColorNbt(nbt, this.glowSculkColor, "GlowSculkColor");
		writeColorNbt(nbt, this.boneColor, "BoneColor");
		writeColorNbt(nbt, this.obsidianColor, "ObsidianColor");
		writeColorNbt(nbt, this.glowstoneColor, "GlowstoneColor");
		writeColorNbt(nbt, this.cryingColor, "CryingColor");
	}

	public void writeColorNbt(NbtCompound nbt, CompositeColorEntry cce, String key) {
		if(cce != null) {
			cce.writeNbt(nbt, key);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.eyeColor = readColorNbt(nbt, "EyeColor");
		this.sculkColor = readColorNbt(nbt, "SculkColor");
		this.glowSculkColor = readColorNbt(nbt, "GlowSculkColor");
		this.boneColor = readColorNbt(nbt, "BoneColor");
		this.obsidianColor = readColorNbt(nbt, "ObsidianColor");
		this.glowstoneColor = readColorNbt(nbt, "GlowstoneColor");
		this.cryingColor = readColorNbt(nbt, "CryingColor");
	}

	public CompositeColorEntry readColorNbt(NbtCompound nbt, String key) {
		if(nbt.contains(key)) {
			CompositeColorEntry cce = new CompositeColorEntry();
			cce.readNbt(nbt, key);
			return cce;
		} else {
			return null;
		}
	}

	public CompositeColorEntry getEyeColor() {
		return this.eyeColor;
	}

	public void setEyeColor(CompositeColorEntry color) {
		this.eyeColor = color;
	}

	public CompositeColorEntry getSculkColor() {
		return this.sculkColor;
	}

	public void setSculkColor(CompositeColorEntry color) {
		this.sculkColor = color;
	}

	public CompositeColorEntry getGlowSculkColor() {
		return this.glowSculkColor;
	}

	public void setGlowSculkColor(CompositeColorEntry color) {
		this.glowSculkColor = color;
	}

	public CompositeColorEntry getBoneColor() {
		return this.boneColor;
	}

	public void setBoneColor(CompositeColorEntry color) {
		this.boneColor = color;
	}

	public CompositeColorEntry getObsidianColor() {
		return this.obsidianColor;
	}

	public void setObsidianColor(CompositeColorEntry color) {
		this.obsidianColor = color;
	}

	public CompositeColorEntry getGlowstoneColor() {
		return this.glowstoneColor;
	}

	public void setGlowstoneColor(CompositeColorEntry color) {
		this.glowstoneColor = color;
	}

	public CompositeColorEntry getCryingColor() {
		return this.cryingColor;
	}

	public void setCryingColor(CompositeColorEntry color) {
		this.cryingColor = color;
	}
}
