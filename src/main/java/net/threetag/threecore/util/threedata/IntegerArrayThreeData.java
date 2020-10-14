package net.threetag.threecore.util.threedata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.JSONUtils;

import java.util.Arrays;

public class IntegerArrayThreeData extends ThreeData<Integer[]> {

	public IntegerArrayThreeData(String key) {
		super(key);
	}

	@Override
	public Integer[] parseValue(JsonObject jsonObject, Integer[] defaultValue) {
		if (!JSONUtils.hasField(jsonObject, this.jsonKey))
			return defaultValue;

		JsonElement element = jsonObject.get(this.jsonKey);

		if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
			return new Integer[]{ element.getAsInt() };
		} else {
			JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, this.jsonKey);
			Integer[] array = new Integer[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				array[i] = jsonArray.get(i).getAsInt();
			}

			return array;
		}
	}

	@Override
	public void writeToNBT(CompoundNBT nbt, Integer[] value) {
		nbt.put(this.key, new IntArrayNBT(Arrays.asList(value)));
	}

	@Override
	public Integer[] readFromNBT(CompoundNBT nbt, Integer[] defaultValue) {
		if (!nbt.contains(this.key))
			return defaultValue;

		int[] array = nbt.getIntArray(this.key);
		Integer[] integerArray = new Integer[array.length];

		for (int i = 0; i < array.length; i++)
			integerArray[i] = array[i];

		return integerArray;
	}

	@Override
	public JsonElement serializeJson(Integer[] value) {
		if (value.length == 1) {
			return new JsonPrimitive(value[0]);
		} else {
			JsonArray jsonArray = new JsonArray();
			for (Integer i : value) {
				jsonArray.add(i);
			}
			return jsonArray;
		}

	}
}
