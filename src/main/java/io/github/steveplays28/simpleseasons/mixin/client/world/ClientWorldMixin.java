package io.github.steveplays28.simpleseasons.mixin.client.world;

import io.github.steveplays28.simpleseasons.client.color.world.SimpleSeasonsClientWorldColorCache;
import io.github.steveplays28.simpleseasons.client.extension.world.ClientWorldExtension;
import io.github.steveplays28.simpleseasons.client.world.event.ClientWorldEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World implements ClientWorldExtension {
	@Unique
	private final @NotNull SimpleSeasonsClientWorldColorCache simple_seasons$colorCache = new SimpleSeasonsClientWorldColorCache(
			(ClientWorld) (Object) this);

	protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(
				properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess,
				maxChainedNeighborUpdates
		);
	}

	@Override
	public @NotNull SimpleSeasonsClientWorldColorCache simple_seasons$getColorCache() {
		return simple_seasons$colorCache;
	}

	@Inject(method = "reloadColor", at = @At(value = "HEAD"))
	private void simple_seasons$invokeReloadColorEvent(@NotNull CallbackInfo ci) {
		ClientWorldEvents.COLOR_RELOAD.invoker().onColorReload((ClientWorld) (Object) this);
	}
}
