package io.github.steveplays28.simpleseasons.client.util.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RenderingUtil {
	public static void reloadChunkColors(@NotNull ClientWorld clientWorld) {
		clientWorld.reloadColor();

		@NotNull final var client = MinecraftClient.getInstance();
		@Nullable final var player = client.player;
		if (player == null) {
			return;
		}

		@NotNull final var playerChunkPos = player.getChunkPos();
		var clampedViewDistance = MinecraftClient.getInstance().options.getClampedViewDistance();
		for (int chunkX = playerChunkPos.x - clampedViewDistance; chunkX < playerChunkPos.x + clampedViewDistance; chunkX++) {
			for (int chunkZ = playerChunkPos.z - clampedViewDistance; chunkZ < playerChunkPos.z + clampedViewDistance; chunkZ++) {
				for (int chunkY = clientWorld.getBottomY() >> 4; chunkY < clientWorld.getTopY() >> 4; chunkY++) {
					client.worldRenderer.scheduleChunkRender(chunkX, chunkY, chunkZ, true);
				}
			}
		}
	}
}
