package io.github.steveplays28.simpleseasons.client.util.season.color;

import io.github.steveplays28.simpleseasons.client.registry.SeasonColorsRegistries;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

public class SeasonColorUtil {
	public static final @NotNull Color FALLBACK_SEASON_COLOR = new Color(129, 192, 62);

	// TODO: Move into SimpleSeasonsClientApi
	// TODO: Biome support
	public static @NotNull Color getBlockSeasonColor(@NotNull Identifier blockIdentifier, @NotNull Biome biome, SeasonTracker.@NotNull Seasons season, float seasonProgress, @NotNull Color fallbackSeasonColor) {
		var blockSeasonColors = SeasonColorsRegistries.BLOCK_SEASON_COLORS_REGISTRY.get(blockIdentifier);
		if (blockSeasonColors == null) {
			return fallbackSeasonColor;
		}

		var blockSeasonColor = blockSeasonColors.get(season);
		var blockNextSeasonColor = blockSeasonColors.get(season.getNext());
		return blockSeasonColor.lerp(blockNextSeasonColor, seasonProgress);
	}

	public static @NotNull Color getItemSeasonColor(@NotNull Identifier itemIdentifier, SeasonTracker.@NotNull Seasons season, float seasonProgress, @NotNull Color fallbackSeasonColor) {
		var blockSeasonColors = SeasonColorsRegistries.ITEM_SEASON_COLORS_REGISTRY.get(itemIdentifier);
		if (blockSeasonColors == null) {
			return fallbackSeasonColor;
		}

		var blockSeasonColor = blockSeasonColors.get(season);
		var blockNextSeasonColor = blockSeasonColors.get(season.getNext());
		return blockSeasonColor.lerp(blockNextSeasonColor, seasonProgress);
	}
}
