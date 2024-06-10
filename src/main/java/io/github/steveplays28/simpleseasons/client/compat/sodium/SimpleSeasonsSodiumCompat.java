package io.github.steveplays28.simpleseasons.client.compat.sodium;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsSodiumCompat {
	private static final @NotNull String SODIUM_MOD_ID = "sodium";
	private static final @NotNull String EMBEDDIUM_MOD_ID = "embeddium";
	private static final @NotNull String RUBIDIUM_MOD_ID = "rubidium";

	public static boolean isSodiumLoaded() {
		var fabricLoader = FabricLoader.getInstance();
		return fabricLoader.isModLoaded(SODIUM_MOD_ID) || fabricLoader.isModLoaded(EMBEDDIUM_MOD_ID) || fabricLoader.isModLoaded(
				RUBIDIUM_MOD_ID);
	}
}
