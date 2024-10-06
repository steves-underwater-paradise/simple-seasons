package io.github.steveplays28.simpleseasons.state.world;

import io.github.steveplays28.simpleseasons.SimpleSeasons;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record SeasonStatePacket(int season, float seasonProgress) implements FabricPacket {
	public static final @NotNull PacketType<SeasonStatePacket> PACKET_TYPE = PacketType.create(
			new Identifier(SimpleSeasons.MOD_ID, "season_state"), SeasonStatePacket::new);

	public SeasonStatePacket(PacketByteBuf buf) {
		this(buf.readInt(), buf.readFloat());
	}

	@Override
	public void write(@NotNull PacketByteBuf buf) {
		buf.writeInt(season);
		buf.writeFloat(seasonProgress);
	}

	@Override
	public @NotNull PacketType<?> getType() {
		return PACKET_TYPE;
	}
}
