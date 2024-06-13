package io.github.steveplays28.simpleseasons.client.util.season.color;

import io.github.steveplays28.simpleseasons.client.registry.SeasonColorRegistries;
import io.github.steveplays28.simpleseasons.client.resource.json.BlockItemBiomeSeasonColors;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class SeasonColorUtil {
	public static final @NotNull Color FALLBACK_SEASON_COLOR = new Color(129, 192, 62);
	public static final int FALLBACK_SEASON_COLOR_PRECALCULATED = FALLBACK_SEASON_COLOR.toInt();

	// TODO: Move into SimpleSeasonsClientApi
	public static @Nullable Color getBlockSeasonColor(@NotNull Identifier blockIdentifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress) {
		return getSeasonColor(
				blockIdentifier, biomeRegistryEntry, season, seasonProgress, SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY);
	}

	// TODO: Move into SimpleSeasonsClientApi
	public static @Nullable Color getItemSeasonColor(@NotNull Identifier itemIdentifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress) {
		return getSeasonColor(
				itemIdentifier, biomeRegistryEntry, season, seasonProgress, SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY);
	}

	private static @Nullable Color getSeasonColor(@NotNull Identifier identifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress, @NotNull Map<Identifier, BlockItemBiomeSeasonColors> registry) {
		@Nullable var blockItemBiomeSeasonColors = registry.get(identifier);
		if (blockItemBiomeSeasonColors == null) {
			return null;
		}

		@Nullable var biomeSeasonColors = blockItemBiomeSeasonColors.getBiomeSeasonColors();
		if (biomeSeasonColors == null) {
			return null;
		}

		@NotNull var biomeRegistryKey = biomeRegistryEntry.getKey();
		@NotNull Optional<BlockItemBiomeSeasonColors.BiomeSeasonColors> firstBiomeSeasonColorsMatch = Optional.empty();
		if (biomeRegistryKey.isPresent()) {
			// Check if the biome identifier exists in the registry
			// Use the biome identifier to return the correct season color
			@NotNull var biomeIdentifier = biomeRegistryKey.get().getValue();
			firstBiomeSeasonColorsMatch = biomeSeasonColors.stream().filter(
					potentialBiomeSeasonColors -> {
						if (potentialBiomeSeasonColors == null) {
							return false;
						}

						var potentialBiomeIdentifier = potentialBiomeSeasonColors.getIdentifier();
						if (potentialBiomeIdentifier == null) {
							return false;
						}

						return potentialBiomeSeasonColors.getIdentifier().equals(biomeIdentifier);
					}
			).findFirst();
		}

		if (firstBiomeSeasonColorsMatch.isEmpty()) {
			// Otherwise, look if the biome is included in a biome tag
			// Use the biome tag to return the correct season color
			firstBiomeSeasonColorsMatch = biomeSeasonColors.stream().filter(
					potentialBiomeTagSeasonColors -> biomeRegistryEntry.isIn(
							TagKey.of(BiomeTags.IS_OVERWORLD.registry(), potentialBiomeTagSeasonColors.getIdentifier()))).findFirst();
		}

		if (firstBiomeSeasonColorsMatch.isEmpty()) {
			return null;
		}

		@Nullable var seasonColors = firstBiomeSeasonColorsMatch.get().getSeasonColors();
		if (seasonColors == null) {
			return null;
		}

		@Nullable var seasonColor = seasonColors.getColor(season);
		@Nullable var nextSeasonColor = seasonColors.getColor(season.getNext());
		if (seasonColor == null || nextSeasonColor == null) {
			return null;
		}

		return seasonColor.lerp(nextSeasonColor, seasonProgress);
	}
}
