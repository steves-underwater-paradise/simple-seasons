package io.github.steveplays28.simpleseasons.client.util.season.color;

import io.github.steveplays28.simpleseasons.client.registry.SeasonColorRegistries;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.NotNull;

public class SeasonColorUtil {
	public static final @NotNull Color FALLBACK_SEASON_COLOR = new Color(129, 192, 62);

	// TODO: Move into SimpleSeasonsClientApi
	public static @NotNull Color getBlockSeasonColor(@NotNull Identifier blockIdentifier, @NotNull RegistryEntry<Biome> biomeRegistryEntry, SeasonTracker.@NotNull Seasons season, float seasonProgress, @NotNull Color fallbackSeasonColor) {
		var blockBiomesSeasonColors = SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY.get(blockIdentifier);
		if (blockBiomesSeasonColors == null) {
			return fallbackSeasonColor;
		}

		var biomeRegistryKey = biomeRegistryEntry.getKey();
		// Check if the biome identifier exists in the registry
		// Use the biome identifier to return the correct season color
		if (biomeRegistryKey.isPresent()) {
			var biomeIdentifier = biomeRegistryKey.get().getValue();
			if (blockBiomesSeasonColors.containsKey(biomeIdentifier)) {
				var blockSeasonColors = blockBiomesSeasonColors.get(biomeRegistryKey.get().getValue());
				var blockSeasonColor = blockSeasonColors.get(season);
				var blockNextSeasonColor = blockSeasonColors.get(season.getNext());
				return blockSeasonColor.lerp(blockNextSeasonColor, seasonProgress);
			}
		}

		// Otherwise, look if the biome is included in a biome tag
		// Use the biome tag to return the correct season color
		for (var potentialBlockBiomeTag : blockBiomesSeasonColors.keySet()) {
			if (!biomeRegistryEntry.isIn(TagKey.of(BiomeTags.IS_OVERWORLD.registry(), potentialBlockBiomeTag))) {
				continue;
			}

			var blockSeasonColors = blockBiomesSeasonColors.get(potentialBlockBiomeTag);
			var blockSeasonColor = blockSeasonColors.get(season);
			var blockNextSeasonColor = blockSeasonColors.get(season.getNext());
			return blockSeasonColor.lerp(blockNextSeasonColor, seasonProgress);
		}

		return fallbackSeasonColor;
	}

	// TODO: Move into SimpleSeasonsClientApi
	// TODO: Potentially add support for biomes and biome tags, instead of hardcoding minecraft:plains
	public static @NotNull Color getItemSeasonColor(@NotNull Identifier itemIdentifier, SeasonTracker.@NotNull Seasons season, float seasonProgress, @NotNull Color fallbackSeasonColor) {
		var itemBiomesSeasonColors = SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY.get(itemIdentifier);
		if (itemBiomesSeasonColors == null) {
			return fallbackSeasonColor;
		}

		var itemSeasonColors = itemBiomesSeasonColors.get(BiomeKeys.PLAINS.getValue());
		var itemSeasonColor = itemSeasonColors.get(season);
		var itemNextSeasonColor = itemSeasonColors.get(season.getNext());
		return itemSeasonColor.lerp(itemNextSeasonColor, seasonProgress);
	}
}
