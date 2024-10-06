package io.github.steveplays28.simpleseasons.server.state;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import io.github.steveplays28.simpleseasons.server.state.world.ServerWorldSeasonState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record SeasonStatePayload(@NotNull ServerWorldSeasonState serverWorldSeasonState) implements CustomPayload {
	public static final CustomPayload.Id<SeasonStatePayload> PACKET_ID = new CustomPayload.Id<>(Identifier.of(SimpleSeasons.MOD_ID, "season_state"));
	public static final PacketCodec<RegistryByteBuf, SeasonStatePayload> PACKET_CODEC = PacketCodec.of(SeasonStatePayload::write, SeasonStatePayload::read);

	public void write(RegistryByteBuf buf) {
		buf.writeInt(serverWorldSeasonState.season);
		buf.writeFloat(serverWorldSeasonState.seasonProgress);
	}

	public static SeasonStatePayload read(RegistryByteBuf buf) {
		return new SeasonStatePayload(
				new ServerWorldSeasonState(
						buf.readInt(),
						buf.readFloat()
				)
		);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
