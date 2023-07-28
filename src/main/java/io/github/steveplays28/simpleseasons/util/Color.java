package io.github.steveplays28.simpleseasons.util;

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

	public Color(Color color) {
		red = color.red;
		green = color.green;
		blue = color.blue;
	}

	public int toInt() {
		int hex = red;
		hex = (hex << 8) + green;
		hex = (hex << 8) + blue;

		return hex;
	}

	public Color multiply(Color color) {
		var newColor = new Color(color);

		newColor.red = clamp(newColor.red * color.red, 0, 255);
		newColor.green = clamp(newColor.green * color.green, 0, 255);
		newColor.blue = clamp(newColor.blue * color.blue, 0, 255);

		return newColor;
	}

	public Color add(Color color) {
		var newColor = new Color(color);

		newColor.red = clamp(newColor.red + color.red, 0, 255);
		newColor.green = clamp(newColor.green + color.green, 0, 255);
		newColor.blue = clamp(newColor.blue + color.blue, 0, 255);

		return newColor;
	}

	public Color subtract(Color color) {
		var newColor = new Color(color);

		newColor.red = clamp(newColor.red - color.red, 0, 255);
		newColor.green = clamp(newColor.green - color.green, 0, 255);
		newColor.blue = clamp(newColor.blue - color.blue, 0, 255);

		return newColor;
	}
}
