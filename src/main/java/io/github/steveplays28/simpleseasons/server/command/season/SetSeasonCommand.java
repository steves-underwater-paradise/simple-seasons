package io.github.steveplays28.simpleseasons.server.command.season;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAMESPACE;
import static io.github.steveplays28.simpleseasons.server.command.season.SeasonCommandCategory.SEASON_COMMAND_CATEGORY;

// TODO: Refactor into a base class that can be overridden and extended
//  This reduces duplicate code
public class SetSeasonCommand {
	public static final String NAME = "set";
	public static final int PERMISSION_LEVEL = 4;

	public static @NotNull LiteralArgumentBuilder<ServerCommandSource> register() {
		return CommandManager.literal(MOD_NAMESPACE).then(CommandManager.literal(SEASON_COMMAND_CATEGORY).then(
				CommandManager.literal(NAME).then(CommandManager.argument(SEASON_COMMAND_CATEGORY, IntegerArgumentType.integer()).executes(
						ctx -> execute(ctx, ctx.getSource())).requires(
						(ctx) -> Permissions.check(
								ctx,
								String.format("%s.commands.%s.%s", MOD_NAMESPACE, SEASON_COMMAND_CATEGORY, NAME), PERMISSION_LEVEL
						)
				))));
	}

	public static int execute(@NotNull CommandContext<ServerCommandSource> commandContext, @NotNull ServerCommandSource source) {
		var seasonIdArgument = IntegerArgumentType.getInteger(commandContext, "season");
		if (seasonIdArgument > SeasonTracker.Seasons.WINTER.ordinal() || seasonIdArgument < SeasonTracker.Seasons.SPRING.ordinal()) {
			source.sendError(
					Text.literal(String.format("Season argument can only be between 0-3 (inclusive), given: %s.", seasonIdArgument)));
			return 1;
		}

		var serverWorld = source.getWorld();
		var seasonArgument = SeasonTracker.Seasons.of(seasonIdArgument);
		var currentSeason = SimpleSeasonsApi.getSeason(serverWorld);
		if (seasonIdArgument == currentSeason.getId()) {
			source.sendError(Text.literal(
					String.format("Season argument (%s) is the same as the current season (%s).", seasonIdArgument, currentSeason)));
			return 1;
		}

		SimpleSeasonsApi.setSeason(serverWorld, seasonArgument);
		source.sendMessage(Text.literal(
				String.format(
						"Set season to %s (%s).", seasonIdArgument,
						SeasonTracker.Seasons.getName(seasonIdArgument).toLowerCase(Locale.ROOT)
				)));
		return 0;
	}
}
