package io.github.steveplays28.simpleseasons.server.state.world;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import io.github.steveplays28.simpleseasons.config.SimpleSeasonsConfig;
import io.github.steveplays28.simpleseasons.server.util.time.TimeUtil;
import io.github.steveplays28.simpleseasons.state.world.SeasonStatePacket;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class ServerWorldSeasonTracker extends SeasonTracker {
	private final @NotNull MinecraftServer server;
	private final @NotNull ServerWorld serverWorld;
	private final @NotNull ServerWorldSeasonState serverWorldSeasonState;

	public ServerWorldSeasonTracker(@NotNull MinecraftServer server, @NotNull ServerWorld serverWorld) {
		super();

		this.server = server;
		this.serverWorld = serverWorld;
		this.serverWorldSeasonState = ServerWorldSeasonState.getOrCreate(serverWorld.getPersistentStateManager());
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server1) -> sendSeasonStatePacket(handler.player));
		ServerTickEvents.END_WORLD_TICK.register(this::onEndServerWorldTick);
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

	private void onEndServerWorldTick(@NotNull ServerWorld serverWorld) {
		if (serverWorld != this.serverWorld) {
			return;
		}

		var configInstance = SimpleSeasonsConfig.HANDLER.instance();
		var seasonColorUpdateRate = configInstance.seasonProgressUpdateRate;
		if (serverWorld.getTime() % (TimeUtil.getTicksPerSecond() / seasonColorUpdateRate) == 0) {
			setSeasonProgress(
					getSeasonProgress() + (1f / (SimpleSeasonsApi.getSeasonLengthSeconds() * configInstance.seasonLengthMultiplier) / seasonColorUpdateRate));
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
		ServerPlayNetworking.send(player, new SeasonStatePacket(serverWorldSeasonState.season, serverWorldSeasonState.seasonProgress));
	}
}
