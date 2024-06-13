package io.github.steveplays28.simpleseasons.client.resource.json;

import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockItemBiomeSeasonColors {
	@SuppressWarnings("unused")
	private @Nullable List<BiomeSeasonColors> biomes;

	private transient @Nullable Identifier identifier;

	public BlockItemBiomeSeasonColors() {}

	public BlockItemBiomeSeasonColors(@Nullable List<BiomeSeasonColors> biomes) {
		this.biomes = biomes;
	}

	public @Nullable List<BiomeSeasonColors> getBiomeSeasonColors() {
		return biomes;
	}

	/**
	 * @return The {@link Identifier} of the {@link net.minecraft.block.Block} the {@link BiomeSeasonColors} apply to.
	 */
	public @Nullable Identifier getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the {@link Identifier} of the {@link net.minecraft.block.Block} the {@link BiomeSeasonColors} apply to.
	 */
	public void setIdentifier(@NotNull Identifier identifier) {
		this.identifier = identifier;
	}

	public static class BiomeSeasonColors {
		@SuppressWarnings("unused")
		private @Nullable String identifier;
		@SuppressWarnings("unused")
		private @Nullable SeasonColors seasonColors;

		private transient @Nullable Identifier identifierCached;

		/**
		 * @return The {@link Identifier} of the {@link net.minecraft.world.biome.Biome} the {@link SeasonColors} apply to.
		 */
		public @Nullable Identifier getIdentifier() {
			if (identifier == null) {
				return null;
			}
			if (identifierCached == null) {
				// Remove the biome tag prefix if it exists
				// # is an illegal character in identifiers
				if (identifier.startsWith("#")) {
					identifierCached = new Identifier(identifier.substring(1));
					return identifierCached;
				}

				identifierCached = new Identifier(identifier);
			}

			return identifierCached;
		}

		public @Nullable SeasonColors getSeasonColors() {
			return seasonColors;
		}

		public static class SeasonColors {
			@SuppressWarnings("unused")
			private @Nullable String spring;
			@SuppressWarnings("unused")
			private @Nullable String summer;
			@SuppressWarnings("unused")
			private @Nullable String fall;
			@SuppressWarnings("unused")
			private @Nullable String winter;

			private transient @Nullable Color springCached;
			private transient @Nullable Color summerCached;
			private transient @Nullable Color fallCached;
			private transient @Nullable Color winterCached;

			public @Nullable Color getSpringColor() {
				if (spring == null) {
					return null;
				}
				if (springCached == null) {
					springCached = Color.parse(spring);
				}

				return springCached;
			}

			public @Nullable Color getSummerColor() {
				if (summer == null) {
					return null;
				}
				if (summerCached == null) {
					summerCached = Color.parse(summer);
				}

				return summerCached;
			}

			public @Nullable Color getFallColor() {
				if (fall == null) {
					return null;
				}
				if (fallCached == null) {
					fallCached = Color.parse(fall);
				}

				return fallCached;
			}

			public @Nullable Color getWinterColor() {
				if (winter == null) {
					return null;
				}
				if (winterCached == null) {
					winterCached = Color.parse(winter);
				}

				return winterCached;
			}

			public @Nullable Color getColor(SeasonTracker.@NotNull Seasons season) {
				return switch (season) {
					case SPRING -> getSpringColor();
					case SUMMER -> getSummerColor();
					case FALL -> getFallColor();
					case WINTER -> getWinterColor();
				};
			}
		}
	}
}
