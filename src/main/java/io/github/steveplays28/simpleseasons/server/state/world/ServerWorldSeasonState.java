package io.github.steveplays28.simpleseasons.server.state.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.simpleseasons.SimpleSeasons.MOD_ID;

public class ServerWorldSeasonState extends PersistentState {
	public static final Type<ServerWorldSeasonState> TYPE = new PersistentState.Type<>(
			ServerWorldSeasonState::new,
			(nbt, lookup) -> createFromNbt(nbt),
			null
	);

	public static final String SEASON_KEY = "season";
	public static final String SEASON_PROGRESS_KEY = "season_progress";

	public int season;
	public float seasonProgress;

	public ServerWorldSeasonState() {

	}

	public ServerWorldSeasonState(int season, float seasonProgress) {
		this.season = season;
		this.seasonProgress = seasonProgress;
	}

	@Override
	public NbtCompound writeNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
		nbt.putInt(SEASON_KEY, season);
		nbt.putFloat(SEASON_PROGRESS_KEY, seasonProgress);
		return nbt;
	}

	/**
	 * Creates a {@link ServerWorldSeasonState} from an {@link NbtCompound}.
	 *
	 * @param tag The {@link NbtCompound} from which to create the {@link ServerWorldSeasonState}.
	 * @return The {@link ServerWorldSeasonState} created from the specified {@link NbtCompound}.
	 */
	public static @NotNull ServerWorldSeasonState createFromNbt(@NotNull NbtCompound tag) {
		@NotNull var worldSeasonState = new ServerWorldSeasonState();
		worldSeasonState.season = tag.getInt(SEASON_KEY);
		worldSeasonState.seasonProgress = tag.getFloat(SEASON_PROGRESS_KEY);
		return worldSeasonState;
	}

	/**
	 * Reads the {@link ServerWorldSeasonState} file from the disk if it exists, otherwise creates a new file and saves it to the disk.
	 *
	 * @param persistentStateManager The {@link PersistentStateManager} of the {@link ServerWorld} for which the season state should be fetched.
	 * @return The season state that was fetched for the specified {@link ServerWorld} from its {@link PersistentStateManager}.
	 */
	public static @NotNull ServerWorldSeasonState getOrCreate(@NotNull PersistentStateManager persistentStateManager) {
		return persistentStateManager.getOrCreate(TYPE, MOD_ID);
	}
}
