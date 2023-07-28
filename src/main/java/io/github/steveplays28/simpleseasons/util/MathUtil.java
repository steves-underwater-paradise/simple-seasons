package io.github.steveplays28.simpleseasons.util;

public class MathUtil {
	public static int clamp(int value, int min, int max) {
		if (value < min) {
			value = min;
		} else if (value > max) {
			value = max;
		}

		return value;
	}
}
