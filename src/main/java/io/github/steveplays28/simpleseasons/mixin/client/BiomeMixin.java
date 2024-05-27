package io.github.steveplays28.simpleseasons.mixin.client;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
@Mixin(Biome.class)
public abstract class BiomeMixin {
	@Shadow
	@Final
	private BiomeEffects effects;

	@Shadow
	protected abstract int getDefaultFoliageColor();

	@Shadow
	@Final
	public Biome.Weather weather;

	@Inject(method = "getFoliageColor", at = @At(value = "HEAD"), cancellable = true)
	public void getFoliageColorInject(CallbackInfoReturnable<Integer> cir) {
		var foliageColor = new Color(this.effects.getFoliageColor().orElseGet(this::getDefaultFoliageColor));

		if (SimpleSeasons.isDryBiome(weather.temperature(), weather.downfall())) {
			foliageColor = foliageColor.add(
					SEASONS_DRY_BIOMES_COLOR_ADDITIONS_MAP.get(SimpleSeasonsClient.seasonTracker.getSeason().getId()));
		} else {
			foliageColor = foliageColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(SimpleSeasonsClient.seasonTracker.getSeason().getId()));
		}

		cir.setReturnValue(foliageColor.toInt());
	}
}
