package io.github.steveplays28.simpleseasons.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.steveplays28.simpleseasons.config.SimpleSeasonsConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SimpleSeasonsClientModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> SimpleSeasonsConfig.HANDLER.generateGui().generateScreen(parent);
	}
}
