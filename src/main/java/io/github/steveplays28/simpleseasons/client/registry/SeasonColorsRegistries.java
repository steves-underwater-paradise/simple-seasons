package io.github.steveplays28.simpleseasons.client.registry;

import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

public class SeasonColorsRegistries {
	public static final @NotNull SimpleRegistry<@NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color>> BLOCK_SEASON_COLORS_REGISTRY = FabricRegistryBuilder.createSimple(
			RegistryKey.<Map<SeasonTracker.Seasons, Color>>ofRegistry(new Identifier(MOD_ID, "block_season_colors"))).buildAndRegister();
	public static final @NotNull SimpleRegistry<@NotNull Map<SeasonTracker.@NotNull Seasons, @NotNull Color>> ITEM_SEASON_COLORS_REGISTRY = FabricRegistryBuilder.createSimple(
			RegistryKey.<Map<SeasonTracker.Seasons, Color>>ofRegistry(new Identifier(MOD_ID, "item_season_colors"))).buildAndRegister();
}
