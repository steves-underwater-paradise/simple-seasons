package io.github.steveplays28.simpleseasons.client.world.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ClientWorldEvents {
	public static final Event<ColorReloadCallback> COLOR_RELOAD = EventFactory.createArrayBacked(
			ColorReloadCallback.class, callbacks -> clientWorld -> {
				for (var callback : callbacks) {
					callback.onColorReload(clientWorld);
				}
			}
	);

	@FunctionalInterface
	public interface ColorReloadCallback {
		void onColorReload(@NotNull ClientWorld clientWorld);
	}
}
