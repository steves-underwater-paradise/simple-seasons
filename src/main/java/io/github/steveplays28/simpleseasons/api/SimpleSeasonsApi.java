package io.github.steveplays28.simpleseasons.api;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import io.github.steveplays28.simpleseasons.server.api.world.registry.state.ServerWorldSeasonTrackerRegistry;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class SimpleSeasonsApi {
	public static boolean worldHasSeasons(@NotNull World world) {
		return !world.getDimension().hasFixedTime();
	}

	/**
	 * Gets the {@link SeasonTracker.Seasons} of the specified {@link World}.
	 *
	 * @param world The {@link World} to get the {@link SeasonTracker.Seasons} from.
	 * @return The {@link SeasonTracker.Seasons} of the specified {@link World}.
	 */
	public static SeasonTracker.@NotNull Seasons getSeason(@NotNull World world) {
		if (world.isClient()) {
			return SimpleSeasonsClient.SEASON_TRACKER.getSeason();
		}

		@Nullable var seasonTracker = ServerWorldSeasonTrackerRegistry.get(world.getRegistryKey().getValue());
		if (seasonTracker == null) {
			SimpleSeasons.LOGGER.error(
					"Error occurred while getting the season of world {}: the world does not have a registered season tracker.\n{}",
					world, new Exception().getStackTrace()
			);
			return SeasonTracker.Seasons.of(0);
		}
		return seasonTracker.getSeason();
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
		}

		@Nullable var seasonTracker = ServerWorldSeasonTrackerRegistry.get(world.getRegistryKey().getValue());
		if (seasonTracker == null) {
			SimpleSeasons.LOGGER.error(
					"Error occurred while getting the season progress of world {}: the world does not have a registered season tracker.\n{}",
					world, new Exception().getStackTrace()
			);
			return 0f;
		}
		return seasonTracker.getSeasonProgress();
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

		@Nullable var seasonTracker = ServerWorldSeasonTrackerRegistry.get(world.getRegistryKey().getValue());
		if (seasonTracker == null) {
			SimpleSeasons.LOGGER.error(
					"Error occurred while setting the season of world {}: the world does not have a registered season tracker.\n{}",
					world, new Exception().getStackTrace()
			);
			return;
		}

		seasonTracker.setSeason(season);
	}
}
