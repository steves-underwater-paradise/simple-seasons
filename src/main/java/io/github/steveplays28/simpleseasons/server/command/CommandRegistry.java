package io.github.steveplays28.simpleseasons.server.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.server.command.config.ReloadConfigCommand;
import io.github.steveplays28.simpleseasons.server.command.season.SeasonArgumentType;
import io.github.steveplays28.simpleseasons.server.command.season.SetSeasonCommand;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.List;

public class CommandRegistry {
	/**
	 * A {@link List} of serverside commands that should be registered in the {@link CommandRegistrationCallback} event.
	 *
	 * @see CommandRegistry#registerCommands
	 * @see CommandRegistrationCallback
	 */
	public static final List<LiteralArgumentBuilder<ServerCommandSource>> COMMANDS = List.of(
			SetSeasonCommand.register(),
			ReloadConfigCommand.register()
	);

	/**
	 * Registers serverside commands.
	 */
	public static void registerCommands() {
		ArgumentTypeRegistry.registerArgumentType(Identifier.of(SimpleSeasons.MOD_ID, "season"), SeasonArgumentType.class, ConstantArgumentSerializer.of(SeasonArgumentType::season));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			for (var command : COMMANDS) {
				dispatcher.register(command);
			}
		});
	}
}
