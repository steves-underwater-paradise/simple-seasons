package io.github.steveplays28.simpleseasons.state.world;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks the season state.
 */
public abstract class SeasonTracker {
	private final @NotNull SeasonProgress seasonProgress = new SeasonProgress();

	private @NotNull Seasons season = Seasons.SPRING;

	public enum Seasons implements StringIdentifiable {
		SPRING("spring"), SUMMER("summer"), FALL("fall"), WINTER("winter");

		public static final @NotNull com.mojang.serialization.Codec<Seasons> CODEC = StringIdentifiable.createCodec(Seasons::values);

		private final @NotNull String name;
		private final int value;

		Seasons(@NotNull String name) {
			this.name = name;
			this.value = ordinal();
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

		@Override
		public @NotNull String asString() {
			return name;
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
