package phanastrae.soul_under_sculk.networking;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;
import phanastrae.soul_under_sculk.transformation.TransformationType;
import phanastrae.soul_under_sculk.util.TransformableEntity;

import java.util.function.Consumer;

public class TransformationSyncHandler {

	public static void syncDataOnStart(TransformableEntity entity, Consumer<Packet<?>> packetSender) {
		if(entity.getTransHandler() == null) return;
		syncData(entity, packetSender, false);
	}

	public static void syncDataIfNeeded(TransformableEntity entity, Consumer<Packet<?>> packetSender) {
		if(entity.getTransHandler() == null) return;
		if(entity.getTransHandler().shouldSyncData()) {
			syncData(entity, packetSender, true);
		}
	}

	public static void syncData(TransformableEntity entity, Consumer<Packet<?>> packetSender, boolean cleanSyncFlags) {
		TransformationHandler transHandler = entity.getTransHandler();

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier((transHandler.getTransformation() == null) ? TransformationType.ID_NO_TRANSFORMATION : transHandler.getTransformation().getRegistryId());
		buf.writeInt(((Entity)entity).getId());
		NbtCompound nbt = new NbtCompound();
		transHandler.writeNbt(nbt);
		buf.writeNbt(nbt);

		packetSender.accept(new CustomPayloadS2CPacket(ModPackets.ENTITY_TRANSFORM_PACKET_ID, buf));

		if(cleanSyncFlags) {
			transHandler.setShouldSyncData(false);
		}
	}
}
