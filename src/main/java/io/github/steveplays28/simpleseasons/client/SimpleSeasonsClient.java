package io.github.steveplays28.simpleseasons.client;

import io.github.steveplays28.simpleseasons.client.api.BlockColorProviderRegistry;
import io.github.steveplays28.simpleseasons.client.model.SeasonClampedModelPredicateProvider;
import io.github.steveplays28.simpleseasons.mixin.accessor.WorldRendererAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.*;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsClient implements ClientModInitializer {
	public static int season;

	@Override
	public void onInitializeClient() {
		// Register vanilla block color providers using the API
		registerVanillaColorProviders();

		// Register season model predicate provider
		ModelPredicateProviderRegistry.register(new Identifier(MOD_ID, "season"), new SeasonClampedModelPredicateProvider());

		ClientPlayNetworking.registerGlobalReceiver(SEASON_PACKET_CHANNEL, (client, handler, buf, responseSender) -> {
			if (client.world == null || client.player == null) return;

			season = buf.readInt();

			var initialLoad = buf.readBoolean();
			if (initialLoad) return;

			reloadChunkColors(client.world);
		});
	}

	private void registerVanillaColorProviders() {
		BlockColorProviderRegistry.registerBlocks(
				List.of(Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, Blocks.SPORE_BLOSSOM,
						Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM, Blocks.WHEAT, Blocks.ACACIA_SAPLING, Blocks.BIRCH_SAPLING,
						Blocks.CHERRY_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING,
						Blocks.BAMBOO, Blocks.BAMBOO_SAPLING
				));

		// TODO: Fix bamboo item color
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> SEASONS_COLOR_ADDITIONS_MAP.get(season).toInt(), Items.BAMBOO);
	}

	private void reloadChunkColors(@NotNull ClientWorld world) {
		world.reloadColor();

		for (ChunkBuilder.BuiltChunk builtChunk : ((WorldRendererAccessor) world.worldRenderer).getChunks().chunks) {
			builtChunk.scheduleRebuild(true);
		}
	}
}
