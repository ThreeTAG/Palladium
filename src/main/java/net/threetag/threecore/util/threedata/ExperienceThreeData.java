package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.INBTSerializable;

public class ExperienceThreeData extends ThreeData<ExperienceThreeData.Experience> {

    public ExperienceThreeData(String key) {
        super(key);
    }

    @Override
    public Experience parseValue(JsonObject jsonObject, Experience defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        JsonPrimitive primitive = jsonObject.get(this.key).getAsJsonPrimitive();
        return new Experience(primitive);
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Experience value) {
        nbt.put(this.key, value.serializeNBT());
    }

    @Override
    public Experience readFromNBT(CompoundNBT nbt, Experience defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        CompoundNBT tag = nbt.getCompound(this.key);
        return new Experience(tag.getBoolean("Levels"), tag.getInt("Value"));
    }

    @Override
    public JsonElement serializeJson(Experience value) {
        if (value.isLevels()) {
            return new JsonPrimitive(value.getValue() + "L");
        } else {
            return new JsonPrimitive(value.getValue());
        }
    }

    public static class Experience implements INBTSerializable<CompoundNBT> {

        protected boolean levels;
        protected int value;

        public Experience(boolean levels, int value) {
            this.levels = levels;
            this.value = value;
        }

        public Experience(CompoundNBT nbt) {
            this.deserializeNBT(nbt);
        }

        public Experience(JsonPrimitive primitive) {
            this.parseFromJson(primitive);
        }

        public int getValue() {
            return value;
        }

        public boolean isLevels() {
            return levels;
        }

        public boolean has(PlayerEntity entity) {
            if (isLevels())
                return entity.experienceLevel >= value;
            else
                return entity.experienceTotal >= value;
        }

        public void take(PlayerEntity entity) {
            if (isLevels())
                entity.addExperienceLevel(-value);
            else
                entity.giveExperiencePoints(-value);
        }

        public void parseFromJson(JsonPrimitive primitive) {
            if (primitive.isString()) {
                String s = primitive.getAsString();
                this.levels = s.endsWith("L");
                this.value = Integer.parseInt(s.replaceAll("L", ""));
            } else {
                this.levels = false;
                this.value = primitive.getAsInt();
            }
        }

        public JsonObject writeToJson() {
            JsonObject jsonObject = new JsonObject();
            if (this.levels) {
                jsonObject.addProperty("experience", this.value + "L");
            } else {
                jsonObject.addProperty("experience", this.value);
            }
            return jsonObject;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("Levels", this.levels);
            nbt.putInt("Value", this.value);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            this.levels = nbt.getBoolean("Levels");
            this.value = nbt.getInt("Value");
        }
    }

}
