package io.github.steveplays28.simpleseasons.state;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

// TODO: Make SeasonState be per world
public class SeasonState extends PersistentState {
	public static final String SEASON_KEY = "season";
	public static final String SEASON_PROGRESS_KEY = "season_progress";

	public int season;
	public float seasonProgress;

	@Override
	public NbtCompound writeNbt(@NotNull NbtCompound nbt) {
		nbt.putInt(SEASON_KEY, season);
		nbt.putFloat(SEASON_PROGRESS_KEY, seasonProgress);
		return nbt;
	}

	public static @NotNull SeasonState createFromNbt(@NotNull NbtCompound tag) {
		SeasonState seasonState = new SeasonState();
		seasonState.season = tag.getInt(SEASON_KEY);
		seasonState.seasonProgress = tag.getFloat(SEASON_PROGRESS_KEY);
		return seasonState;
	}

	public static SeasonState getServerState(@NotNull MinecraftServer server) {
		// First we get the persistentStateManager for the OVERWORLD
		PersistentStateManager persistentStateManager = Objects.requireNonNull(
				server.getWorld(World.OVERWORLD)).getPersistentStateManager();

		// Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
		return persistentStateManager.getOrCreate(SeasonState::createFromNbt, SeasonState::new, MOD_ID);
	}
}
