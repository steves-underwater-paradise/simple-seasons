package io.github.steveplays28.simpleseasons.mixin.client;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import io.github.steveplays28.simpleseasons.mixin.client.accessor.ChunkRendererRegionAccessor;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASONS_COLOR_ADDITIONS_MAP;
import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	@Inject(method = "getGrassColor", at = @At(value = "RETURN"), cancellable = true)
	private static void getGrassColorInject(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		var grassColorVanilla = cir.getReturnValue();
		var grassColor = new Color(grassColorVanilla);

		if (world != null && pos != null && world instanceof ChunkRendererRegionAccessor chunkRendererRegion) {
			var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
			var biome = clientWorld.getBiome(pos).value();
			var biomeWeather = biome.weather;

			if (SimpleSeasons.isDryBiome(biomeWeather.temperature(), biomeWeather.downfall())) {
				grassColor = grassColor.add(SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP.get(SimpleSeasonsClient.seasonTracker.getSeason().getId()));
				cir.setReturnValue(grassColor.toInt());
				return;
			}
		}

		grassColor = grassColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(SimpleSeasonsClient.seasonTracker.getSeason().getId()));
		cir.setReturnValue(grassColor.toInt());
	}
}
