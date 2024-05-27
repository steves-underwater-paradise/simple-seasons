package io.github.steveplays28.simpleseasons.client.util.rendering;

import io.github.steveplays28.simpleseasons.mixin.accessor.WorldRendererAccessor;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

public class RenderingUtil {
	public static void reloadChunkColors(@NotNull ClientWorld world) {
		world.reloadColor();

		for (ChunkBuilder.BuiltChunk builtChunk : ((WorldRendererAccessor) world.worldRenderer).getChunks().chunks) {
			builtChunk.scheduleRebuild(true);
		}
	}
}
