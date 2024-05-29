package io.github.steveplays28.simpleseasons.mixin.client;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
import io.github.steveplays28.simpleseasons.mixin.client.accessor.ChunkRendererRegionAccessor;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	@Inject(method = {"getGrassColor", "getFoliageColor"}, at = @At(value = "RETURN"), cancellable = true)
	private static void simple_seasons$modifyGrassAndFoliageColors(BlockRenderView world, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {
		if (world == null || blockPos == null || !(world instanceof ChunkRendererRegionAccessor chunkRendererRegion)) {
			return;
		}

		var vanillaColor = new Color(cir.getReturnValue());
		var clientWorld = (ClientWorld) chunkRendererRegion.getWorld();
		var biome = clientWorld.getBiome(blockPos).value();
		cir.setReturnValue(
				SeasonColorUtil.getBlockSeasonColor(Registries.BLOCK.getId(clientWorld.getBlockState(blockPos).getBlock()),
						clientWorld.getRegistryManager().get(RegistryKeys.BIOME).getEntry(biome),
						SimpleSeasonsApi.getSeason(clientWorld), SimpleSeasonsApi.getSeasonProgress(clientWorld), vanillaColor
				).toInt());
	}
}
