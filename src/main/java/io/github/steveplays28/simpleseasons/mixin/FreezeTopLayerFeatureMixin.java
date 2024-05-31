package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FreezeTopLayerFeature.class)
public class FreezeTopLayerFeatureMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0), method = "generate", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void setMeltableIce(
			@NotNull FeatureContext<DefaultFeatureConfig> context, @NotNull CallbackInfoReturnable<Boolean> cir,
			@NotNull StructureWorldAccess structureWorldAccess, @NotNull BlockPos blockPos,
			@NotNull BlockPos.Mutable mutable, @NotNull BlockPos.Mutable mutable2,
			int i, int j, int k, int l, int m, @NotNull Biome biome
	) {
		cir.setReturnValue(
				SimpleSeasonsApi.getSeason(structureWorldAccess.toServerWorld()).getId() != SeasonTracker.Seasons.WINTER.ordinal());
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1), method = "generate", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void setMeltableSnow(
			@NotNull FeatureContext<DefaultFeatureConfig> context, @NotNull CallbackInfoReturnable<Boolean> cir,
			@NotNull StructureWorldAccess structureWorldAccess, @NotNull BlockPos blockPos,
			@NotNull BlockPos.Mutable mutable, @NotNull BlockPos.Mutable mutable2,
			int i, int j, int k, int l, int m, @NotNull Biome biome
	) {
		cir.setReturnValue(
				SimpleSeasonsApi.getSeason(structureWorldAccess.toServerWorld()).getId() != SeasonTracker.Seasons.WINTER.ordinal());
	}
}
