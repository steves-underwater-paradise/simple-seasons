package io.github.steveplays28.simpleseasons.server.util.time;

/**
 * Utility class for interacting with Minecraft's day/night cycle and time system.
 */
// TODO: Add compatibility with (library) mods that allow changing the length of the day/night cycle
public class TimeUtil {
	private static final long TICKS_PER_SECOND = 20;
	private static final long DAY_NIGHT_CYCLE_LENGTH_TICKS = 24000;
	private static final double YEAR_LENGTH_DAYS = 365.25d;

	/**
	 * Gets the amount of ticks a Minecraft server processes per second.
	 *
	 * @return The amount of ticks a Minecraft server processes per second.
	 */
	public static long getTicksPerSecond() {
		return TICKS_PER_SECOND;
	}

	/**
	 * Gets the length of Minecraft's day/night cycle, in ticks.
	 *
	 * @return The length of the day/night cycle, in ticks.
	 */
	public static long getDayNightCycleLengthTicks() {
		return DAY_NIGHT_CYCLE_LENGTH_TICKS;
	}

	/**
	 * Gets the length of Minecraft's day/night cycle, in seconds.
	 *
	 * @return The length of the day/night cycle, in seconds.
	 */
	public static long getDayNightCycleLengthSeconds() {
		return getDayNightCycleLengthTicks() / getTicksPerSecond();
	}

	/**
	 * Gets the length of a year, in days.
	 *
	 * @return The length of a year, in days.
	 */
	public static double getYearLengthDays() {
		return YEAR_LENGTH_DAYS;
	}
}
