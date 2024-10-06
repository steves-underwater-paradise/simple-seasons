package io.github.steveplays28.simpleseasons.server.command.season;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAMESPACE;
import static io.github.steveplays28.simpleseasons.server.command.season.SeasonCommandCategory.SEASON_COMMAND_CATEGORY;

// TODO: Refactor into a base class that can be overridden and extended
//  This reduces duplicate code
public class SetSeasonCommand {
	private static final String NAME = "set";
	private static final int PERMISSION_LEVEL = 4;

	public static @NotNull LiteralArgumentBuilder<ServerCommandSource> register() {
		return CommandManager.literal(MOD_NAMESPACE).then(CommandManager.literal(SEASON_COMMAND_CATEGORY).then(
				CommandManager.literal(NAME).then(CommandManager.argument(SEASON_COMMAND_CATEGORY, new SeasonArgumentType()).executes(
						ctx -> execute(ctx, ctx.getSource())).requires(
						(ctx) -> Permissions.check(
								ctx,
								String.format("%s.commands.%s.%s", MOD_NAMESPACE, SEASON_COMMAND_CATEGORY, NAME), PERMISSION_LEVEL
						)
				))));
	}

	public static int execute(@NotNull CommandContext<ServerCommandSource> commandContext, @NotNull ServerCommandSource source) {
		var seasonArgument = SeasonArgumentType.getArgument(commandContext);
		var serverWorld = source.getWorld();
		var currentSeason = SimpleSeasonsApi.getSeason(serverWorld);
		if (seasonArgument.getId() == currentSeason.getId()) {
			source.sendError(Text.literal(
					String.format(
							"Season argument (%s) is the same as the current season (%s).", seasonArgument.asString(),
							currentSeason.asString()
					)));
			return 1;
		}

		SimpleSeasonsApi.setSeason(serverWorld, seasonArgument);
		source.sendMessage(Text.literal(
				String.format(
						"Set season to %s.", seasonArgument.asString()
				)
		));
		return 0;
	}
}
