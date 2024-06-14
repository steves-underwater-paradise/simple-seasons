package io.github.steveplays28.simpleseasons.client;

import io.github.steveplays28.simpleseasons.client.model.SeasonClampedModelPredicateProvider;
import io.github.steveplays28.simpleseasons.client.resource.SimpleSeasonsResourceReloadListener;
import io.github.steveplays28.simpleseasons.client.state.world.ClientSeasonTracker;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;
import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAME;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsClient implements ClientModInitializer {
	public static final SeasonTracker SEASON_TRACKER = new ClientSeasonTracker();

	@Override
	public void onInitializeClient() {
		// Register a season model predicate provider
		ModelPredicateProviderRegistry.register(new Identifier(MOD_ID, "season"), new SeasonClampedModelPredicateProvider());

		var modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);
		if (modContainer.isEmpty()) {
			throw new IllegalStateException(
					String.format("%s's mod container (mod ID: %s) is empty. %s is unable to register a built-in default resource pack.",
							MOD_NAME, MOD_ID, MOD_NAME
					));
		}

		// Register a built-in default resource pack
		ResourceManagerHelper.registerBuiltinResourcePack(
				new Identifier(MOD_ID, "default"), modContainer.get(), ResourcePackActivationType.DEFAULT_ENABLED);
		// Register a resource reload listener
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSeasonsResourceReloadListener());
	}
}
