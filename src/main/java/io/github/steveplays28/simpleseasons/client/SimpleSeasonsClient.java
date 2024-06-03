package io.github.steveplays28.simpleseasons.client;

import io.github.steveplays28.simpleseasons.client.model.SeasonClampedModelPredicateProvider;
import io.github.steveplays28.simpleseasons.client.resource.SimpleSeasonsResourceReloadListener;
import io.github.steveplays28.simpleseasons.client.state.world.ClientSeasonTracker;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsClient implements ClientModInitializer {
	public static final SeasonTracker SEASON_TRACKER = new ClientSeasonTracker();

	@Override
	public void onInitializeClient() {
		// Register a season model predicate provider
		ModelPredicateProviderRegistry.register(new Identifier(MOD_ID, "season"), new SeasonClampedModelPredicateProvider());
		// Register a resource reload listener
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSeasonsResourceReloadListener());
	}
}
