package io.github.steveplays28.simpleseasons.util;

import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.simpleseasons.util.MathUtil.clamp;

/**
 * Color class that takes in red, green and blue values between 0-255. Values outside of this range are truncated.
 */
public class Color {
	public int red;
	public int green;
	public int blue;

	public Color(int red, int green, int blue) {
		this.red = clamp(red, 0, 255);
		this.green = clamp(green, 0, 255);
		this.blue = clamp(blue, 0, 255);
	}

	public Color(int hex) {
		red = (hex >> 16) & 0xFF;
		green = (hex >> 8) & 0xFF;
		blue = hex & 0xFF;
	}

	public Color(@NotNull Color color) {
		red = color.red;
		green = color.green;
		blue = color.blue;
	}

	@Override
	public String toString() {
		return String.format("Color(%s, %s, %s)", red, green, blue);
	}

	public int toInt() {
		int hex = red;
		hex = (hex << 8) + green;
		hex = (hex << 8) + blue;

		return hex;
	}

	public Color multiply(@NotNull Color color) {
		var newColor = new Color(this);

		newColor.red = clamp(newColor.red * color.red, 0, 255);
		newColor.green = clamp(newColor.green * color.green, 0, 255);
		newColor.blue = clamp(newColor.blue * color.blue, 0, 255);

		return newColor;
	}

	public Color add(@NotNull Color color) {
		var newColor = new Color(this);

		newColor.red = clamp(newColor.red + color.red, 0, 255);
		newColor.green = clamp(newColor.green + color.green, 0, 255);
		newColor.blue = clamp(newColor.blue + color.blue, 0, 255);

		return newColor;
	}

	public Color subtract(@NotNull Color color) {
		var newColor = new Color(this);

		newColor.red = clamp(newColor.red - color.red, 0, 255);
		newColor.green = clamp(newColor.green - color.green, 0, 255);
		newColor.blue = clamp(newColor.blue - color.blue, 0, 255);

		return newColor;
	}

	public Color invert() {
		var newColor = new Color(this);

		newColor.red = clamp(255 - this.red, 0, 255);
		newColor.green = clamp(255 - this.green, 0, 255);
		newColor.blue = clamp(255 - this.blue, 0, 255);

		return newColor;
	}

	public Color lerp(@NotNull Color endColor, float t) {
		var newColor = new Color(this);

		newColor.red = MathUtil.lerp(this.red, endColor.red, t);
		newColor.green = MathUtil.lerp(this.green, endColor.green, t);
		newColor.blue = MathUtil.lerp(this.blue, endColor.blue, t);

		return newColor;
	}
}
