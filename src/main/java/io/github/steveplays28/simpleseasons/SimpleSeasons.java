package io.github.steveplays28.simpleseasons;

import io.github.steveplays28.simpleseasons.util.Color;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
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
	public static final Map<Integer, Color> SEASONS_COLOR_MAP = Map.of(
			Seasons.SPRING.ordinal(), SPRING_COLOR_ADDITION,
			Seasons.SUMMER.ordinal(), SUMMER_COLOR_ADDITION,
			Seasons.FALL.ordinal(), FALL_COLOR_ADDITION,
			Seasons.WINTER.ordinal(), WINTER_COLOR_ADDITION
	);

	public enum Seasons {
		SPRING, SUMMER, FALL, WINTER
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing {}!", MOD_NAME);

		// Set season to winter
		// TODO: Replace with a season cycle
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SimpleSeasonsState simpleSeasonsState = SimpleSeasonsState.getServerState(server);

			simpleSeasonsState.season = Seasons.WINTER.ordinal();
			simpleSeasonsState.markDirty();
		});


		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			SimpleSeasonsState simpleSeasonsState = SimpleSeasonsState.getServerState(server);
			var simpleSeasonsStatePacket = createSimpleSeasonsStatePacket(simpleSeasonsState);

			// Send the packet to the connected player
			ServerPlayNetworking.send(handler.player, SEASON_PACKET_CHANNEL, simpleSeasonsStatePacket);
		});
	}

	private PacketByteBuf createSimpleSeasonsStatePacket(SimpleSeasonsState simpleSeasonsState) {
		PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(simpleSeasonsState.season);

		return packet;
	}
}
