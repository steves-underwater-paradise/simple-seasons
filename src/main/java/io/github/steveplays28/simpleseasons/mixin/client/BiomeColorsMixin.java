package io.github.steveplays28.simpleseasons.mixin.client;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
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
				grassColor = grassColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld), true));
				cir.setReturnValue(grassColor.toInt());
				return;
			}

			grassColor = grassColor.add(SeasonColorUtil.getSeasonColorAddition(SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld)));
		}

		cir.setReturnValue(grassColor.toInt());
	}
}
