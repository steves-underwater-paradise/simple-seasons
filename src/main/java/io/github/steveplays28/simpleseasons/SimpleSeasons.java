package io.github.steveplays28.simpleseasons;

import io.github.steveplays28.simpleseasons.command.CommandRegistry;
import io.github.steveplays28.simpleseasons.server.state.ServerSeasonTracker;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSeasons implements ModInitializer {
	public static final String MOD_ID = "simple-seasons";
	public static final String MOD_NAMESPACE = "simple_seasons";
	public static final String MOD_NAME = "Simple Seasons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	public static final Identifier SEASON_PACKET_CHANNEL = new Identifier(MOD_NAMESPACE);
	public static final SeasonTracker SEASON_TRACKER = new ServerSeasonTracker();

	@Override
	public void onInitialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		CommandRegistry.registerCommands();
	}
}
