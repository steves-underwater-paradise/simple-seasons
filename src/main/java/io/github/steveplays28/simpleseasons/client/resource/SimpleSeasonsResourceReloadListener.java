package io.github.steveplays28.simpleseasons.client.resource;

import com.google.gson.JsonParser;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.extension.world.ClientWorldExtension;
import io.github.steveplays28.simpleseasons.client.registry.SeasonColorRegistries;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsResourceReloadListener implements SimpleResourceReloadListener<Void> {
	private static final String JSON_FILE_SUFFIX = ".json";
	private static final String SEASON_COLORS_FOLDER_NAME = "season_colors";
	private static final String BLOCK_FOLDER_NAME = "block";
	private static final String ITEM_FOLDER_NAME = "item";

	private static boolean isFirstLoad = true;

	/**
	 * Asynchronously process and load resource-based data. The code
	 * must be thread-safe and not modify game state!
	 *
	 * @param resourceManager The {@link ResourceManager} used during reloading.
	 * @param profiler        The {@link Profiler} which may be used for this stage.
	 * @param executor        The {@link Executor} which should be used for this stage.
	 * @return A {@link CompletableFuture} representing the "data loading" stage.
	 */
	@Override
	public CompletableFuture<Void> load(@NotNull ResourceManager resourceManager, @NotNull Profiler profiler, @NotNull Executor executor) {
		return CompletableFuture.runAsync(() -> {
			loadSeasonColors(resourceManager, BLOCK_FOLDER_NAME, SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY);
			loadSeasonColors(resourceManager, ITEM_FOLDER_NAME, SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY);
			isFirstLoad = false;
		});
	}

	/**
	 * Synchronously apply loaded data to the game state.
	 *
	 * @param data            The data that is being applied.
	 * @param resourceManager The {@link ResourceManager} used during reloading.
	 * @param profiler        The {@link Profiler} which may be used for this stage.
	 * @param executor        The {@link Executor} which should be used for this stage.
	 * @return A {@link CompletableFuture} representing the "data applying" stage.
	 */
	@Override
	public CompletableFuture<Void> apply(@NotNull Void data, @NotNull ResourceManager resourceManager, @NotNull Profiler profiler, @NotNull Executor executor) {
		return CompletableFuture.completedFuture(null);
	}

	/**
	 * @return The unique {@link Identifier} of this listener.
	 */
	@Override
	public Identifier getFabricId() {
		return new Identifier(String.format(MOD_ID), "resource_reload_listener");
	}

	private void loadSeasonColors(@NotNull ResourceManager resourceManager, @NotNull String folderName, @NotNull SimpleRegistry<@NotNull Map<@NotNull Identifier, @NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color>>> registry) {
		var dataPackJsonFiles = resourceManager.findResources(
				String.format("%s/%s", SEASON_COLORS_FOLDER_NAME, folderName),
				identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX)
		);

		for (var dataPackJsonFile : dataPackJsonFiles.entrySet()) {
			var splitDataPackJsonFilePath = dataPackJsonFile.getKey().getPath().replace(".json", "").split("/");
			var identifier = new Identifier(
					splitDataPackJsonFilePath[splitDataPackJsonFilePath.length - 2],
					splitDataPackJsonFilePath[splitDataPackJsonFilePath.length - 1]
			);
			if (registry.containsId(identifier)) {
				continue;
			}
			if (!isFirstLoad) {
				SimpleSeasons.LOGGER.info(
						"Detected changes to season colors, please restart your game for the changes to take effect (cannot modify frozen registries).");
				return;
			}

			try {
				var dataPackJson = JsonParser.parseReader(dataPackJsonFile.getValue().getReader()).getAsJsonObject();
				var dataPackJsonBiomesSeasonColors = dataPackJson.asMap();
				@NotNull Map<@NotNull Identifier, @NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color>> biomesSeasonColors = new HashMap<>();
				for (var dataPackJsonBiomesSeasonColor : dataPackJsonBiomesSeasonColors.entrySet()) {
					@NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color> seasonColors = new HashMap<>();
					for (var dataPackJsonSeasonColor : dataPackJsonBiomesSeasonColor.getValue().getAsJsonObject().entrySet()) {
						seasonColors.put(
								SeasonTracker.Seasons.parse(dataPackJsonSeasonColor.getKey()),
								Color.parse(dataPackJsonSeasonColor.getValue().getAsString())
						);
					}

					var biomeKey = dataPackJsonBiomesSeasonColor.getKey();
					// Remove biome tag prefix if it exists
					// # is an illegal character in identifiers
					if (biomeKey.startsWith("#")) {
						biomeKey = biomeKey.substring(1);
					}

					biomesSeasonColors.put(new Identifier(biomeKey), seasonColors);
				}

				Registry.register(registry, identifier, biomesSeasonColors);
				if (folderName.equals(BLOCK_FOLDER_NAME)) {
					registerBlockColorProvider(identifier);
				} else if (folderName.equals(ITEM_FOLDER_NAME)) {
					registerItemColorProvider(identifier);
				}
			} catch (IOException | IllegalArgumentException e) {
				SimpleSeasons.LOGGER.error("Exception thrown while reading a resource pack JSON file:\n", e);
			}
		}
	}

	private void registerBlockColorProvider(Identifier blockIdentifier) {
		ColorProviderRegistry.BLOCK.register((blockState, blockRenderView, startBlockPos, tintIndex) -> {
			@NotNull var client = MinecraftClient.getInstance();
			@Nullable var clientWorld = client.world;
			if (clientWorld == null || startBlockPos == null) {
				return FoliageColors.getDefaultColor();
			}

			@NotNull var blockPosColorCache = ((ClientWorldExtension) clientWorld).simple_seasons$getColorCache().blockPosColorCache;
			@Nullable var cachedStartBlockSeasonColor = blockPosColorCache.get(startBlockPos);
			if (cachedStartBlockSeasonColor != null) {
				return cachedStartBlockSeasonColor.toInt();
			}

			var biomeBlendedBlockSeasonColor = clientWorld.calculateColor(startBlockPos, (biome, x, z) -> {
				@NotNull var blockPos = new BlockPos((int) Math.round(x), startBlockPos.getY(), (int) Math.round(z));
				// Nullable due to concurrency
				@Nullable var cachedBlockSeasonColor = blockPosColorCache.get(blockPos);
				if (cachedBlockSeasonColor != null) {
					return cachedBlockSeasonColor.toInt();
				}

				@Nullable var blockSeasonColor = SeasonColorUtil.getBlockSeasonColor(
						Registries.BLOCK.getId(clientWorld.getBlockState(blockPos).getBlock()),
						clientWorld.getBiome(blockPos),
						SimpleSeasonsApi.getSeason(clientWorld),
						SimpleSeasonsApi.getSeasonProgress(clientWorld)
				);
				if (blockSeasonColor != null) {
					return blockSeasonColor.toInt();
				}

				@Nullable var startBlockSeasonColor = SeasonColorUtil.getBlockSeasonColor(
						Registries.BLOCK.getId(clientWorld.getBlockState(startBlockPos).getBlock()),
						clientWorld.getBiome(startBlockPos),
						SimpleSeasonsApi.getSeason(clientWorld),
						SimpleSeasonsApi.getSeasonProgress(clientWorld)
				);
				if (startBlockSeasonColor == null) {
					return SeasonColorUtil.FALLBACK_SEASON_COLOR_PRECALCULATED;
				}

				return startBlockSeasonColor.toInt();
			});

			blockPosColorCache.put(startBlockPos, new Color(biomeBlendedBlockSeasonColor));
			return biomeBlendedBlockSeasonColor;
		}, Registries.BLOCK.get(blockIdentifier));
	}

	private void registerItemColorProvider(Identifier itemIdentifier) {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			@NotNull var client = MinecraftClient.getInstance();
			if (client.world == null || client.player == null) {
				throw new IllegalStateException(
						"Error occurred while registering item color providers: client.world == null || client.player == null.");
			}

			@Nullable var itemSeasonColor = SeasonColorUtil.getItemSeasonColor(Registries.ITEM.getId(stack.getItem()),
					client.world.getBiome(client.player.getBlockPos()),
					SimpleSeasonsApi.getSeason(client.world), SimpleSeasonsApi.getSeasonProgress(client.world)
			);
			if (itemSeasonColor == null) {
				return FoliageColors.getDefaultColor();
			}

			return itemSeasonColor.toInt();
		}, Registries.BLOCK.get(itemIdentifier));
	}
}
