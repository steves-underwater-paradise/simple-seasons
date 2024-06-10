package io.github.steveplays28.simpleseasons.api;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import io.github.steveplays28.simpleseasons.registry.tag.biome.SimpleSeasonsBiomeTags;
import io.github.steveplays28.simpleseasons.server.api.world.registry.state.ServerWorldSeasonTrackerRegistry;
import io.github.steveplays28.simpleseasons.server.util.time.TimeUtil;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class SimpleSeasonsApi {
	private static @Nullable Long SEASON_LENGTH_SECONDS_CACHED;

	/**
	 * Checks if the specified {@link World} has seasons.
	 * Any worlds with unfixed time will have seasons.
	 *
	 * @param world The {@link World} to check.
	 * @return If the specified {@link World} has seasons.
	 */
	public static boolean worldHasSeasons(@NotNull World world) {
		return !world.getDimension().hasFixedTime();
	}

	/**
	 * Checks if the specified {@link Biome} has wet and dry seasons.
	 * This is done by checking if the specified {@link Biome} is in the biome tag {@code simple_seasons:has_wet_and_dry_seasons}.
	 *
	 * @param biome The {@link Biome} to check for wet and dry seasons.
	 * @return If the specified {@link Biome} has wet and dry seasons.
	 */
	public static boolean biomeHasWetAndDrySeasons(@NotNull RegistryEntry<Biome> biome) {
		return biome.isIn(SimpleSeasonsBiomeTags.HAS_WET_AND_DRY_SEASONS);
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

	/**
	 * Gets the length of a season, in seconds.
	 * This value is cached as {@link SimpleSeasonsApi#SEASON_LENGTH_SECONDS_CACHED}.
	 *
	 * @return The length of a season, in seconds.
	 */
	public static long getSeasonLengthSeconds() {
		if (SEASON_LENGTH_SECONDS_CACHED == null) {
			SEASON_LENGTH_SECONDS_CACHED = Math.round(
					TimeUtil.getDayNightCycleLengthSeconds() * (TimeUtil.getYearLengthDays() / SeasonTracker.Seasons.values().length));
		}

		return SEASON_LENGTH_SECONDS_CACHED;
	}
}
