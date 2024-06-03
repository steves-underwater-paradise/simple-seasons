package io.github.steveplays28.simpleseasons.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public abstract class WorldMixin {
	@WrapOperation(method = "hasRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
	private @NotNull Biome.Precipitation simple_seasons$getSeasonPrecipitation(@NotNull Biome biome, @NotNull BlockPos blockPos, @NotNull Operation<Biome.Precipitation> originalMethod) {
		@NotNull var castedWorld = (World) (Object) this;
		if (!SimpleSeasonsApi.worldHasSeasons(castedWorld) || SimpleSeasonsApi.biomeHasWetAndDrySeasons(
				castedWorld.getRegistryManager().get(RegistryKeys.BIOME).getEntry(biome)) || SimpleSeasonsApi.getSeason(
				castedWorld) != SeasonTracker.Seasons.WINTER) {
			return originalMethod.call(biome, blockPos);
		}

		return Biome.Precipitation.SNOW;
	}
}
