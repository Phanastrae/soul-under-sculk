package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import phanastrae.soul_under_sculk.networking.ModPackets;

import java.awt.*;

public class SculkmateTransformationData extends TransformationData {

	private CompositeColorEntry eyeColor = new CompositeColorEntry();
	private CompositeColorEntry sculkColor = new CompositeColorEntry();
	private CompositeColorEntry glowSculkColor = new CompositeColorEntry();
	private CompositeColorEntry boneColor = new CompositeColorEntry();
	private CompositeColorEntry obsidianColor = new CompositeColorEntry();
	private CompositeColorEntry glowstoneColor = new CompositeColorEntry();
	private CompositeColorEntry cryingColor = new CompositeColorEntry();
	private CompositeColorEntry particleColor = new CompositeColorEntry();

	private float distortionFactor = 0;
	private float distortionFactorLast = 0;
	private float distortionFactorTarget = 0;

	private String earType;

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
		writeColorNbt(nbt, this.particleColor, "ParticleColor");
		if(earType != null) {
			nbt.putString("EarType", earType);
		}
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
		this.particleColor = readColorNbt(nbt, "ParticleColor");
		this.earType = nbt.getString("EarType");
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

	public CompositeColorEntry getParticleColor() {
		return this.particleColor;
	}

	public void setParticleColor(CompositeColorEntry color) {
		this.particleColor = color;
	}

	public void updateDistortion() {
		this.distortionFactorLast = this.distortionFactor;
		this.distortionFactor = this.distortionFactorTarget + (this.distortionFactor - this.distortionFactorTarget) * 0.6F;
		this.distortionFactor = Math.max(-0.9F, Math.min(0.9F, this.distortionFactor));
		this.distortionFactorTarget *= 0.6F;
	}

	public float getDistortionFactorLerp(float tickDelta) {
		return this.distortionFactor * tickDelta + this.distortionFactorLast * (1 - tickDelta);
	}

	public void setDistortionFactor(float f) {
		this.distortionFactor = f;
	}

	public float getDistortionFactor() {
		return this.distortionFactor;
	}

	public void setDistortionFactorTarget(float f) {
		this.distortionFactorTarget = f;
	}

	public float getDistortionFactorTarget() {
		return this.distortionFactorTarget;
	}

	public void sendDistortionPacket(Entity e, float d) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(e.getId());
		buf.writeFloat(d);

		for(PlayerEntity p : e.getWorld().getPlayers()) {
			if(p.equals(e)) continue;
			if(!(p instanceof ServerPlayerEntity)) continue;

			((ServerPlayerEntity)p).networkHandler.sendPacket(new CustomPayloadS2CPacket(ModPackets.SCULKMATE_DISTORT_PACKET_ID, buf));
		}
	}

	public void setEarType(String str) {
		this.earType = str;
	}

	public String getEarType() {
		return this.earType;
	}
}
