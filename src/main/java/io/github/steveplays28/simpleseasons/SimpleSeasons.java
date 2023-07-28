package io.github.steveplays28.simpleseasons;

import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SimpleSeasons implements ModInitializer {
	public static final String MOD_ID = "simple-seasons";
	public static final String MOD_NAMESPACE = "simpleseasons";
	public static final String MOD_NAME = "Simple Seasons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
	public static final Identifier SEASON_PACKET_CHANNEL = new Identifier(MOD_NAMESPACE);
	// Per-season color additions
	public static final Color SPRING_COLOR_ADDITION = new Color(255 / 3, 255 / 3, 0);
	public static final Color SUMMER_COLOR_ADDITION = new Color(0, 0, 0);
	public static final Color FALL_COLOR_ADDITION = new Color(255 / 3, 0, 0);
	public static final Color WINTER_COLOR_ADDITION = new Color(255 / 2, 255 / 2, 255 / 2);
	// Seasons color map
	public static final Map<Integer, Color> SEASONS_COLOR_ADDITIONS_MAP = Map.of(Seasons.SPRING.ordinal(), SPRING_COLOR_ADDITION,
			Seasons.SUMMER.ordinal(), SUMMER_COLOR_ADDITION, Seasons.FALL.ordinal(), FALL_COLOR_ADDITION, Seasons.WINTER.ordinal(),
			WINTER_COLOR_ADDITION
	);
	public static final long TIME_PER_DAY = 2;
	public static final long TIME_PER_SEASON_CHANGE = TIME_PER_DAY * 30 * 3;

	public enum Seasons {
		SPRING, SUMMER, FALL, WINTER
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing {}!", MOD_NAME);

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			var overworld = server.getOverworld();
			var simpleSeasonsState = SimpleSeasonsState.getServerState(server);
			var currentTime = overworld.getTime();

			if (currentTime - simpleSeasonsState.lastSeasonChangeTime >= TIME_PER_SEASON_CHANGE) {
				simpleSeasonsState.lastSeasonChangeTime = currentTime;
				if (simpleSeasonsState.season >= 3) {
					simpleSeasonsState.season = 0;
				} else {
					simpleSeasonsState.season += 1;
				}
				simpleSeasonsState.markDirty();

				// Stop raining/snowing if it's spring or summer, and start raining/snowing if it's fall or winter
				var worldProperties = overworld.getLevelProperties();
				worldProperties.setRaining(
						simpleSeasonsState.season == Seasons.FALL.ordinal() || simpleSeasonsState.season == Seasons.WINTER.ordinal());

				// Send Simple Seasons state packet to all players
				for (var player : server.getPlayerManager().getPlayerList()) {
					sendSimpleSeasonsStatePacket(server, player);
				}

				LOGGER.info("Set season to {}.", simpleSeasonsState.season);
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sendSimpleSeasonsStatePacket(server, handler.player));
	}

	private PacketByteBuf createSimpleSeasonsStatePacket(SimpleSeasonsState simpleSeasonsState) {
		PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(simpleSeasonsState.season);

		return packet;
	}

	private void sendSimpleSeasonsStatePacket(MinecraftServer server, ServerPlayerEntity player) {
		var simpleSeasonsState = SimpleSeasonsState.getServerState(server);
		var simpleSeasonsStatePacket = createSimpleSeasonsStatePacket(simpleSeasonsState);

		ServerPlayNetworking.send(player, SEASON_PACKET_CHANNEL, simpleSeasonsStatePacket);
	}
}
