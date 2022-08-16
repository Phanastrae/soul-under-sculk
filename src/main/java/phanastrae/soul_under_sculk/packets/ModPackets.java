package phanastrae.soul_under_sculk.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import phanastrae.soul_under_sculk.SoulUnderSculk;
import phanastrae.soul_under_sculk.transformation.TransformationType;
import phanastrae.soul_under_sculk.util.TransformableEntity;


public class ModPackets {
	public static Identifier ENTITY_TRANSFORM_PACKET_ID = SoulUnderSculk.id("entity_transform");

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(ENTITY_TRANSFORM_PACKET_ID, (client, handler, buf, responseSender) -> {
			Identifier id = buf.readIdentifier();
			int entityId = buf.readInt();

			client.execute(() -> {
				Entity entity = client.world.getEntityById(entityId);
				if(entity instanceof PlayerEntity) {
					TransformationType transformationType = SoulUnderSculk.TRANSFORMATIONS.get(id);
					((TransformableEntity)entity).getTransHandler().setTransformation(transformationType);
				}
			});
		});
	}
}
