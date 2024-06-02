package io.github.steveplays28.simpleseasons.mixin.block;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IceBlock.class)
public abstract class IceBlockMixin extends TransparentBlock {
	public IceBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	protected abstract void melt(BlockState blockState, World world, BlockPos blockPos);

	@Inject(method = "randomTick", at = @At(value = "HEAD"), cancellable = true)
	public void randomTickInject(@NotNull BlockState blockState, @NotNull ServerWorld serverWorld, @NotNull BlockPos blockPos, @NotNull Random random, @NotNull CallbackInfo ci) {
		if (!SimpleSeasonsApi.worldHasSeasons(serverWorld)) {
			return;
		}

		if (SimpleSeasonsApi.getSeason(serverWorld) == SeasonTracker.Seasons.WINTER) {
			ci.cancel();
			return;
		}

		this.melt(blockState, serverWorld, blockPos);
	}
}
