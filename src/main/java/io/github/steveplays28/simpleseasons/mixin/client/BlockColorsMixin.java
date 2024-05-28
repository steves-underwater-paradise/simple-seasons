package io.github.steveplays28.simpleseasons.mixin.client;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.api.BlockColorProviderRegistry;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
import io.github.steveplays28.simpleseasons.mixin.client.accessor.ChunkRendererRegionAccessor;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(BlockColors.class)
public class BlockColorsMixin {
	@Unique
	private static Color getFoliageColor(BlockRenderView world, BlockPos pos) {
		if (world == null || pos == null) {
			return new Color(FoliageColors.getDefaultColor());
		}

		Color foliageColor = new Color(BiomeColors.getFoliageColor(world, pos));
		if (world instanceof ChunkRendererRegionAccessor chunkRendererRegion) {
			var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
			var biome = clientWorld.getBiome(pos).value();
			var biomeWeather = biome.weather;

			if (SimpleSeasons.isDryBiome(biomeWeather.temperature(), biomeWeather.downfall())) {
				foliageColor = foliageColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld), true));
				return foliageColor;
			}

			foliageColor = foliageColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld)));
		}

		return foliageColor;
	}

	@Unique
	private static Color getGrassColor(BlockRenderView world, BlockPos pos) {
		if (world == null || pos == null) {
			return new Color(GrassColors.getDefaultColor());
		}

		var grassColor = new Color(BiomeColors.getGrassColor(world, pos));
		if (world instanceof ChunkRendererRegionAccessor chunkRendererRegion) {
			var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
			var biome = clientWorld.getBiome(pos).value();
			var biomeWeather = biome.weather;

			if (SimpleSeasons.isDryBiome(biomeWeather.temperature(), biomeWeather.downfall())) {
				grassColor = grassColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld), true));
				return grassColor;
			}

			grassColor = grassColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld)));
		}

		return grassColor;
	}

	@Inject(method = "create", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void createInject(CallbackInfoReturnable<BlockColors> cir, BlockColors blockColors) {
		// Grab all registered blocks from the API
		Block[] registeredBlocks = BlockColorProviderRegistry.getRegisteredBlocks().toArray(new Block[0]);

		// Register color provider for blocks
		blockColors.registerColorProvider((state, world, pos, tintIndex) -> getFoliageColor(world, pos).toInt(), registeredBlocks);
	}

	@Inject(method = "method_1695", at = @At(value = "HEAD"), cancellable = true)
	private static void spruceLeavesColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		var spruceLeavesColor = new Color(FoliageColors.getSpruceColor());

		if (world != null && pos != null && world instanceof ChunkRendererRegionAccessor chunkRendererRegion) {
			var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
			var biome = clientWorld.getBiome(pos).value();
			var biomeWeather = biome.weather;

			if (SimpleSeasons.isDryBiome(biomeWeather.temperature(), biomeWeather.downfall())) {
				spruceLeavesColor = spruceLeavesColor.add(
						SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld), true));
				cir.setReturnValue(spruceLeavesColor.toInt());
				return;
			}

			spruceLeavesColor = spruceLeavesColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld)));
		}

		cir.setReturnValue(spruceLeavesColor.toInt());
	}

	@Inject(method = "method_1687", at = @At(value = "HEAD"), cancellable = true)
	private static void birchLeavesColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		var birchLeavesColor = new Color(FoliageColors.getBirchColor());

		if (world != null && pos != null && world instanceof ChunkRendererRegionAccessor chunkRendererRegion) {
			var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
			var biome = clientWorld.getBiome(pos).value();
			var biomeWeather = biome.weather;

			if (SimpleSeasons.isDryBiome(biomeWeather.temperature(), biomeWeather.downfall())) {
				birchLeavesColor = birchLeavesColor.add(
						SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld), true));
				cir.setReturnValue(birchLeavesColor.toInt());
				return;
			}

			birchLeavesColor = birchLeavesColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld)));
		}

		cir.setReturnValue(birchLeavesColor.toInt());
	}

	@Inject(method = "method_1693", at = @At(value = "HEAD"), cancellable = true)
	private static void grassColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(getGrassColor(world, pos).toInt());
	}

	@Inject(method = "method_1692", at = @At(value = "HEAD"), cancellable = true)
	private static void foliageColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(getFoliageColor(world, pos).toInt());
	}

	@Inject(method = "method_1696", at = @At(value = "HEAD"), cancellable = true)
	private static void stemColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(getFoliageColor(world, pos).toInt());
	}

	@Inject(method = "method_1684", at = @At(value = "HEAD"), cancellable = true)
	private static void lilyPadColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(getFoliageColor(world, pos).toInt());
	}
}
