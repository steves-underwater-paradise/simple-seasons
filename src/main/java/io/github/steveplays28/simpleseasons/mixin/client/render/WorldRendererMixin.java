package io.github.steveplays28.simpleseasons.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
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

	@WrapOperation(method = {"renderWeather", "tickRainSplashing"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
	private @NotNull Biome.Precipitation simple_seasons$getSeasonPrecipitation(@NotNull Biome biome, @NotNull BlockPos blockPos, @NotNull Operation<Biome.Precipitation> originalMethod) {
		if (world == null || !SimpleSeasonsApi.worldHasSeasons(world) || SimpleSeasonsApi.getSeason(world) != SeasonTracker.Seasons.WINTER) {
			return originalMethod.call(blockPos);
		}

		return Biome.Precipitation.SNOW;
	}
}
