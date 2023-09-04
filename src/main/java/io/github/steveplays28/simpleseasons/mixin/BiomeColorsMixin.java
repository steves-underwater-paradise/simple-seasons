package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient.season;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	@Inject(method = "getGrassColor", at = @At(value = "RETURN"), cancellable = true)
	private static void getGrassColorInject(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		var grassColorVanilla = cir.getReturnValue();
		var grassColor = new Color(grassColorVanilla);

		grassColor = grassColor.add(SimpleSeasons.SEASONS_COLOR_ADDITIONS_MAP.get(season));
		cir.setReturnValue(grassColor.toInt());
	}
}
