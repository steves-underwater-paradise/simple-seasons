package io.github.steveplays28.simpleseasons.client.util.rendering;

import io.github.steveplays28.simpleseasons.mixin.client.accessor.WorldRendererAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class RenderingUtil {
	public static void reloadChunkColors(@NotNull ClientWorld world) {
		world.reloadColor();

		for (ChunkBuilder.BuiltChunk builtChunk : ((WorldRendererAccessor) world.worldRenderer).getChunks().chunks) {
			builtChunk.scheduleRebuild(true);
		}
	}
}
