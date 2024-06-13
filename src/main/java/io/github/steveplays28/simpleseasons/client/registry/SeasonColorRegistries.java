package io.github.steveplays28.simpleseasons.client.registry;

import io.github.steveplays28.simpleseasons.client.resource.json.BlockItemBiomeSeasonColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SeasonColorRegistries {
	public static final @NotNull Map<Identifier, BlockItemBiomeSeasonColors> BLOCK_SEASON_COLORS_REGISTRY = new HashMap<>();
	public static final @NotNull Map<Identifier, BlockItemBiomeSeasonColors> ITEM_SEASON_COLORS_REGISTRY = new HashMap<>();
}
