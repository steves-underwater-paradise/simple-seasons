package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.state.SeasonState;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {
	@Inject(method = "randomTick", at = @At(value = "HEAD"))
	public void randomTickInject(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		var simpleSeasonsState = SeasonState.getServerState(world.getServer());

		if (simpleSeasonsState.season != SeasonTracker.Seasons.WINTER.ordinal()) {
			world.removeBlock(pos, false);
		}
	}
}
