package io.github.steveplays28.simpleseasons.state.world;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record SeasonStatePayload(int season, float seasonProgress) implements CustomPayload {
	public static final @NotNull CustomPayload.Id<SeasonStatePayload> PACKET_ID = new CustomPayload.Id<>(
			Identifier.of(SimpleSeasons.MOD_ID, "season_state"));
	public static final @NotNull PacketCodec<RegistryByteBuf, SeasonStatePayload> PACKET_CODEC = PacketCodec.of(
			SeasonStatePayload::write, SeasonStatePayload::read);

	public void write(@NotNull RegistryByteBuf buf) {
		buf.writeInt(season);
		buf.writeFloat(seasonProgress);
	}

	@Contract("_ -> new")
	public static @NotNull SeasonStatePayload read(@NotNull RegistryByteBuf buf) {
		return new SeasonStatePayload(buf.readInt(), buf.readFloat());
	}

	@Override
	public @NotNull Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
