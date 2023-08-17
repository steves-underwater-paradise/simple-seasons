package io.github.steveplays28.simpleseasons.command.season;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.*;
import static io.github.steveplays28.simpleseasons.command.season.SeasonCommandCategory.seasonCommandCategory;

public class SetSeasonCommand {
	public static final String NAME = "set";
	public static final int PERMISSION_LEVEL = 4;

	public static LiteralArgumentBuilder<ServerCommandSource> register() {
		return CommandManager.literal(MOD_NAMESPACE).then(
				CommandManager.literal(seasonCommandCategory).then(
						CommandManager.literal(NAME).then(CommandManager.argument("season", IntegerArgumentType.integer()).executes(
								ctx -> execute(ctx, ctx.getSource())).requires(
								(ctx) -> Permissions.check(ctx, MOD_ID + ".commands." + NAME, PERMISSION_LEVEL)))));
	}

	public static int execute(CommandContext<ServerCommandSource> commandContext, ServerCommandSource source) {
		setSeason(source.getServer(), IntegerArgumentType.getInteger(commandContext, "season"));

		return 0;
	}
}
