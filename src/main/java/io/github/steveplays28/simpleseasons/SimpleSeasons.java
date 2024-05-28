package io.github.steveplays28.simpleseasons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.steveplays28.simpleseasons.command.season.SetSeasonCommand;
import io.github.steveplays28.simpleseasons.server.state.ServerSeasonTracker;
import io.github.steveplays28.simpleseasons.state.SeasonState;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleSeasons implements ModInitializer {
	public static final String MOD_ID = "simple-seasons";
	public static final String MOD_NAMESPACE = "simpleseasons";
	public static final String MOD_NAME = "Simple Seasons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	public static final List<LiteralArgumentBuilder<ServerCommandSource>> COMMANDS = List.of(SetSeasonCommand.register());
	public static final Identifier SEASON_PACKET_CHANNEL = new Identifier(MOD_NAMESPACE);
	// Mod IDs
	public static final String MINECRAFT_MOD_ID = "minecraft";
	public static final long TIME_PER_DAY = 50;
	public static final long TIME_PER_SEASON_CHANGE = TIME_PER_DAY * 30 * 3;

	public static MinecraftServer server;

	@SuppressWarnings("unused")
	private static final SeasonTracker SEASON_TRACKER = new ServerSeasonTracker();

	@Override
	public void onInitialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		ServerLifecycleEvents.SERVER_STARTING.register(server -> SimpleSeasons.server = server);

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			for (var command : COMMANDS) {
				dispatcher.register(command);
			}
		});
	}

	// TODO: Move into SimpleSeasonsApi
	public static SeasonTracker.Seasons getSeason() {
		var simpleSeasonsState = SeasonState.getServerState(server);
		return SeasonTracker.Seasons.of(simpleSeasonsState.season);
	}

	// TODO: Move into SimpleSeasonsApi
	public static void setSeason(int seasonId) {
		SEASON_TRACKER.setSeason(seasonId);
	}

	// TODO: Move into SimpleSeasonsApi
	public static float getSeasonProgress() {
		return SEASON_TRACKER.getSeasonProgress();
	}

	@Contract(pure = true)
	public static boolean isDryBiome(float temperature, float downfall) {
		return temperature > 1.5f && downfall < 0.35f;
	}
}
