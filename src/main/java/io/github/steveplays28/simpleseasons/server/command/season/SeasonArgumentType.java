package io.github.steveplays28.simpleseasons.server.command.season;

import com.mojang.brigadier.context.CommandContext;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class SeasonArgumentType extends EnumArgumentType<SeasonTracker.Seasons> {


	private SeasonArgumentType() {
		super(SeasonTracker.Seasons.CODEC, SeasonTracker.Seasons::values);
	}

	public static SeasonArgumentType season() {
		return new SeasonArgumentType();
	}

	public static SeasonTracker.Seasons getSeason(CommandContext<ServerCommandSource> context, String id) {
		return context.getArgument(id, SeasonTracker.Seasons.class);
	}
}
