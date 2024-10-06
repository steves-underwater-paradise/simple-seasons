package io.github.steveplays28.simpleseasons.server.command.season;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class SeasonArgumentType extends EnumArgumentType<SeasonTracker.Seasons> {
	public static final String NAME = "season";

	SeasonArgumentType() {
		super(SeasonTracker.Seasons.CODEC, SeasonTracker.Seasons::values);
	}

	public static void register() {
		ArgumentTypeRegistry.registerArgumentType(
				new Identifier(SimpleSeasons.MOD_ID, NAME),
				SeasonArgumentType.class,
				ConstantArgumentSerializer.of(SeasonArgumentType::new)
		);
	}
}
