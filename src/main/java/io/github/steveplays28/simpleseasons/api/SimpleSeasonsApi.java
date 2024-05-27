package io.github.steveplays28.simpleseasons.api;

import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class SimpleSeasonsApi {
	public static SeasonTracker.Seasons getSeason(@NotNull World world) {
		if (world.isClient()) {
			return SimpleSeasonsClient.seasonTracker.getSeason();
		} else {
			return SimpleSeasons.getSeason();
		}
	}
}
