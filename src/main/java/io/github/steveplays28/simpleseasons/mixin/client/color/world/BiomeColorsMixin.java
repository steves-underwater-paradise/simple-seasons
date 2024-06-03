package io.github.steveplays28.simpleseasons.mixin.client.color.world;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.extension.world.ClientWorldExtension;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
import io.github.steveplays28.simpleseasons.mixin.client.accessor.ChunkRendererRegionAccessor;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {
	/**
	 * A Mixin {@link Inject} into {@link BiomeColors#getGrassColor} and {@link BiomeColors#getFoliageColor}.
	 *
	 * @param world         The {@link BlockRenderView} that can be used to get the {@link ClientWorld}.
	 *                      Can never be {@code null}, as {@code null} checks are done in every code path before this function is called in vanilla Minecraft.
	 * @param startBlockPos The position of the block where the color is being queried.
	 *                      Can never be {@code null}, as {@code null} checks are done in every code path before this function is called in vanilla Minecraft.
	 * @param cir           The {@link CallbackInfoReturnable}.
	 */
	@Inject(method = {"getGrassColor", "getFoliageColor"}, at = @At(value = "RETURN"), cancellable = true)
	private static void simple_seasons$modifyGrassAndFoliageColors(@NotNull BlockRenderView world, @NotNull BlockPos startBlockPos, @NotNull CallbackInfoReturnable<Integer> cir) {
		if (!(world instanceof ChunkRendererRegionAccessor chunkRendererRegion)) {
			return;
		}

		@NotNull var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
		@NotNull var blockPosColorCache = ((ClientWorldExtension) clientWorld).simple_seasons$getColorCache().blockPosColorCache;
		@Nullable var cachedStartBlockSeasonColor = blockPosColorCache.get(startBlockPos);
		if (cachedStartBlockSeasonColor != null) {
			cir.setReturnValue(cachedStartBlockSeasonColor.toInt());
		}

		var biomeBlendedBlockSeasonColor = clientWorld.calculateColor(startBlockPos, (biome, x, z) -> {
			var blockPos = new BlockPos((int) Math.round(x), startBlockPos.getY(), (int) Math.round(z));
			// Nullable due to concurrency
			@Nullable var cachedBlockSeasonColor = blockPosColorCache.get(blockPos);
			if (cachedBlockSeasonColor != null) {
				return cachedBlockSeasonColor.toInt();
			}

			var blockSeasonColor = SeasonColorUtil.getBlockSeasonColor(
					Registries.BLOCK.getId(clientWorld.getBlockState(blockPos).getBlock()),
					clientWorld.getBiome(blockPos),
					SimpleSeasonsApi.getSeason(clientWorld),
					SimpleSeasonsApi.getSeasonProgress(clientWorld)
			);
			if (blockSeasonColor != null) {
				return blockSeasonColor.toInt();
			}

			var startBlockSeasonColor = SeasonColorUtil.getBlockSeasonColor(
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
		cir.setReturnValue(biomeBlendedBlockSeasonColor);
	}
}
