package io.github.steveplays28.simpleseasons.mixin.world.biome;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	@Shadow
	public abstract boolean hasPrecipitation();

	@Inject(method = "canSetSnow", at = @At(value = "HEAD"), cancellable = true)
	private void simple_seasons$canSetSnow(@NotNull WorldView worldView, @NotNull BlockPos blockPos, @NotNull CallbackInfoReturnable<Boolean> cir) {
		if (!(worldView instanceof @NotNull World world) || !SimpleSeasonsApi.worldHasSeasons(
				world) || !this.hasPrecipitation() || SimpleSeasonsApi.biomeHasWetAndDrySeasons(
				world.getBiome(blockPos)) || !Blocks.SNOW.getDefaultState().canPlaceAt(world, blockPos)) {
			return;
		}

		cir.setReturnValue(SimpleSeasonsApi.getSeason(world) == SeasonTracker.Seasons.WINTER);
	}

	@Inject(method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z", at = @At(value = "HEAD"), cancellable = true)
	private void simple_seasons$canSetIceAllowSettingIceInWinter(@NotNull WorldView worldView, @NotNull BlockPos blockPos, boolean doWaterCheck, @NotNull CallbackInfoReturnable<Boolean> cir) {
		if (!(worldView instanceof @NotNull World world) || !SimpleSeasonsApi.worldHasSeasons(
				world) || SimpleSeasonsApi.biomeHasWetAndDrySeasons(world.getBiome(blockPos)) || world.getLightLevel(
				LightType.BLOCK, blockPos) >= 10 || doWaterCheck && !world.getFluidState(blockPos).isOf(Fluids.WATER)) {
			return;
		}

		cir.setReturnValue(SimpleSeasonsApi.getSeason(world) == SeasonTracker.Seasons.WINTER);
	}
}
