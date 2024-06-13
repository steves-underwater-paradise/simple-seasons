package io.github.steveplays28.simpleseasons.client.resource.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BlockItemBiomeSeasonColorsDeserializer implements JsonDeserializer<BlockItemBiomeSeasonColors> {
	/**
	 * Gson invokes this call-back method during deserialization when it encounters a field of the
	 * specified type.
	 * <p>In the implementation of this call-back method, you should consider invoking
	 * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
	 * for any non-trivial field of the returned object. However, you should never invoke it on the
	 * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
	 * call-back method again).
	 *
	 * @param json    The Json data being deserialized.
	 * @param typeOfT The type of the Object to deserialize to.
	 * @param context The {@link JsonDeserializationContext} of the Json data being deserialized.
	 * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}.
	 * @throws JsonParseException if json is not in the expected format of {@code typeofT}.
	 */
	@Override
	public BlockItemBiomeSeasonColors deserialize(@NotNull JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
		@Nullable var biomeSeasonColorsArray = json.getAsJsonObject().getAsJsonArray("biomes");
		if (biomeSeasonColorsArray == null) {
			return new BlockItemBiomeSeasonColors();
		}

		List<BlockItemBiomeSeasonColors.BiomeSeasonColors> biomeSeasonColors = new ArrayList<>();
		for (int i = 0; i < biomeSeasonColorsArray.size(); i++) {
			biomeSeasonColors.add(context.deserialize(biomeSeasonColorsArray.get(i), BlockItemBiomeSeasonColors.BiomeSeasonColors.class));
		}

		return new BlockItemBiomeSeasonColors(biomeSeasonColors);
	}
}
