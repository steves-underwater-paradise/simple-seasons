package io.github.steveplays28.simpleseasons.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.FloatField;
import dev.isxander.yacl3.config.v2.api.autogen.FloatSlider;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;
import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_NAMESPACE;

public class SimpleSeasonsConfig {
	public static final @NotNull String JSON_5_FILE_SUFFIX = ".json5";
	public static final @NotNull ConfigClassHandler<SimpleSeasonsConfig> HANDLER = ConfigClassHandler.createBuilder(
			SimpleSeasonsConfig.class).id(
			Identifier.of(MOD_ID, "config")).serializer(config -> GsonConfigSerializerBuilder.create(config).setPath(
			FabricLoader.getInstance().getConfigDir().resolve(String.format("%s/config%s", MOD_NAMESPACE, JSON_5_FILE_SUFFIX))).setJson5(
			true).build()).build();

	private static final @NotNull String SERVER_CATEGORY = "Server";

	@AutoGen(category = SERVER_CATEGORY)
	@SerialEntry(comment = "The rate at which the season progress updates. Every time the season progress updates in a world, a packet is sent to all clients in that world containing the new season progress. Subsequently, all clients in that world will reload the world's color and schedule a rebuild for all chunks. Increasing this value will result in packets being sent more often to all clients in a world and thus season colors will update more often on all clients in that world. Decreasing this value will result in packets being sent less often to all clients in a world and thus season colors will update less often on all clients in that world. This config option can be performance intensive if set too high.")
	@FloatSlider(min = 0.0001f, max = 20f, step = 0.0001f, format = "%.4f")
	public float seasonProgressUpdateRate = 0.1f;
	@AutoGen(category = SERVER_CATEGORY)
	@SerialEntry(comment = "The length multiplier of seasons. Increasing this value will result in longer seasons and decreasing this value will result in shorter seasons.")
	@FloatField(min = 0.0001f, format = "%.4f")
	public float seasonLengthMultiplier = 1f;
	@AutoGen(category = SERVER_CATEGORY)
	@SerialEntry(comment = "Determines if ice should form in water (with the exception of water in oceans and water in biomes with a wet/dry season) during winter.")
	@Boolean(colored = true)
	public boolean iceFormationInWaterDuringWinter = true;
}
