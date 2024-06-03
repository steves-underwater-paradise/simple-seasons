package io.github.steveplays28.simpleseasons.client.util.season.color;

import io.github.steveplays28.simpleseasons.client.registry.SeasonColorRegistries;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SeasonColorUtil {
	public static final @NotNull Color FALLBACK_SEASON_COLOR = new Color(129, 192, 62);
	public static final int FALLBACK_SEASON_COLOR_PRECALCULATED = FALLBACK_SEASON_COLOR.toInt();

	// TODO: Move into SimpleSeasonsClientApi
	// TODO: Add a BlockPos->Color cache
	public static @Nullable Color getBlockSeasonColor(@NotNull Identifier blockIdentifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress) {
		return getSeasonColor(
				blockIdentifier, biomeRegistryEntry, season, seasonProgress, SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY);
	}

	// TODO: Move into SimpleSeasonsClientApi
	public static @Nullable Color getItemSeasonColor(@NotNull Identifier itemIdentifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress) {
		return getSeasonColor(
				itemIdentifier, biomeRegistryEntry, season, seasonProgress, SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY);
	}

	private static @Nullable Color getSeasonColor(@NotNull Identifier identifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress, @NotNull SimpleRegistry<@NotNull Map<@NotNull Identifier, @NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color>>> registry) {
		var biomesSeasonColors = registry.get(identifier);
		if (biomesSeasonColors == null) {
			return null;
		}

		var biomeRegistryKey = biomeRegistryEntry.getKey();
		// Check if the biome identifier exists in the registry
		// Use the biome identifier to return the correct season color
		if (biomeRegistryKey.isPresent()) {
			var biomeIdentifier = biomeRegistryKey.get().getValue();
			if (biomesSeasonColors.containsKey(biomeIdentifier)) {
				var seasonColors = biomesSeasonColors.get(biomeRegistryKey.get().getValue());
				var seasonColor = seasonColors.get(season);
				var nextSeasonColor = seasonColors.get(season.getNext());
				return seasonColor.lerp(nextSeasonColor, seasonProgress);
			}
		}

		// Otherwise, look if the biome is included in a biome tag
		// Use the biome tag to return the correct season color
		for (var potentialBiomeTag : biomesSeasonColors.keySet()) {
			if (!biomeRegistryEntry.isIn(TagKey.of(BiomeTags.IS_OVERWORLD.registry(), potentialBiomeTag))) {
				continue;
			}

			var seasonColors = biomesSeasonColors.get(potentialBiomeTag);
			var seasonColor = seasonColors.get(season);
			var nextSeasonColor = seasonColors.get(season.getNext());
			return seasonColor.lerp(nextSeasonColor, seasonProgress);
		}

		return null;
	}
}
