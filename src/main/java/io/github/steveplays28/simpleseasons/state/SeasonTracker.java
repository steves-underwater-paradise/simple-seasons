package io.github.steveplays28.simpleseasons.state;

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

		/**
		 * Gets the season from an ID.
		 *
		 * @param season The season's ID. Must be within the bounds of the <code>Seasons</code> enum.
		 * @return The season at the requested ID in the <code>Seasons</code> enum.
		 */
		public static Seasons of(int season) {
			return Seasons.values()[season];
		}
	}

	public @NotNull Seasons getSeason() {
		return season;
	}

	public void setSeason(int seasonId) {
		this.season = Seasons.of(seasonId);
		this.seasonProgress.resetProgress();
	}

	public void incrementSeason() {
		if (getSeason().getId() >= Seasons.values().length - 1) {
			setSeason(0);
			return;
		}

		setSeason(getSeason().getId() + 1);
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
		}

		if (seasonProgress < 0f) {
			throw new IllegalArgumentException();
		}

		this.seasonProgress.setProgress(seasonProgress);
	}
}
