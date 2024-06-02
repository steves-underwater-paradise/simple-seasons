package io.github.steveplays28.simpleseasons.server.state.world;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class ServerWorldSeasonTracker extends SeasonTracker {
	private final @NotNull MinecraftServer server;
	private final @NotNull ServerWorldSeasonState serverWorldSeasonState;

	public ServerWorldSeasonTracker(@NotNull MinecraftServer server, @NotNull ServerWorld serverWorld) {
		super();

		this.server = server;
		this.serverWorldSeasonState = ServerWorldSeasonState.getOrCreate(serverWorld.getPersistentStateManager());
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server1) -> sendSeasonStatePacket(handler.player));
		ServerTickEvents.END_SERVER_TICK.register(this::onEndServerTick);
	}

	@Override
	public void setSeason(int seasonId) {
		super.setSeason(seasonId);

		serverWorldSeasonState.season = seasonId;
		serverWorldSeasonState.seasonProgress = getSeasonProgress();
		serverWorldSeasonState.markDirty();
		sendSeasonStatePacketToAllPlayers();

		SimpleSeasons.LOGGER.info("Set season to {}.", this.getSeason());
	}

	@Override
	public void setSeasonProgress(float seasonProgress) {
		super.setSeasonProgress(seasonProgress);

		serverWorldSeasonState.season = getSeason().getId();
		serverWorldSeasonState.seasonProgress = getSeasonProgress();
		serverWorldSeasonState.markDirty();
		sendSeasonStatePacketToAllPlayers();
	}

	// TODO: Move into SeasonStatePacket class
	private static @NotNull PacketByteBuf createSeasonStatePacket(@NotNull ServerWorldSeasonState serverWorldSeasonState) {
		@NotNull var packet = PacketByteBufs.create();
		packet.writeInt(serverWorldSeasonState.season);
		packet.writeFloat(serverWorldSeasonState.seasonProgress);
		return packet;
	}

	private void onEndServerTick(@NotNull MinecraftServer server) {
		if (server.getOverworld().getTime() % 4L == 0) {
			setSeasonProgress(getSeasonProgress() + 0.001f);
		}
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	private void sendSeasonStatePacketToAllPlayers() {
		var playerList = this.server.getPlayerManager().getPlayerList();
		for (int i = 0; i < playerList.size(); i++) {
			sendSeasonStatePacket(playerList.get(i));
		}
	}

	private void sendSeasonStatePacket(@NotNull ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, SimpleSeasons.SEASON_PACKET_CHANNEL, createSeasonStatePacket(serverWorldSeasonState));
	}
}
