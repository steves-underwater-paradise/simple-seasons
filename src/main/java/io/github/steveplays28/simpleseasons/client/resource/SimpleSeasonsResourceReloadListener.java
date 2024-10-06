package io.github.steveplays28.simpleseasons.client.resource;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.extension.world.ClientWorldExtension;
import io.github.steveplays28.simpleseasons.client.registry.SeasonColorRegistries;
import io.github.steveplays28.simpleseasons.client.resource.json.BlockItemBiomeSeasonColors;
import io.github.steveplays28.simpleseasons.client.resource.json.BlockItemBiomeSeasonColorsDeserializer;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsResourceReloadListener implements SimpleResourceReloadListener<Pair<@NotNull List<BlockItemBiomeSeasonColors>, @NotNull List<BlockItemBiomeSeasonColors>>> {
	private static final @NotNull Identifier IDENTIFIER = Identifier.of(String.format(MOD_ID), "resource_reload_listener");
	private static final @NotNull String JSON_FILE_SUFFIX = ".json";
	private static final @NotNull String SEASON_COLORS_FOLDER_NAME = "season_colors";
	private static final @NotNull String BLOCK_FOLDER_NAME = "block";
	private static final @NotNull String ITEM_FOLDER_NAME = "item";

	/**
	 * @return The unique {@link Identifier} of this listener.
	 */
	@Override
	public @NotNull Identifier getFabricId() {
		return IDENTIFIER;
	}

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
	public @NotNull CompletableFuture<Pair<@NotNull List<BlockItemBiomeSeasonColors>, @NotNull List<BlockItemBiomeSeasonColors>>> load(@NotNull ResourceManager resourceManager, @NotNull Profiler profiler, @NotNull Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			@NotNull final var blocksSeasonColors = loadSeasonColors(
					resourceManager, BLOCK_FOLDER_NAME, SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY);
			@NotNull final var itemsSeasonColors = loadSeasonColors(
					resourceManager, ITEM_FOLDER_NAME, SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY);
			return new Pair<>(blocksSeasonColors, itemsSeasonColors);
		}, executor);
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
	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Override
	public @NotNull CompletableFuture<Void> apply(@NotNull Pair<@NotNull List<BlockItemBiomeSeasonColors>, @NotNull List<BlockItemBiomeSeasonColors>> data, ResourceManager resourceManager, Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			// TODO: Move into SeasonColorRegistries#register using a client-side resource reload event
			SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY.clear();
			// TODO: Move into SeasonColorRegistries#register using a client-side resource reload event
			SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY.clear();

			@NotNull final var blockBiomeSeasonColorsList = data.getLeft();
			for (int i = 0; i < blockBiomeSeasonColorsList.size(); i++) {
				@NotNull var blockBiomeSeasonColors = blockBiomeSeasonColorsList.get(i);
				@Nullable var blockIdentifier = blockBiomeSeasonColors.getIdentifier();
				if (blockIdentifier == null) {
					continue;
				}

				registerBlockColorProvider(blockIdentifier);
				SeasonColorRegistries.BLOCK_SEASON_COLORS_REGISTRY.put(blockIdentifier, blockBiomeSeasonColors);
			}

			@NotNull final var itemBiomeSeasonColorsList = data.getRight();
			for (int i = 0; i < itemBiomeSeasonColorsList.size(); i++) {
				@NotNull var itemBiomeSeasonColors = itemBiomeSeasonColorsList.get(i);
				@Nullable var itemIdentifier = itemBiomeSeasonColors.getIdentifier();
				if (itemIdentifier == null) {
					continue;
				}

				registerItemColorProvider(itemIdentifier);
				SeasonColorRegistries.ITEM_SEASON_COLORS_REGISTRY.put(itemIdentifier, itemBiomeSeasonColors);
			}

			return null;
		}, executor);
	}

	private @NotNull List<BlockItemBiomeSeasonColors> loadSeasonColors(@NotNull ResourceManager resourceManager, @NotNull String folderName, @NotNull Map<Identifier, BlockItemBiomeSeasonColors> registry) {
		@NotNull final var dataPackJsonFiles = resourceManager.findResources(
				String.format("%s/%s", SEASON_COLORS_FOLDER_NAME, folderName),
				identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX)
		);
		@NotNull List<BlockItemBiomeSeasonColors> blockItemBiomeSeasonColorsList = new ArrayList<>();
		for (@NotNull final var dataPackJsonFile : dataPackJsonFiles.entrySet()) {
			@NotNull final var splitDataPackJsonFilePath = dataPackJsonFile.getKey().getPath().replace(".json", "").split("/");
			@NotNull final var identifier = Identifier.of(
					splitDataPackJsonFilePath[splitDataPackJsonFilePath.length - 2],
					splitDataPackJsonFilePath[splitDataPackJsonFilePath.length - 1]
			);
			if (registry.containsKey(identifier)) {
				continue;
			}

			try {
				var gson = new Gson().newBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(
						BlockItemBiomeSeasonColors.class,
						new BlockItemBiomeSeasonColorsDeserializer()
				).create();
				var blockItemBiomeSeasonColors = gson.fromJson(dataPackJsonFile.getValue().getReader(), BlockItemBiomeSeasonColors.class);
				blockItemBiomeSeasonColors.setIdentifier(identifier);
				blockItemBiomeSeasonColorsList.add(blockItemBiomeSeasonColors);
			} catch (IOException | IllegalArgumentException e) {
				SimpleSeasons.LOGGER.error(
						"Exception thrown while reading a resource pack JSON file (block/item identifier: {}):\n", identifier, e);
			}
		}

		return blockItemBiomeSeasonColorsList;
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
