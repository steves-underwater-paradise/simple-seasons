package io.github.steveplays28.simpleseasons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASON_PACKET_CHANNEL;

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

			scheduleBlockRenders(
					client.world, -viewDistanceBlocks, viewDistanceBlocks, -viewDistanceBlocks, viewDistanceBlocks, -viewDistanceBlocks,
					viewDistanceBlocks
			);
		});
	}

	private void scheduleBlockRenders(ClientWorld world, int fromX, int toX, int fromY, int toY, int fromZ, int toZ) {
		for (int x = fromX; x < toX; x++) {
			for (int y = fromY; y < toY; y++) {
				for (int z = fromZ; z < toZ; z++) {
					world.worldRenderer.scheduleBlockRender(x, y, z);
					world.markChunkRenderability(x, z);
				}
			}
		}
	}
}
