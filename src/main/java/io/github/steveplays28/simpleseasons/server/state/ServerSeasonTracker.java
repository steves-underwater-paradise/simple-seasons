package io.github.steveplays28.simpleseasons.server.state;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.state.SeasonState;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerSeasonTracker extends SeasonTracker {
	public @Nullable MinecraftServer server;

	public ServerSeasonTracker() {
		super();
		ServerLifecycleEvents.SERVER_STARTING.register(server -> this.server = server);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sendSeasonStatePacket(handler.player));
		ServerTickEvents.END_SERVER_TICK.register(this::onEndServerTick);
	}

	@Override
	public void setSeason(int seasonId) {
		super.setSeason(seasonId);

		if (server == null) {
			throw new IllegalStateException("Error occurred while trying to set the season: server is null.");
		}

		var seasonState = SeasonState.getServerState(server);
		seasonState.season = seasonId;
		seasonState.lastSeasonChangeTime = server.getOverworld().getTime();
		seasonState.markDirty();
		sendSeasonStatePacketToAllPlayers();

		SimpleSeasons.LOGGER.info("Set season to {}.", this.getSeason());
	}

	// TODO: Move into SeasonStatePacket class
	private static @NotNull PacketByteBuf createSeasonsStatePacket(@NotNull SeasonState seasonState) {
		PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(seasonState.season);

		return packet;
	}

	private void onEndServerTick(@NotNull MinecraftServer server) {
		if (server.getOverworld().getTime() % 4L == 0) {
			setSeasonProgress(getSeasonProgress() + 0.001f);
		}
	}

	private void sendSeasonStatePacketToAllPlayers() {
		if (server == null) {
			throw new IllegalStateException("Error occurred while trying to set the season: server is null.");
		}

		for (var player : this.server.getPlayerManager().getPlayerList()) {
			sendSeasonStatePacket(player);
		}
	}

	private void sendSeasonStatePacket(@NotNull ServerPlayerEntity player) {
		if (server == null) {
			throw new IllegalStateException("Error occurred while trying to set the season: server is null.");
		}

		var simpleSeasonsState = SeasonState.getServerState(this.server);
		var simpleSeasonsStatePacket = createSeasonsStatePacket(simpleSeasonsState);

		ServerPlayNetworking.send(player, SimpleSeasons.SEASON_PACKET_CHANNEL, simpleSeasonsStatePacket);
	}
}
