package net.threetag.palladium.power;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.IconSerializer;
import net.threetag.palladium.util.json.GsonUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Power {

    private final ResourceLocation id;
    private final Component name;
    private final IIcon icon;
    private final List<AbilityConfiguration> abilities = new ArrayList<>();
    private final TextureReference background;
    private final TextureReference abilityBar;
    private final Color primaryColor, secondaryColor;
    private final boolean persistentData;
    private final boolean hidden;
    private final GuiDisplayType guiDisplayType;
    private boolean invalid = false;

    public Power(ResourceLocation id, Component name, IIcon icon, TextureReference background, TextureReference abilityBar, Color primaryColor, Color secondaryColor, boolean persistentData, boolean hidden, GuiDisplayType guiDisplayType) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.background = background;
        this.abilityBar = abilityBar;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.persistentData = persistentData;
        this.hidden = hidden;
        this.guiDisplayType = guiDisplayType;
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

    public TextureReference getBackground() {
        return background;
    }

    public TextureReference getAbilityBarTexture() {
        return abilityBar;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public boolean hasPersistentData() {
        return this.persistentData;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public GuiDisplayType getGuiDisplayType() {
        return this.guiDisplayType;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeComponent(this.name);
        buf.writeNbt(IconSerializer.serializeNBT(this.icon));
        buf.writeBoolean(this.background != null);
        if (this.background != null) {
            this.background.toBuffer(buf);
        }
        buf.writeBoolean(this.abilityBar != null);
        if (this.abilityBar != null) {
            this.abilityBar.toBuffer(buf);
        }
        buf.writeInt(this.primaryColor.getRGB());
        buf.writeInt(this.secondaryColor.getRGB());
        buf.writeBoolean(this.persistentData);
        buf.writeBoolean(this.hidden);
        buf.writeInt(this.guiDisplayType.ordinal());
        buf.writeInt(this.abilities.size());
        for (AbilityConfiguration configuration : this.abilities) {
            configuration.toBuffer(buf);
        }
    }

    public static Power fromBuffer(ResourceLocation id, FriendlyByteBuf buf) {
        Power power = new Power(id, buf.readComponent(), IconSerializer.parseNBT(Objects.requireNonNull(buf.readNbt())), buf.readBoolean() ? TextureReference.fromBuffer(buf) : null, buf.readBoolean() ? TextureReference.fromBuffer(buf) : null, new Color(buf.readInt()), new Color(buf.readInt()), buf.readBoolean(), buf.readBoolean(), GuiDisplayType.values()[buf.readInt()]);
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            power.addAbility(AbilityConfiguration.fromBuffer(buf));
        }

        return power;
    }

    public static Power fromJSON(ResourceLocation id, JsonObject json) {
        Component name = Component.Serializer.fromJson(json.get("name"));
        TextureReference background = GsonUtil.getAsTextureReference(json, "background", null);
        TextureReference abilityBarTexture = GsonUtil.getAsTextureReference(json, "ability_bar_texture", null);
        GuiDisplayType displayType = GuiDisplayType.getByName(GsonHelper.getAsString(json, "gui_display_type", "list"));

        if (displayType == null) {
            throw new JsonParseException("Unknown gui display type '" + GsonHelper.getAsString(json, "gui_display_type", "list") + "'");
        }

        Power power = new Power(id,
                name,
                IconSerializer.parseJSON(json.get("icon")),
                background,
                abilityBarTexture,
                GsonUtil.getAsColor(json, "primary_color", new Color(210, 112, 49)),
                GsonUtil.getAsColor(json, "secondary_color", new Color(126, 97, 86)),
                GsonHelper.getAsBoolean(json, "persistent_data", false),
                GsonHelper.getAsBoolean(json, "hidden", false),
                displayType
        );

        if (GsonHelper.isValidNode(json, "abilities")) {
            JsonObject abilities = GsonHelper.getAsJsonObject(json, "abilities");

            for (String key : abilities.keySet()) {
                power.addAbility(AbilityConfiguration.fromJSON(key, GsonHelper.getAsJsonObject(abilities, key)));
            }
        }

        return power;
    }

    public enum GuiDisplayType {

        TREE("tree"),
        LIST("list");

        private final String name;

        GuiDisplayType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static GuiDisplayType getByName(String name) {
            for (GuiDisplayType value : values()) {
                if (value.getName().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }
    }

}
