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

import java.util.Locale;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;
import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAMESPACE;
import static io.github.steveplays28.simpleseasons.server.command.season.SeasonCommandCategory.seasonCommandCategory;

public class SetSeasonCommand {
	public static final String NAME = "set";
	public static final int PERMISSION_LEVEL = 4;

	public static LiteralArgumentBuilder<ServerCommandSource> register() {
		return CommandManager.literal(MOD_NAMESPACE).then(CommandManager.literal(seasonCommandCategory).then(
				CommandManager.literal(NAME).then(CommandManager.argument("season", IntegerArgumentType.integer()).executes(
						ctx -> execute(ctx, ctx.getSource())).requires(
						(ctx) -> Permissions.check(ctx, MOD_ID + ".commands." + NAME, PERMISSION_LEVEL)))));
	}

	public static int execute(CommandContext<ServerCommandSource> commandContext, ServerCommandSource source) {
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
