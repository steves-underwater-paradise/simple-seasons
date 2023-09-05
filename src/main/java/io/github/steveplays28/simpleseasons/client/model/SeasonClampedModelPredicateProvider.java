package io.github.steveplays28.simpleseasons.client.model;

import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient.season;

public class SeasonClampedModelPredicateProvider implements ClampedModelPredicateProvider {
	@Override
	public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
		return season;
	}
}
