package io.github.steveplays28.simpleseasons.client.extension.world;

import io.github.steveplays28.simpleseasons.client.color.world.SimpleSeasonsClientWorldColorCache;
import org.jetbrains.annotations.NotNull;

public interface ClientWorldExtension {
	@NotNull SimpleSeasonsClientWorldColorCache simple_seasons$getColorCache();
}
