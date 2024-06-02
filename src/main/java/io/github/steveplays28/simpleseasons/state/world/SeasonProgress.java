package io.github.steveplays28.simpleseasons.state.world;

public class SeasonProgress {
	private float progress = 0f;

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public void resetProgress() {
		this.progress = 0f;
	}
}
