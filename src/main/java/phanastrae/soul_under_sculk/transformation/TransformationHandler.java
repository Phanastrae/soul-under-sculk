package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.util.TransformableEntity;

public class TransformationHandler {

	private boolean shouldSyncData = false;
	private boolean shouldClientReloadModel = false;

	public PlayerEntity player;
	public TransformationType transformationType;

	public TransformationHandler(PlayerEntity player) {
		this.player = player;
	}

	public void setTransformation(TransformationType type) {
		if(player == null) return;

		if(this.transformationType != type) {
			this.transformationType = type;
			if(this.transformationType != null) {
				this.transformationType.onTransform(this);
			}

			if(player.world instanceof ServerWorld) {
				setShouldSyncData(true);
			} else {
				setShouldClientReloadModel(true);
			}

			player.calculateDimensions();
		}
	}

	public TransformationType getTransformation() {
		return this.transformationType;
	}

	public boolean isTransformed() {
		return getTransformation() != null;
	}

	public void readNbt(NbtCompound nbt) {
		NbtCompound susNbt = nbt.getCompound("SoulUnderSculk");

		this.transformationType = SoulUnderSculk.TRANSFORMATIONS.get(new Identifier(susNbt.getString("Transformation")));
		player.calculateDimensions();
	}

	public void writeNbt(NbtCompound nbt) {
		NbtCompound susNbt = new NbtCompound();

		susNbt.putString("Transformation", (this.transformationType == null) ? "" : this.transformationType.getRegistryId().toString());

		nbt.put("SoulUnderSculk", susNbt);
	}

	public void loadFromOnDeath(TransformationHandler oldTransHandler, boolean alive) {
		if(alive || false) { // TODO: Curse of Transcendence
			this.setTransformation(oldTransHandler.transformationType);
		}
	}

	public void setShouldSyncData(boolean b) {
		shouldSyncData = b;
	}

	public boolean shouldSyncData() {
		return shouldSyncData;
	}

	public void setShouldClientReloadModel(boolean b) {
		shouldClientReloadModel = b;
	}

	public boolean shouldClientReloadModel() {
		return shouldClientReloadModel;
	}

	public static TransformationHandler getFromEntity(Entity entity) {
		if(!(entity instanceof TransformableEntity)) return null;
		return ((TransformableEntity)entity).getTransHandler();
	}

	public static TransformationType getTypeFromEntity(Entity entity) {
		TransformationHandler handler = getFromEntity(entity);
		if(handler == null) return null;
		return handler.getTransformation();
	}
}
