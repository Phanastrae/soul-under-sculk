package phanastrae.soul_under_sculk.transformation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.packets.ModPackets;

public class TransformationHandler {

	private boolean shouldSyncData = false;

	public PlayerEntity player;
	public TransformationType transformationType;

	public TransformationHandler(PlayerEntity player) {
		this.player = player;
	}

	public void setTransformation(TransformationType type) {
		if(player == null) return;

		if(this.transformationType != type) {
			this.transformationType = type;

			if(player.world instanceof ServerWorld) {
				setShouldSyncData(true);
			}
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
	}

	public void writeNbt(NbtCompound nbt) {
		NbtCompound susNbt = new NbtCompound();

		susNbt.putString("Transformation", (this.transformationType == null) ? "" : this.transformationType.getRegistryId().toString());

		nbt.put("SoulUnderSculk", susNbt);
	}

	public void setShouldSyncData(boolean b) {
		shouldSyncData = b;
	}

	public boolean shouldSyncData() {
		return shouldSyncData;
	}
}
