package io.github.steveplays28.simpleseasons.client.util.season.color;

import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SeasonColorUtil {
	// Per-season color additions
	public static final Color SPRING_COLOR_ADDITION = new Color(255 / 3, 255 / 3, 0);
	public static final Color SUMMER_COLOR_ADDITION = new Color(0, 0, 0);
	public static final Color FALL_COLOR_ADDITION = new Color(255 / 3, 0, 0);
	public static final Color WINTER_COLOR_ADDITION = new Color(255 / 2, 255 / 2, 255 / 2);
	// Dry biomes
	public static final Color HOT_DRY_BIOMES_COLOR_ADDITION = new Color(120, 0, 0);
	public static final Color WET_DRY_BIOMES_COLOR_ADDITION = new Color(50, 50, 0);
	// Seasons color map
	public static final Map<Integer, Color> SEASONS_COLOR_ADDITIONS_MAP = Map.of(
			SeasonTracker.Seasons.SPRING.ordinal(),
			SPRING_COLOR_ADDITION, SeasonTracker.Seasons.SUMMER.ordinal(), SUMMER_COLOR_ADDITION, SeasonTracker.Seasons.FALL.ordinal(),
			FALL_COLOR_ADDITION, SeasonTracker.Seasons.WINTER.ordinal(), WINTER_COLOR_ADDITION
	);
	// Dry biomes
	public static final Map<Integer, Color> SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP = Map.of(SeasonTracker.Seasons.SPRING.ordinal(),
			HOT_DRY_BIOMES_COLOR_ADDITION, SeasonTracker.Seasons.SUMMER.ordinal(), HOT_DRY_BIOMES_COLOR_ADDITION,
			SeasonTracker.Seasons.FALL.ordinal(), WET_DRY_BIOMES_COLOR_ADDITION, SeasonTracker.Seasons.WINTER.ordinal(),
			WET_DRY_BIOMES_COLOR_ADDITION
	);

	public static Color getSeasonColorAddition(SeasonTracker.@NotNull Seasons season, float seasonProgress) {
		return SEASONS_COLOR_ADDITIONS_MAP.get(season.getId()).lerp(SEASONS_COLOR_ADDITIONS_MAP.get(season.getNext().getId()), seasonProgress);
	}

	public static Color getSeasonColorAddition(SeasonTracker.@NotNull Seasons season, float seasonProgress, boolean isDryBiome) {
		if (isDryBiome) {
			return SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP.get(season.getId()).lerp(SEASONS_COLOR_ADDITIONS_MAP.get(season.getNext().getId()), seasonProgress);
		}

		return getSeasonColorAddition(season, seasonProgress);
	}
}
