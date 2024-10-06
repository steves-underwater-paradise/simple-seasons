package io.github.steveplays28.simpleseasons.registry.tag.biome;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAMESPACE;

public class SimpleSeasonsBiomeTags {
	public static final TagKey<Biome> HAS_WET_AND_DRY_SEASONS = TagKey.of(
			RegistryKeys.BIOME, Identifier.of(MOD_NAMESPACE, "has_wet_and_dry_seasons"));
}
