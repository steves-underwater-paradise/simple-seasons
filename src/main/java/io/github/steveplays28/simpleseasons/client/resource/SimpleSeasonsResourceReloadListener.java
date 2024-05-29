package io.github.steveplays28.simpleseasons.client.resource;

import com.google.gson.JsonParser;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.registry.SeasonColorsRegistries;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

public class SimpleSeasonsResourceReloadListener implements SimpleResourceReloadListener<Void> {
	private static final String JSON_FILE_SUFFIX = ".json";
	private static final String SEASON_COLORS_FOLDER_NAME = "season_colors";
	private static final String BLOCK_FOLDER_NAME = "block";
	private static final String ITEM_FOLDER_NAME = "item";

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
			loadBlockSeasonColors(resourceManager);
			loadItemSeasonColors(resourceManager);
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

	private void loadBlockSeasonColors(@NotNull ResourceManager resourceManager) {
		var dataPackJsonFiles = resourceManager.findResources(
				String.format("%s/%s", SEASON_COLORS_FOLDER_NAME, BLOCK_FOLDER_NAME),
				identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX)
		);

		for (var dataPackJsonFile : dataPackJsonFiles.entrySet()) {
			var splitDataPackJsonFilePath = dataPackJsonFile.getKey().getPath().replace(".json", "").split("/");
			var blockId = new Identifier(
					splitDataPackJsonFilePath[splitDataPackJsonFilePath.length - 2],
					splitDataPackJsonFilePath[splitDataPackJsonFilePath.length - 1]
			);
			if (SeasonColorsRegistries.BLOCK_SEASON_COLORS_REGISTRY.containsId(blockId)) {
				continue;
			}

			try {
				var dataPackJson = JsonParser.parseReader(dataPackJsonFile.getValue().getReader()).getAsJsonObject();
				var dataPackJsonSeasonColors = dataPackJson.asMap();
				@NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color> blockSeasonColors = new HashMap<>();
				for (var dataPackJsonSeasonColor : dataPackJsonSeasonColors.entrySet()) {
					blockSeasonColors.put(
							SeasonTracker.Seasons.parse(dataPackJsonSeasonColor.getKey()),
							Color.parse(dataPackJsonSeasonColor.getValue().getAsString())
					);
				}

				Registry.register(
						SeasonColorsRegistries.BLOCK_SEASON_COLORS_REGISTRY, blockId,
						blockSeasonColors
				);
			} catch (IOException | IllegalArgumentException e) {
				SimpleSeasons.LOGGER.error("Exception thrown while reading a datapack JSON file:\n", e);
			}
		}
	}

	private void loadItemSeasonColors(@NotNull ResourceManager resourceManager) {
		var dataPackJsonFiles = resourceManager.findResources(
				String.format("%s/%s", SEASON_COLORS_FOLDER_NAME, ITEM_FOLDER_NAME),
				identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX)
		);

		for (var dataPackJsonFile : dataPackJsonFiles.entrySet()) {
			var itemId = dataPackJsonFile.getKey();
			if (SeasonColorsRegistries.ITEM_SEASON_COLORS_REGISTRY.containsId(itemId)) {
				continue;
			}

			try {
				var dataPackJson = JsonParser.parseReader(dataPackJsonFile.getValue().getReader()).getAsJsonObject();
				var dataPackJsonSeasonColors = dataPackJson.asMap();
				@NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color> itemSeasonColors = new HashMap<>();
				for (var dataPackJsonSeasonColor : dataPackJsonSeasonColors.entrySet()) {
					itemSeasonColors.put(
							SeasonTracker.Seasons.parse(dataPackJsonSeasonColor.getKey()),
							Color.parse(dataPackJsonSeasonColor.getValue().getAsString())
					);
				}

				Registry.register(
						SeasonColorsRegistries.ITEM_SEASON_COLORS_REGISTRY, itemId,
						itemSeasonColors
				);
			} catch (IOException | IllegalArgumentException e) {
				SimpleSeasons.LOGGER.error("Exception thrown while reading a datapack JSON file:\n", e);
			}
		}
	}
}