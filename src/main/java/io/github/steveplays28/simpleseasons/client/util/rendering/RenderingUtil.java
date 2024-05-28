package io.github.steveplays28.simpleseasons.client.util.rendering;

import io.github.steveplays28.simpleseasons.mixin.client.accessor.WorldRendererAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class RenderingUtil {
	@SuppressWarnings("ForLoopReplaceableByForEach")
	public static void reloadChunkColors(@NotNull ClientWorld world) {
		world.reloadColor();

		var chunks = ((WorldRendererAccessor) world.worldRenderer).getChunks();
		if (chunks == null) {
			return;
		}

		@NotNull var builtChunks = chunks.chunks;
		for (int i = 0; i < builtChunks.length; i++) {
			builtChunks[i].scheduleRebuild(true);
		}
	}
}
