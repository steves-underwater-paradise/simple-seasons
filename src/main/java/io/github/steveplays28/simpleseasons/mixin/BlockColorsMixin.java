package io.github.steveplays28.simpleseasons.mixin;

import io.github.steveplays28.simpleseasons.util.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASONS_COLOR_ADDITIONS_MAP;
import static io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient.season;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
	@Inject(method = "create", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void createInject(CallbackInfoReturnable<BlockColors> cir, BlockColors blockColors) {
		blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
			if (world == null || pos == null) {
				return FoliageColors.getDefaultColor();
			}

			return BiomeColors.getFoliageColor(world, pos);
		}, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, Blocks.SPORE_BLOSSOM);
	}

	@Inject(method = "method_1695", at = @At(value = "HEAD"), cancellable = true)
	private static void spruceLeavesColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		var spruceLeavesColor = new Color(FoliageColors.getSpruceColor());
		spruceLeavesColor = spruceLeavesColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(season));

		cir.setReturnValue(spruceLeavesColor.toInt());
	}

	@Inject(method = "method_1687", at = @At(value = "HEAD"), cancellable = true)
	private static void birchLeavesColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		var birchLeavesColor = new Color(FoliageColors.getBirchColor());
		birchLeavesColor = birchLeavesColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(season));

		cir.setReturnValue(birchLeavesColor.toInt());
	}

	@Inject(method = "method_1693", at = @At(value = "HEAD"), cancellable = true)
	private static void grassColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		Color grassColor;

		if (world == null || pos == null) {
			grassColor = new Color(GrassColors.getColor(0.5, 1.0));
		} else {
			grassColor = new Color(BiomeColors.getGrassColor(world, pos));
		}

		grassColor = grassColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(season));

		cir.setReturnValue(grassColor.toInt());
	}

	@Inject(method = "method_1692", at = @At(value = "HEAD"), cancellable = true)
	private static void foliageColorInject(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
		Color foliageColor;

		if (world == null || pos == null) {
			foliageColor = new Color(FoliageColors.getDefaultColor());
		} else {
			foliageColor = new Color(BiomeColors.getFoliageColor(world, pos));
		}

		foliageColor = foliageColor.add(SEASONS_COLOR_ADDITIONS_MAP.get(season));

		cir.setReturnValue(foliageColor.toInt());
	}
}
