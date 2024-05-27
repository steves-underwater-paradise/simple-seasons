package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FreezeTopLayerFeature.class)
public class FreezeTopLayerFeatureMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0), method = "generate", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void setMeltableIce(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir, StructureWorldAccess structureWorldAccess, BlockPos blockPos, BlockPos.Mutable mutable, BlockPos.Mutable mutable2, int i, int j, int k, int l, int m, Biome biome) {
		cir.setReturnValue(SimpleSeasonsClient.seasonTracker.getSeason().getId() != SeasonTracker.Seasons.WINTER.ordinal());
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1), method = "generate", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void setMeltableSnow(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir, StructureWorldAccess structureWorldAccess, BlockPos blockPos, BlockPos.Mutable mutable, BlockPos.Mutable mutable2, int i, int j, int k, int l, int m, Biome biome) {
		cir.setReturnValue(SimpleSeasonsClient.seasonTracker.getSeason().getId() != SeasonTracker.Seasons.WINTER.ordinal());
	}
}
