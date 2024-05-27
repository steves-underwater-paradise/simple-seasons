package io.github.steveplays28.simpleseasons.client.state;

import io.github.steveplays28.simpleseasons.client.util.rendering.RenderingUtil;
import io.github.steveplays28.simpleseasons.state.SeasonTracker;
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

	private void onSeasonStatePacketReceived(@NotNull MinecraftClient client, @NotNull ClientPlayNetworkHandler clientPlayNetworkHandler, @NotNull PacketByteBuf buf, @NotNull PacketSender responseSender) {
		if (client.world == null) {
			return;
		}

		this.setSeason(buf.readInt());
		RenderingUtil.reloadChunkColors(client.world);
	}
}
