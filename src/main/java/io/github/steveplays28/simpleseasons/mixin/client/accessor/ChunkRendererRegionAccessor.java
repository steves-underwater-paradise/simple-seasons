package io.github.steveplays28.simpleseasons.mixin.client.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRendererRegion.class)
public interface ChunkRendererRegionAccessor {
	@Accessor("world")
	World getWorld();
}
