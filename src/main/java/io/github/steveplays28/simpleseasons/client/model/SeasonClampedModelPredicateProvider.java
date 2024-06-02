package io.github.steveplays28.simpleseasons.client.model;

import io.github.steveplays28.simpleseasons.api.SimpleSeasonsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SeasonClampedModelPredicateProvider implements ClampedModelPredicateProvider {
	@Override
	public float unclampedCall(@NotNull ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity entity, int seed) {
		if (clientWorld == null || !SimpleSeasonsApi.worldHasSeasons(clientWorld)) {
			return 0f;
		}

		return SimpleSeasonsApi.getSeason(clientWorld).getId() + SimpleSeasonsApi.getSeasonProgress(clientWorld);
	}
}
