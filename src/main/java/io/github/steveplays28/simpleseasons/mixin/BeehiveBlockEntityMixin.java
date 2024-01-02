package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin extends BlockEntity {
	public BeehiveBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(method = "tryReleaseBee", at = @At(value = "HEAD"))
	private void simpleseasons$stopBeeReleaseInWinter(BlockState state, BeehiveBlockEntity.BeeState beeState, CallbackInfoReturnable<List<Entity>> cir) {
		var world = getWorld();
		if (world == null) {
			return;
		}

		if (SimpleSeasonsApi.getSeason(world) == SimpleSeasons.Seasons.WINTER.value) {
			cir.cancel();
		}
	}
}