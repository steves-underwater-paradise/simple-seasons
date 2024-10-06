package io.github.steveplays28.simpleseasons.server.command.season;

import com.mojang.brigadier.context.CommandContext;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SeasonArgumentType extends EnumArgumentType<SeasonTracker.Seasons> {
	private static final String NAME = "season";

	SeasonArgumentType() {
		super(SeasonTracker.Seasons.CODEC, SeasonTracker.Seasons::values);
	}

	public static void register() {
		ArgumentTypeRegistry.registerArgumentType(
				Identifier.of(SimpleSeasons.MOD_ID, NAME),
				SeasonArgumentType.class,
				ConstantArgumentSerializer.of(SeasonArgumentType::new)
		);
	}

	public static @NotNull SeasonTracker.Seasons getArgument(@NotNull CommandContext<ServerCommandSource> commandContext) {
		return commandContext.getArgument(NAME, SeasonTracker.Seasons.class);
	}
}
