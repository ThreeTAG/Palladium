package net.threetag.palladium.power;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.IconSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Power {

    private final ResourceLocation id;
    private final Component name;
    private final IIcon icon;
    private final List<AbilityConfiguration> abilities = new ArrayList<>();
    private boolean invalid = false;

    public Power(ResourceLocation id, Component name, IIcon icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public void invalidate() {
        this.invalid = true;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public Power addAbility(AbilityConfiguration configuration) {
        this.abilities.add(configuration);
        return this;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public IIcon getIcon() {
        return icon;
    }

    public List<AbilityConfiguration> getAbilities() {
        return abilities;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeComponent(this.name);
        buf.writeNbt(IconSerializer.serializeNBT(this.icon));
        buf.writeInt(this.abilities.size());
        for (AbilityConfiguration configuration : this.abilities) {
            configuration.toBuffer(buf);
        }
    }

    public static Power fromBuffer(ResourceLocation id, FriendlyByteBuf buf) {
        Power power = new Power(id, buf.readComponent(), IconSerializer.parseNBT(Objects.requireNonNull(buf.readNbt())));
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            power.addAbility(AbilityConfiguration.fromBuffer(buf));
        }

        return power;
    }

    public static Power fromJSON(ResourceLocation id, JsonObject json) {
        Component name = Component.Serializer.fromJson(json.get("name"));
        Power power = new Power(id, name, IconSerializer.parseJSON(GsonHelper.getAsJsonObject(json, "icon")));

        if (GsonHelper.isValidNode(json, "abilities")) {
            JsonObject abilities = GsonHelper.getAsJsonObject(json, "abilities");

            for (String key : abilities.keySet()) {
                power.addAbility(AbilityConfiguration.fromJSON(key, GsonHelper.getAsJsonObject(abilities, key)));
            }
        }

        return power;
    }

}
