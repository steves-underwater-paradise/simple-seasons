package io.github.steveplays28.simpleseasons.client.state.world;

import io.github.steveplays28.simpleseasons.client.util.rendering.RenderingUtil;
import io.github.steveplays28.simpleseasons.state.world.SeasonTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.SEASON_PACKET_CHANNEL;

@Environment(EnvType.CLIENT)
public class ClientSeasonTracker extends SeasonTracker {
	public ClientSeasonTracker() {
		super();

		ClientPlayNetworking.registerGlobalReceiver(SEASON_PACKET_CHANNEL, this::onSeasonStatePacketReceived);
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

	private void onSeasonStatePacketReceived(@NotNull MinecraftClient client, @NotNull ClientPlayNetworkHandler clientPlayNetworkHandler, @NotNull PacketByteBuf buf, @NotNull PacketSender responseSender) {
		if (client.world == null) {
			return;
		}

		final var seasonId = buf.readInt();
		final var seasonProgress = buf.readFloat();
		client.executeSync(() -> {
			this.setSeason(seasonId);
			this.setSeasonProgress(seasonProgress);
		});
	}
}
