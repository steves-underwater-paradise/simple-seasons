package io.github.steveplays28.simpleseasons.server.api.world.registry.state;

import io.github.steveplays28.simpleseasons.server.state.world.ServerWorldSeasonTracker;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ServerWorldSeasonTrackerRegistry {
	private static final @NotNull Map<@Nullable Identifier, @Nullable ServerWorldSeasonTracker> SERVER_WORLD_SEASON_TRACKER_REGISTRY = new HashMap<>();

	public static void register(@NotNull Identifier serverWorldIdentifier, @NotNull ServerWorldSeasonTracker serverWorldSeasonTracker) {
		SERVER_WORLD_SEASON_TRACKER_REGISTRY.put(serverWorldIdentifier, serverWorldSeasonTracker);
	}

	public static @Nullable ServerWorldSeasonTracker get(@NotNull Identifier serverWorldIdentifier) {
		return SERVER_WORLD_SEASON_TRACKER_REGISTRY.get(serverWorldIdentifier);
	}
}
