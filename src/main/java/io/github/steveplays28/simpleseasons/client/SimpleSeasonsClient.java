package io.github.steveplays28.simpleseasons.client;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.client.api.BlockColorProviderRegistry;
import io.github.steveplays28.simpleseasons.client.model.SeasonClampedModelPredicateProvider;
import io.github.steveplays28.simpleseasons.client.resource.SimpleSeasonsResourceReloadListener;
import io.github.steveplays28.simpleseasons.client.state.ClientSeasonTracker;
import io.github.steveplays28.simpleseasons.client.util.season.color.SeasonColorUtil;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.List;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsClient implements ClientModInitializer {
	public static final SeasonTracker seasonTracker = new ClientSeasonTracker();

	private MinecraftClient client;

	@Override
	public void onInitializeClient() {
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> this.client = client);

		// Register vanilla block color providers using the API
		registerVanillaColorProviders();

		// Register season model predicate provider
		ModelPredicateProviderRegistry.register(new Identifier(MOD_ID, "season"), new SeasonClampedModelPredicateProvider());

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSeasonsResourceReloadListener());
	}

	private void registerVanillaColorProviders() {
		BlockColorProviderRegistry.registerBlocks(
				List.of(Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, Blocks.SPORE_BLOSSOM,
						Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM, Blocks.WHEAT, Blocks.ACACIA_SAPLING, Blocks.BIRCH_SAPLING,
						Blocks.CHERRY_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING,
						Blocks.BAMBOO, Blocks.BAMBOO_SAPLING
				));

		// TODO: Fix bamboo item color
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if (client.world == null) {
				throw new IllegalStateException("Error occurred while registering item color providers: client.world == null.");
			}

			return SeasonColorUtil.getItemSeasonColor(Registries.ITEM.getId(stack.getItem()),
					SimpleSeasonsApi.getSeason(client.world), SimpleSeasonsApi.getSeasonProgress(client.world),
					SeasonColorUtil.FALLBACK_SEASON_COLOR
			).toInt();
		}, Items.BAMBOO);
	}
}
