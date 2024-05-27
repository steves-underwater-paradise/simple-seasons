package io.github.steveplays28.simpleseasons.client.model;

import io.github.steveplays28.simpleseasons.client.SimpleSeasonsClient;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SeasonClampedModelPredicateProvider implements ClampedModelPredicateProvider {
	@Override
	public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
		return SimpleSeasonsClient.seasonTracker.getSeason().getId();
	}
}
