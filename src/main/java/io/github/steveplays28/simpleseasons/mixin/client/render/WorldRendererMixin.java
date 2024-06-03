package io.github.steveplays28.simpleseasons.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow
	private @Nullable ClientWorld world;

	@WrapOperation(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;hasPrecipitation()Z"))
	private boolean simple_seasons$getSeasonPrecipitation(@NotNull Biome biome, @NotNull Operation<Boolean> originalMethod) {
		if (world == null || !SimpleSeasonsApi.worldHasSeasons(world)) {
			return originalMethod.call(biome);
		}

		@NotNull var season = SimpleSeasonsApi.getSeason(world);
		if (SimpleSeasonsApi.biomeHasWetAndDrySeasons(world.getRegistryManager().get(RegistryKeys.BIOME).getEntry(biome))) {
			return season == SeasonTracker.Seasons.SPRING || season == SeasonTracker.Seasons.SUMMER;
		}

		if (season != SeasonTracker.Seasons.WINTER) {
			return originalMethod.call(biome);
		}

		return true;
	}

	@WrapOperation(method = {"renderWeather", "tickRainSplashing"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
	private @NotNull Biome.Precipitation simple_seasons$getSeasonPrecipitation(@NotNull Biome biome, @NotNull BlockPos blockPos, @NotNull Operation<Biome.Precipitation> originalMethod) {
		if (world == null || !SimpleSeasonsApi.worldHasSeasons(world)) {
			return originalMethod.call(biome, blockPos);
		}

		@NotNull var season = SimpleSeasonsApi.getSeason(world);
		if (SimpleSeasonsApi.biomeHasWetAndDrySeasons(world.getRegistryManager().get(RegistryKeys.BIOME).getEntry(biome))) {
			if (season == SeasonTracker.Seasons.SPRING || season == SeasonTracker.Seasons.SUMMER) {
				return Biome.Precipitation.RAIN;
			}

			return Biome.Precipitation.NONE;
		}

		if (season != SeasonTracker.Seasons.WINTER) {
			return originalMethod.call(biome, blockPos);
		}

		return Biome.Precipitation.SNOW;
	}
}
