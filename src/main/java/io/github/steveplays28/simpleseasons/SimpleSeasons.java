package io.github.steveplays28.simpleseasons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.steveplays28.simpleseasons.command.season.SetSeasonCommand;
import io.github.steveplays28.simpleseasons.server.state.ServerSeasonTracker;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.state.SeasonState;
import io.github.steveplays28.simpleseasons.util.Color;
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
import java.util.Map;

public class SimpleSeasons implements ModInitializer {
	public static final String MOD_ID = "simple-seasons";
	public static final String MOD_NAMESPACE = "simpleseasons";
	public static final String MOD_NAME = "Simple Seasons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	public static final List<LiteralArgumentBuilder<ServerCommandSource>> COMMANDS = List.of(SetSeasonCommand.register());
	public static final Identifier SEASON_PACKET_CHANNEL = new Identifier(MOD_NAMESPACE);
	// Mod IDs
	public static final String MINECRAFT_MOD_ID = "minecraft";
	// Per-season color additions
	public static final Color SPRING_COLOR_ADDITION = new Color(255 / 3, 255 / 3, 0);
	public static final Color SUMMER_COLOR_ADDITION = new Color(0, 0, 0);
	public static final Color FALL_COLOR_ADDITION = new Color(255 / 3, 0, 0);
	public static final Color WINTER_COLOR_ADDITION = new Color(255 / 2, 255 / 2, 255 / 2);
	// Dry biomes
	public static final Color HOT_DRY_BIOMES_COLOR_ADDITION = new Color(120, 0, 0);
	public static final Color WET_DRY_BIOMES_COLOR_ADDITION = new Color(50, 50, 0);
	// Seasons color map
	public static final Map<Integer, Color> SEASONS_COLOR_ADDITIONS_MAP = Map.of(
			SeasonTracker.Seasons.SPRING.ordinal(),
			SPRING_COLOR_ADDITION, SeasonTracker.Seasons.SUMMER.ordinal(), SUMMER_COLOR_ADDITION, SeasonTracker.Seasons.FALL.ordinal(),
			FALL_COLOR_ADDITION, SeasonTracker.Seasons.WINTER.ordinal(), WINTER_COLOR_ADDITION
	);
	// Dry biomes
	public static final Map<Integer, Color> SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP = Map.of(SeasonTracker.Seasons.SPRING.ordinal(),
			HOT_DRY_BIOMES_COLOR_ADDITION, SeasonTracker.Seasons.SUMMER.ordinal(), HOT_DRY_BIOMES_COLOR_ADDITION,
			SeasonTracker.Seasons.FALL.ordinal(), WET_DRY_BIOMES_COLOR_ADDITION, SeasonTracker.Seasons.WINTER.ordinal(),
			WET_DRY_BIOMES_COLOR_ADDITION
	);
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

	// TODO: Move into SimpleSeasonsAPI
	public static SeasonTracker.Seasons getSeason() {
		var simpleSeasonsState = SeasonState.getServerState(server);
		return SeasonTracker.Seasons.of(simpleSeasonsState.season);
	}

	// TODO: Move into SimpleSeasonsAPI
	public static void setSeason(int seasonId) {
		SEASON_TRACKER.setSeason(seasonId);
	}

	@Contract(pure = true)
	public static boolean isDryBiome(float temperature, float downfall) {
		return temperature > 1.5f && downfall < 0.35f;
	}
}
