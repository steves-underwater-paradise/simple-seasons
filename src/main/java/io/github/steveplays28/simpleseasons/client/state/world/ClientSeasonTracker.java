package io.github.steveplays28.simpleseasons.client.state.world;

import io.github.steveplays28.simpleseasons.client.util.rendering.RenderingUtil;
import io.github.steveplays28.simpleseasons.state.world.SeasonStatePayload;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientSeasonTracker extends SeasonTracker {
	public ClientSeasonTracker() {
		super();

		ClientPlayNetworking.registerGlobalReceiver(SeasonStatePayload.PACKET_ID, this::onSeasonStatePacketReceived);
	}

	@Override
	public void setSeasonProgress(float seasonProgress) {
		super.setSeasonProgress(seasonProgress);

		@NotNull var client = MinecraftClient.getInstance();
		if (client.world == null) {
			throw new IllegalStateException("Error occurred while setting the season's progress: client.world == null.");
		}

		RenderingUtil.reloadChunkColors(client.world);
	}

	private void onSeasonStatePacketReceived(@NotNull SeasonStatePayload payload, @NotNull ClientPlayNetworking.Context context) {
		@SuppressWarnings("resource")
		@Nullable var client = context.client();
		if (client == null || client.world == null) {
			return;
		}

		final var seasonId = payload.season();
		final var seasonProgress = payload.seasonProgress();
		client.executeSync(() -> {
			this.setSeason(seasonId);
			this.setSeasonProgress(seasonProgress);
		});
	}
}
