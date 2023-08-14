package net.threetag.palladium.power.ability;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class AbilityDescription {

    private final Component lockedDescription;
    private final Component unlockedDescription;

    public AbilityDescription(Component lockedDescription, Component unlockedDescription) {
        this.lockedDescription = lockedDescription;
        this.unlockedDescription = unlockedDescription;
    }

    public AbilityDescription(Component description) {
        this.lockedDescription = this.unlockedDescription = description;
    }

    public Component get(boolean unlocked) {
        return unlocked ? this.unlockedDescription : this.lockedDescription;
    }

    public static AbilityDescription fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new AbilityDescription(Component.Serializer.fromJson(jsonElement));
        } else if (jsonElement.isJsonObject()) {
            var obj = jsonElement.getAsJsonObject();

            if (obj.has("locked") && obj.has("unlocked")) {
                return new AbilityDescription(
                        Component.Serializer.fromJson(obj.get("locked")),
                        Component.Serializer.fromJson(obj.get("unlocked"))
                );
            } else {
                return new AbilityDescription(Component.Serializer.fromJson(jsonElement));
            }
        } else {
            return new AbilityDescription(Component.Serializer.fromJson(jsonElement));
        }
    }

    public JsonElement toJson() {
        if (this.lockedDescription == this.unlockedDescription) {
            return Component.Serializer.toJsonTree(this.lockedDescription);
        } else {
            JsonObject json = new JsonObject();
            json.add("locked", Component.Serializer.toJsonTree(this.lockedDescription));
            json.add("unlocked", Component.Serializer.toJsonTree(this.unlockedDescription));
            return json;
        }
    }

    public static AbilityDescription fromNbt(CompoundTag nbt) {
        var locked = nbt.getString("Locked");
        var unlocked = nbt.getString("Unlocked");

        if (locked.equals(unlocked)) {
            return new AbilityDescription(Component.Serializer.fromJson(locked));
        } else {
            return new AbilityDescription(Component.Serializer.fromJson(locked), Component.Serializer.fromJson(unlocked));
        }
    }

    public CompoundTag toNbt() {
        var nbt = new CompoundTag();
        nbt.putString("Locked", Component.Serializer.toJson(this.lockedDescription));
        nbt.putString("Unlocked", Component.Serializer.toJson(this.unlockedDescription));
        return nbt;
    }

    public static AbilityDescription fromBuffer(FriendlyByteBuf buf) {
        boolean same = buf.readBoolean();
        if (same) {
            return new AbilityDescription(buf.readComponent());
        } else {
            return new AbilityDescription(buf.readComponent(), buf.readComponent());
        }
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeBoolean(this.lockedDescription == this.unlockedDescription);
        buf.writeComponent(this.lockedDescription);
        if (this.lockedDescription != this.unlockedDescription) {
            buf.writeComponent(this.unlockedDescription);
        }
    }

}
