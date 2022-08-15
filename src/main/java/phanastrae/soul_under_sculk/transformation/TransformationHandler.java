package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;

public class TransformationHandler {

	public PlayerEntity player;
	public TransformationType transformationType;

	public TransformationHandler(PlayerEntity player) {
		this.player = player;
	}

	public void setTransformation(TransformationType type) {
		if(player == null) return;
		this.transformationType = type;

		if(type != null) {
			SoulUnderSculk.LOGGER.info("Set " + player.getEntityName() + " to " + type.getRegistryId() + "!");
		} else {
			SoulUnderSculk.LOGGER.info("Cleared " + player.getEntityName() + "'s transformation!");
		}
	}

	public TransformationType getTransformation() {
		return this.transformationType;
	}

	public void readNbt(NbtCompound nbt) {
		NbtCompound susNbt = nbt.getCompound("SoulUnderSculk");

		this.transformationType = SoulUnderSculk.TRANSFORMATIONS.get(new Identifier(susNbt.getString("Transformation")));
	}

	public void writeNbt(NbtCompound nbt) {
		NbtCompound susNbt = new NbtCompound();

		susNbt.putString("Transformation", (this.transformationType == null) ? null : this.transformationType.getRegistryId().toString());

		nbt.put("SoulUnderSculk", susNbt);
	}
}
