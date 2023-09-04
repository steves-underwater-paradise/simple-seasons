package io.github.steveplays28.simpleseasons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASON_PACKET_CHANNEL;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsClient implements ClientModInitializer {
	public static int season;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SEASON_PACKET_CHANNEL, (client, handler, buf, responseSender) -> {
			if (client.world == null || client.player == null) return;

			season = buf.readInt();
			client.world.reloadColor();

			var initialLoad = buf.readBoolean();
			if (initialLoad) return;

			var viewDistance = MinecraftClient.getInstance().options.getViewDistance().getValue();
			var viewDistanceBlocks = viewDistance * 16;

			scheduleChunkRenders(client.world, -viewDistanceBlocks, viewDistanceBlocks, -viewDistanceBlocks, viewDistanceBlocks,
					-viewDistanceBlocks, viewDistanceBlocks
			);
		});
	}

	private void scheduleChunkRenders(ClientWorld world, int fromX, int toX, int fromY, int toY, int fromZ, int toZ) {
		int lastChunkPosX = Integer.MAX_VALUE;
		int lastChunkPosY = Integer.MAX_VALUE;
		int lastChunkPosZ = Integer.MAX_VALUE;

		for (int x = fromX; x <= toX; x++) {
			for (int y = fromY; y <= toY; y++) {
				for (int z = fromZ; z <= toZ; z++) {
					if (x == lastChunkPosX && y == lastChunkPosY && z == lastChunkPosZ) {
						continue;
					}

					world.worldRenderer.scheduleChunkRender(x >> 4, y >> 4, z >> 4, true);
					lastChunkPosX = x;
					lastChunkPosY = y;
					lastChunkPosZ = z;
				}
			}
		}
	}
}
