package net.threetag.palladium.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public abstract class EntityDependentInteger {

    public abstract int get(Entity entity);

    public boolean isDynamic() {
        return true;
    }

    public static EntityDependentInteger staticValue(int value) {
        return new Static(value);
    }

    public static EntityDependentInteger score(String objectiveName, int fallbackValue) {
        return new Score(objectiveName, fallbackValue);
    }

    public static EntityDependentInteger palladiumProperty(String propertyName, int fallbackValue) {
        return new PalladiumProperty(propertyName, fallbackValue);
    }

    public static EntityDependentInteger fromJson(JsonElement jsonElement, String memberName) {
        if (jsonElement.isJsonPrimitive()) {
            return staticValue(GsonHelper.convertToInt(jsonElement, memberName));
        } else if (jsonElement.isJsonObject()) {
            var json = GsonHelper.convertToJsonObject(jsonElement, memberName);
            var type = GsonHelper.getAsString(json, "type");

            if (type.equalsIgnoreCase("score")) {
                return score(GsonHelper.getAsString(json, "objective"), GsonHelper.getAsInt(json, "fallback", 0));
            } else if (type.equalsIgnoreCase("palladium_property")) {
                return palladiumProperty(GsonHelper.getAsString(json, "property"), GsonHelper.getAsInt(json, "fallback", 0));
            } else {
                throw new JsonParseException("Unknown type " + type + ", must be 'score' or 'palladium_property'");
            }
        } else {
            throw new JsonParseException(memberName + " must be either a primitive integer, or a JsonObject");
        }
    }

    public abstract JsonElement toJson();

    @Nullable
    public static EntityDependentInteger fromNBT(Tag tag) {
        if (tag instanceof IntTag intTag) {
            return staticValue(intTag.getAsInt());
        } else if (tag instanceof CompoundTag compoundTag) {
            var type = compoundTag.getString("Type");

            if (type.equalsIgnoreCase("score")) {
                return score(compoundTag.getString("Objective"), compoundTag.getInt("Fallback"));
            } else if (type.equalsIgnoreCase("palladium_property")) {
                return palladiumProperty(compoundTag.getString("Property"), compoundTag.getInt("Fallback"));
            }
        }
        return null;
    }

    public abstract Tag toNBT();

    public static EntityDependentInteger fromBuffer(FriendlyByteBuf buf) {
        int type = buf.readInt();

        if (type == 0) {
            return staticValue(buf.readInt());
        } else if (type == 1) {
            return score(buf.readUtf(), buf.readInt());
        } else {
            return palladiumProperty(buf.readUtf(), buf.readInt());
        }
    }

    public abstract void toBuffer(FriendlyByteBuf buf);

    private static class Static extends EntityDependentInteger {

        private final int value;

        public Static(int value) {
            this.value = value;
        }

        @Override
        public int get(Entity entity) {
            return this.value;
        }

        @Override
        public boolean isDynamic() {
            return false;
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.value);
        }

        @Override
        public Tag toNBT() {
            return IntTag.valueOf(this.value);
        }

        @Override
        public void toBuffer(FriendlyByteBuf buf) {
            buf.writeInt(0);
            buf.writeInt(this.value);
        }
    }

    private static class Score extends EntityDependentInteger {

        private final String objectiveName;
        private final int fallbackValue;

        public Score(String objectiveName, int fallbackValue) {
            this.objectiveName = objectiveName;
            this.fallbackValue = fallbackValue;
        }

        @Override
        public int get(Entity entity) {
            var scoreboard = entity.level().getScoreboard();
            var objective = scoreboard.getObjective(this.objectiveName);

            if (objective != null) {
                var scores = scoreboard.getPlayerScores(entity.getScoreboardName());
                return scores.containsKey(objective) ? scores.get(objective).getScore() : this.fallbackValue;
            }

            return this.fallbackValue;
        }

        @Override
        public JsonElement toJson() {
            var json = new JsonObject();
            json.addProperty("type", "score");
            json.addProperty("objective", this.objectiveName);
            if (this.fallbackValue != 0)
                json.addProperty("fallback", this.fallbackValue);
            return json;
        }

        @Override
        public Tag toNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("Type", "score");
            tag.putString("Objective", this.objectiveName);
            tag.putInt("Fallback", this.fallbackValue);
            return tag;
        }

        @Override
        public void toBuffer(FriendlyByteBuf buf) {
            buf.writeInt(1);
            buf.writeUtf(this.objectiveName);
            buf.writeInt(this.fallbackValue);
        }
    }

    private static class PalladiumProperty extends EntityDependentInteger {

        private final String propertyName;
        private final int fallbackValue;

        public PalladiumProperty(String propertyName, int fallbackValue) {
            this.propertyName = propertyName;
            this.fallbackValue = fallbackValue;
        }

        @Override
        public int get(Entity entity) {
            var opt = EntityPropertyHandler.getHandler(entity);

            if (opt.isPresent()) {
                var handler = opt.get();
                var property = handler.getPropertyByName(this.propertyName);

                if (property instanceof IntegerProperty integerProperty) {
                    Integer result = handler.get(integerProperty);
                    return result == null ? this.fallbackValue : result;
                }
            }

            return this.fallbackValue;
        }

        @Override
        public JsonElement toJson() {
            var json = new JsonObject();
            json.addProperty("type", "palladium_property");
            json.addProperty("property", this.propertyName);
            if (this.fallbackValue != 0)
                json.addProperty("fallback", this.fallbackValue);
            return json;
        }

        @Override
        public Tag toNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("Type", "palladium_property");
            tag.putString("Property", this.propertyName);
            tag.putInt("Fallback", this.fallbackValue);
            return tag;
        }

        @Override
        public void toBuffer(FriendlyByteBuf buf) {
            buf.writeInt(2);
            buf.writeUtf(this.propertyName);
            buf.writeInt(this.fallbackValue);
        }
    }

}
