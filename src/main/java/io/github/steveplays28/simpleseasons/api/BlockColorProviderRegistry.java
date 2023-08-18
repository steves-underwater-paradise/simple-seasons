package io.github.steveplays28.simpleseasons.api;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * An endpoint for registering block color providers. Use this to make your block's colors change dynamically depending on the season.
 * Your block's texture is required to be grayscale and your block's model is required to use `tintindex: 0`, or set leaves as the parent, for this feature to work.
 */
public class BlockColorProviderRegistry {
	private static final List<Block> registeredBlocks = new ArrayList<>();

	@SuppressWarnings("unused")
	public static void registerBlock(Block block) {
		registeredBlocks.add(block);
	}

	public static void registerBlocks(List<Block> blocks) {
		registeredBlocks.addAll(blocks);
	}

	public static List<Block> getRegisteredBlocks() {
		return registeredBlocks;
	}
}
