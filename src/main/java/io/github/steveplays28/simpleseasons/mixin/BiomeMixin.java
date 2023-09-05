package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASONS_COLOR_ADDITIONS_MAP;
import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP;
import static io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient.season;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	@Shadow
	@Final
	private BiomeEffects effects;

	@Shadow
	protected abstract int getDefaultFoliageColor();

	@Shadow
	@Final
	private Biome.Weather weather;

	@Inject(method = "getFoliageColor", at = @At(value = "HEAD"), cancellable = true)
	public void getFoliageColorInject(CallbackInfoReturnable<Integer> cir) {
		var foliageColor = new Color(this.effects.getFoliageColor().orElseGet(this::getDefaultFoliageColor));

		if (SimpleSeasons.isDryBiome(weather.temperature(), weather.downfall())) {
			foliageColor = foliageColor.add(SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP.get(season));
		} else {
			foliageColor = foliageColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(season));
		}

		cir.setReturnValue(foliageColor.toInt());
	}

	@Inject(method = "getPrecipitation", at = @At(value = "HEAD"), cancellable = true)
	public void getPrecipitationInject(CallbackInfoReturnable<Biome.Precipitation> cir) {
		var originalPrecipitation = cir.getReturnValue();
		if (originalPrecipitation == Biome.Precipitation.NONE) return;

		if (season == SimpleSeasons.Seasons.WINTER.ordinal()) {
			cir.setReturnValue(Biome.Precipitation.SNOW);
		} else {
			cir.setReturnValue(Biome.Precipitation.RAIN);
		}
	}

	@Inject(method = "doesNotSnow", at = @At(value = "HEAD"), cancellable = true)
	public void doesNotSnowInject(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(season != SimpleSeasons.Seasons.WINTER.ordinal());
	}
}
