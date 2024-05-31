package io.github.steveplays28.simpleseasons.state;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import org.jetbrains.annotations.NotNull;

// TODO: Make SeasonTracker be per world

/**
 * Tracks the season state of a world. Self-contained.
 */
public abstract class SeasonTracker {
	private final @NotNull SeasonProgress seasonProgress = new SeasonProgress();

	private @NotNull Seasons season = Seasons.SPRING;

	public enum Seasons {
		SPRING, SUMMER, FALL, WINTER;

		private final int value;

		Seasons() {
			this.value = ordinal();
		}

		public static @NotNull String getName(int value) {
			for (Seasons season : Seasons.values()) {
				if (season.value == value) {
					return season.name();
				}
			}

			// Not found
			return "season not found";
		}

		public int getId() {
			return value;
		}

		public @NotNull Seasons getNext() {
			var nextSeasonId = this.getId() + 1;
			if (nextSeasonId >= Seasons.values().length) {
				nextSeasonId = 0;
			}

			return Seasons.of(nextSeasonId);
		}

		/**
		 * Gets the season from an ID.
		 *
		 * @param season The season's ID. Must be within the bounds of {@link Seasons}.
		 * @return The season at the requested ID in {@link Seasons}.
		 */
		public static @NotNull Seasons of(int season) {
			return Seasons.values()[season];
		}

		/**
		 * Gets the season from either a {@link String} ID or from a {@link String} name.
		 *
		 * @param season Either the season's {@link String} ID or the season's {@link String} name. Must be within the bounds of {@link Seasons}.
		 * @return The season at the requested ID in {@link Seasons}.
		 */
		@SuppressWarnings("ForLoopReplaceableByForEach")
		public static @NotNull Seasons parse(@NotNull String season) {
			var seasons = Seasons.values();
			Integer seasonId = null;
			try {
				seasonId = Integer.parseInt(season);
			} catch (NumberFormatException e) {
				for (int i = 0; i < seasons.length; i++) {
					var seasonValue = seasons[i];
					if (seasonValue.name().equalsIgnoreCase(season)) {
						seasonId = seasonValue.getId();
					}
				}

				if (seasonId == null) {
					SimpleSeasons.LOGGER.error(
							"Exception thrown while trying to parse a String season into a SeasonTracker.Seasons value:\n", e);
					return seasons[0];
				}
			}

			return seasons[seasonId];
		}
	}

	public @NotNull Seasons getSeason() {
		return season;
	}

	public void setSeason(Seasons season) {
		this.season = season;
		this.seasonProgress.resetProgress();


	}

	public void setSeason(int seasonId) {
		if (seasonId > SeasonTracker.Seasons.WINTER.ordinal() || seasonId < SeasonTracker.Seasons.SPRING.ordinal()) {
			SimpleSeasons.LOGGER.error(
					"Error occurred while setting the season: seasonId is out of bounds. The seasonId can only be between 0-3 (inclusive), given: {}.\n{}",
					seasonId, new Exception().getStackTrace()
			);
			return;
		}

		this.season = Seasons.of(seasonId);
		this.seasonProgress.resetProgress();
	}

	public void incrementSeason() {
		if (getSeason().getId() >= Seasons.values().length - 1) {
			setSeason(0);
			return;
		}

		setSeason(getSeason().getNext());
	}

	/**
	 * Gets the progress of the current season.
	 *
	 * @return The progress of the current season, as a percentage between 0-1.
	 */
	public float getSeasonProgress() {
		return seasonProgress.getProgress();
	}

	/**
	 * Sets the progress of the current season.
	 *
	 * @param seasonProgress The progress of the current season, as a percentage between 0-1.
	 */
	public void setSeasonProgress(float seasonProgress) {
		if (seasonProgress >= 1f) {
			incrementSeason();
			return;
		}

		if (seasonProgress < 0f) {
			throw new IllegalArgumentException("Error occurred while setting the season's progress: seasonProgress < 0f.");
		}

		this.seasonProgress.setProgress(seasonProgress);
	}
}
