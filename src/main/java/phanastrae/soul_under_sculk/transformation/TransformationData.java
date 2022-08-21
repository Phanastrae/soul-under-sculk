package phanastrae.soul_under_sculk.transformation;

import net.minecraft.nbt.NbtCompound;

public abstract class TransformationData {

	public TransformationData(TransformationHandler transHandler) {
		this.transformationHandler = transHandler;
		setShouldSyncData(true);
	}

	private final TransformationHandler transformationHandler;

	private boolean shouldSyncData = false;

	public void setShouldSyncData(boolean b) {
		this.shouldSyncData = b;
		if(b && !this.transformationHandler.shouldSyncData()) {
			this.transformationHandler.setShouldSyncData(true);
		}
	}

	public boolean shouldSyncData() {
		return shouldSyncData;
	}

	public abstract void writeNbt(NbtCompound nbtCompound);
	public abstract void readNbt(NbtCompound nbtCompound);
}
