package io.github.steveplays28.simpleseasons;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.Objects;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

public class SimpleSeasonsState extends PersistentState {
	public static final String SEASON_KEY = "season";
	public static final String LAST_SEASON_CHANGE_TIME_KEY = "last_season_change_time";

	public int season;
	public long lastSeasonChangeTime;

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt(SEASON_KEY, season);
		nbt.putLong(LAST_SEASON_CHANGE_TIME_KEY, lastSeasonChangeTime);
		return nbt;
	}

	public static SimpleSeasonsState createFromNbt(NbtCompound tag) {
		SimpleSeasonsState simpleSeasonsState = new SimpleSeasonsState();
		simpleSeasonsState.season = tag.getInt(SEASON_KEY);
		simpleSeasonsState.lastSeasonChangeTime = tag.getLong(LAST_SEASON_CHANGE_TIME_KEY);

		return simpleSeasonsState;
	}

	public static SimpleSeasonsState getServerState(MinecraftServer server) {
		// First we get the persistentStateManager for the OVERWORLD
		PersistentStateManager persistentStateManager = Objects.requireNonNull(
				server.getWorld(World.OVERWORLD)).getPersistentStateManager();

		// Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
		return persistentStateManager.getOrCreate(SimpleSeasonsState::createFromNbt, SimpleSeasonsState::new, MOD_ID);
	}
}
