package io.github.steveplays28.simpleseasons.mixin.world.gen.feature;

import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FreezeTopLayerFeature.class)
public class FreezeTopLayerFeatureMixin {
	@ModifyArg(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z"), index = 2)
	private boolean simple_seasons$setIceWithWaterCheck(boolean doWaterCheck) {
		return true;
	}
}
