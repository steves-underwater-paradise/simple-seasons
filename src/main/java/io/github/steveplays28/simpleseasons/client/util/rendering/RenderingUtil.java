package io.github.steveplays28.simpleseasons.client.util.rendering;

import io.github.steveplays28.simpleseasons.client.compat.sodium.SimpleSeasonsSodiumCompat;
import io.github.steveplays28.simpleseasons.mixin.client.accessor.WorldRendererAccessor;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RenderingUtil {
	@SuppressWarnings("ForLoopReplaceableByForEach")
	public static void reloadChunkColors(@NotNull ClientWorld clientWorld) {
		clientWorld.reloadColor();

		if (SimpleSeasonsSodiumCompat.isSodiumLoaded()) {
			@NotNull var client = MinecraftClient.getInstance();
			@Nullable var player = client.player;
			if (player == null) {
				return;
			}

			var playerChunkPos = player.getChunkPos();
			var clampedViewDistance = MinecraftClient.getInstance().options.getClampedViewDistance();
			SodiumWorldRenderer.instance().scheduleRebuildForChunks(
					playerChunkPos.x - clampedViewDistance, clientWorld.getBottomY() >> 4, playerChunkPos.z - clampedViewDistance,
					playerChunkPos.x + clampedViewDistance, clientWorld.getTopY() >> 4, playerChunkPos.z + clampedViewDistance, true
			);
			return;
		}

		@Nullable var chunks = ((WorldRendererAccessor) clientWorld.worldRenderer).getChunks();
		if (chunks == null) {
			return;
		}

		@NotNull var builtChunks = chunks.chunks;
		for (int i = 0; i < builtChunks.length; i++) {
			builtChunks[i].scheduleRebuild(true);
		}
	}
}
