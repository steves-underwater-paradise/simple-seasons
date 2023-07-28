package io.github.steveplays28.simpleseasons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASON_PACKET_CHANNEL;

public class SimpleSeasonsClient implements ClientModInitializer {
	public static int season;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SEASON_PACKET_CHANNEL, (client, handler, buf, responseSender) -> {
			if (client.world == null || client.player == null) return;

			season = buf.readInt();
		});
	}
}
