package io.github.steveplays28.simpleseasons.mixin.accessor;

import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkRendererRegion.class)
public interface ChunkRendererRegionAccessor {
	@Accessor("world")
	World getWorld();
}
