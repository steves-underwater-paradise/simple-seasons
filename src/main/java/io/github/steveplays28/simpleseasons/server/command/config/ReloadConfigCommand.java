package io.github.steveplays28.simpleseasons.server.command.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.steveplays28.simpleseasons.config.SimpleSeasonsConfig;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAME;
import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAMESPACE;
import static io.github.steveplays28.simpleseasons.server.command.config.ConfigCommandCategory.CONFIG_COMMAND_CATEGORY;

// TODO: Refactor into a base class that can be overridden and extended
//  This reduces duplicate code
public class ReloadConfigCommand {
	public static final @NotNull String NAME = "reload";
	public static final int PERMISSION_LEVEL = 4;

	public static @NotNull LiteralArgumentBuilder<ServerCommandSource> register() {
		return CommandManager.literal(MOD_NAMESPACE).then(
				CommandManager.literal(CONFIG_COMMAND_CATEGORY).then(CommandManager.literal(NAME).executes(
						ctx -> execute(ctx, ctx.getSource())).requires(
						(ctx) -> Permissions.check(
								ctx,
								String.format("%s.commands.%s.%s", MOD_NAMESPACE, CONFIG_COMMAND_CATEGORY, NAME), PERMISSION_LEVEL
						)
				)));
	}

	@SuppressWarnings("unused")
	public static int execute(@NotNull CommandContext<ServerCommandSource> commandContext, @NotNull ServerCommandSource source) {
		SimpleSeasonsConfig.HANDLER.load();

		source.sendMessage(Text.literal(String.format("Reloaded %s' config from the config file in storage.", MOD_NAME)));
		return 0;
	}
}
