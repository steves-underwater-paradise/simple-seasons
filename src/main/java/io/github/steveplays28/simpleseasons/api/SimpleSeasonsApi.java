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
			return SimpleSeasonsClient.SEASON_TRACKER.getSeason();
		} else {
			return SimpleSeasons.SEASON_TRACKER.getSeason();
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
			return SimpleSeasonsClient.SEASON_TRACKER.getSeasonProgress();
		} else {
			return SimpleSeasons.SEASON_TRACKER.getSeasonProgress();
		}
	}

	/**
	 * Sets the {@link SeasonTracker.Seasons} of the specified {@link World}.
	 *
	 * @param world  The {@link World} to set the {@link SeasonTracker.Seasons} in.
	 * @param season The {@link SeasonTracker.Seasons} that should be set in the specified {@link World}.
	 */
	public static void setSeason(@NotNull World world, SeasonTracker.@NotNull Seasons season) {
		if (world.isClient()) {
			throw new IllegalArgumentException(
					"Passed in world argument is a client world, but the season can only be changed from the serverside.");
		}

		SimpleSeasons.SEASON_TRACKER.setSeason(season);
	}
}
