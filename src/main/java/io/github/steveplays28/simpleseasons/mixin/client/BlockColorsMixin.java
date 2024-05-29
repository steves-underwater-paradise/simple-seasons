package io.github.steveplays28.simpleseasons.mixin.client;

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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(BlockColors.class)
public class BlockColorsMixin {
	@Inject(method = "create", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void simple_seasons$createInject(CallbackInfoReturnable<BlockColors> cir, BlockColors blockColors) {
		// Grab all registered blocks from the API
		Block[] registeredBlocks = BlockColorProviderRegistry.getRegisteredBlocks().toArray(new Block[0]);

		// Register color provider for blocks
		blockColors.registerColorProvider((blockState, world, blockPos, tintIndex) -> {
			if (world == null || blockPos == null) {
				return FoliageColors.getDefaultColor();
			}

			return BiomeColors.getFoliageColor(world, blockPos);
		}, registeredBlocks);
	}

	@Inject(method = {"method_1695", "method_1687"}, at = @At(value = "RETURN"), cancellable = true)
	private static void simple_seasons$modifyLeafColors(BlockState blockState, BlockRenderView world, BlockPos blockPos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		if (world == null || blockPos == null || !(world instanceof ChunkRendererRegionAccessor chunkRendererRegion)) {
			return;
		}

		var vanillaColor = new Color(cir.getReturnValue());
		var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
		var biome = clientWorld.getBiome(blockPos).value();
		cir.setReturnValue(
				SeasonColorUtil.getBlockSeasonColor(
						Registries.BLOCK.getId(clientWorld.getBlockState(blockPos).getBlock()), biome,
						SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld),
						vanillaColor
				).toInt());
	}

	// TODO: Reimplement stem block ageing colors
	@Inject(method = "method_1696", at = @At(value = "HEAD"), cancellable = true)
	private static void simple_seasons$modifyStemColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(BiomeColors.getFoliageColor(world, pos));
	}

	@Inject(method = "method_1684", at = @At(value = "HEAD"), cancellable = true)
	private static void simple_seasons$modifyLilyPadColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(BiomeColors.getFoliageColor(world, pos));
	}
}
