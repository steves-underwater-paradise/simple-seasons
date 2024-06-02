package io.github.steveplays28.simpleseasons;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.server.command.CommandRegistry;
import io.github.steveplays28.simpleseasons.server.api.world.registry.state.ServerWorldSeasonTrackerRegistry;
import io.github.steveplays28.simpleseasons.server.state.world.ServerWorldSeasonTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSeasons implements ModInitializer {
	public static final String MOD_ID = "simple-seasons";
	public static final String MOD_NAMESPACE = "simple_seasons";
	public static final String MOD_NAME = "Simple Seasons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	public static final Identifier SEASON_PACKET_CHANNEL = new Identifier(MOD_NAMESPACE);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		ServerLifecycleEvents.SERVER_STARTED.register(this::registerWorldSeasonTrackers);
		CommandRegistry.registerCommands();
	}

	private void registerWorldSeasonTrackers(@NotNull MinecraftServer server) {
		for (@NotNull var serverWorld : server.getWorlds()) {
			if (!SimpleSeasonsApi.worldHasSeasons(serverWorld)) {
				continue;
			}

			ServerWorldSeasonTrackerRegistry.register(serverWorld.getRegistryKey().getValue(), new ServerWorldSeasonTracker(server, serverWorld));
		}
	}
}
