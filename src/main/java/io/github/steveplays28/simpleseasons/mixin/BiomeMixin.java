package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	@Inject(method = "getPrecipitation", at = @At(value = "HEAD"), cancellable = true)
	public void getPrecipitationInject(@NotNull CallbackInfoReturnable<Biome.Precipitation> cir) {
		var originalPrecipitation = cir.getReturnValue();
		if (originalPrecipitation == Biome.Precipitation.NONE) {
			return;
		}

		if (SimpleSeasons.getSeason().getId() == SeasonTracker.Seasons.WINTER.ordinal()) {
			cir.setReturnValue(Biome.Precipitation.SNOW);
		} else {
			cir.setReturnValue(Biome.Precipitation.RAIN);
		}
	}

	@Inject(method = "doesNotSnow", at = @At(value = "HEAD"), cancellable = true)
	public void doesNotSnowInject(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(SimpleSeasons.getSeason().getId() != SeasonTracker.Seasons.WINTER.ordinal());
	}
}
