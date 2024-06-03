package io.github.steveplays28.simpleseasons.client.color.world;

import io.github.steveplays28.simpleseasons.client.world.event.ClientWorldEvents;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleSeasonsClientWorldColorCache {
	public final @NotNull ConcurrentMap<@NotNull BlockPos, @NotNull Color> blockPosColorCache = new ConcurrentHashMap<>();

	private final @NotNull ClientWorld clientWorld;

	public SimpleSeasonsClientWorldColorCache(@NotNull ClientWorld clientWorld) {
		this.clientWorld = clientWorld;

		ClientWorldEvents.COLOR_RELOAD.register(colorReloadClientWorld -> {
			if (!this.clientWorld.equals(colorReloadClientWorld)) {
				return;
			}

			blockPosColorCache.clear();
		});
	}
}
