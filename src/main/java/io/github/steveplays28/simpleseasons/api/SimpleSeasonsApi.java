package io.github.steveplays28.simpleseasons.api;

import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class SimpleSeasonsApi {
	/**
	 * Gets the {@link SeasonTracker.Seasons} of the specified {@link World}.
	 *
	 * @param world The {@link World} to get the {@link SeasonTracker.Seasons} from.
	 * @return The {@link SeasonTracker.Seasons} of the specified {@link World}.
	 */
	public static SeasonTracker.@NotNull Seasons getSeason(@NotNull World world) {
		if (world.isClient()) {
			return SimpleSeasonsClient.seasonTracker.getSeason();
		} else {
			return SimpleSeasons.getSeason();
		}
	}

	/**
	 * Gets the progress of the {@link SeasonTracker.Seasons} of the specified {@link World}.
	 * A value between 0-1.
	 *
	 * @param world The {@link World} to get the progress of the {@link SeasonTracker.Seasons} from.
	 * @return The progress of the {@link SeasonTracker.Seasons} of the specified {@link World}.
	 */
	public static float getSeasonProgress(@NotNull World world) {
		if (world.isClient()) {
			return SimpleSeasonsClient.seasonTracker.getSeasonProgress();
		} else {
			return SimpleSeasons.getSeasonProgress();
		}
	}
}
