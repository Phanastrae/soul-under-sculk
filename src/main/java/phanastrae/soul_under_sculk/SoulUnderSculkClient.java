package phanastrae.soul_under_sculk;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import phanastrae.soul_under_sculk.packets.ModPackets;

public class SoulUnderSculkClient implements ClientModInitializer {

    @Override
	public void onInitializeClient(ModContainer mod) {
		ModPackets.init();
	}
}
