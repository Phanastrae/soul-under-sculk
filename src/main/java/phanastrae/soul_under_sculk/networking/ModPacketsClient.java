package phanastrae.soul_under_sculk.networking;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import phanastrae.soul_under_sculk.transformation.SculkmateTransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationData;
import phanastrae.soul_under_sculk.transformation.TransformationHandler;

public class ModPacketsClient {

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(ModPackets.ENTITY_TRANSFORM_PACKET_ID, (client, handler, buf, responseSender) -> {
			Identifier id = buf.readIdentifier();
			int entityId = buf.readInt();
			NbtCompound nbt = buf.readNbt();

			client.execute(() -> {
				Entity entity = client.world.getEntityById(entityId);
				TransformationHandler transHandler = TransformationHandler.getFromEntity(entity);
				if(transHandler != null) {
					transHandler.readNbt(nbt);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(ModPackets.SCULKMATE_DISTORT_PACKET_ID, (client, handler, buf, responseSender) -> {
			int entityId = buf.readInt();
			float amount = buf.readFloat();

			client.execute(() -> {
				Entity entity = client.world.getEntityById(entityId);
				TransformationHandler transHandler = TransformationHandler.getFromEntity(entity);
				if(transHandler != null) {
					TransformationData transData = transHandler.getTransformationData();
					if(transData instanceof SculkmateTransformationData) {
						((SculkmateTransformationData)transData).setDistortionFactorTarget(amount);
					}
				}
			});
		});
	}
}
