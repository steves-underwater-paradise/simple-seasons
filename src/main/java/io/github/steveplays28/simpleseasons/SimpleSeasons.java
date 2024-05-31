package io.github.steveplays28.simpleseasons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.steveplays28.simpleseasons.command.season.SetSeasonCommand;
import io.github.steveplays28.simpleseasons.server.state.ServerSeasonTracker;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleSeasons implements ModInitializer {
	public static final String MOD_ID = "simple-seasons";
	public static final String MOD_NAMESPACE = "simple_seasons";
	public static final String MOD_NAME = "Simple Seasons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	public static final List<LiteralArgumentBuilder<ServerCommandSource>> COMMANDS = List.of(SetSeasonCommand.register());
	public static final Identifier SEASON_PACKET_CHANNEL = new Identifier(MOD_NAMESPACE);
	public static final SeasonTracker SEASON_TRACKER = new ServerSeasonTracker();

	@Override
	public void onInitialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		registerCommands();
	}

	/**
	 * Registers serverside commands.
	 */
	private void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			for (var command : COMMANDS) {
				dispatcher.register(command);
			}
		});
	}
}
