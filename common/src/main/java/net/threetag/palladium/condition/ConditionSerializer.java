package net.threetag.palladium.condition;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDefaultDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladiumcore.registry.PalladiumRegistry;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ConditionSerializer implements IDefaultDocumentedConfigurable {

    public static final PalladiumRegistry<ConditionSerializer> REGISTRY = PalladiumRegistry.create(ConditionSerializer.class, Palladium.id("condition_serializer"));

    final PropertyManager propertyManager = new PropertyManager();
    public static ConditionEnvironment CURRENT_CONTEXT = ConditionEnvironment.ALL;

    public <T> ConditionSerializer withProperty(PalladiumProperty<T> data, T value) {
        this.propertyManager.register(data, value);
        return this;
    }

    public <T> T getProperty(JsonObject json, PalladiumProperty<T> data) {
        if (this.propertyManager.isRegistered(data)) {
            if (json.has(data.getKey())) {
                JsonElement jsonElement = json.get(data.getKey());
                if (jsonElement.isJsonPrimitive() && jsonElement.getAsString().equals("null")) {
                    return null;
                } else {
                    return data.fromJSON(json.get(data.getKey()));
                }
            } else {
                return this.propertyManager.get(data);
            }
        } else {
            throw new RuntimeException("Condition Serializer does not have " + data.getKey() + " data!");
        }
    }


    public abstract Condition make(JsonObject json);

    public Condition make(JsonObject json, ConditionEnvironment type) {
        return this.make(json);
    }

    public ConditionEnvironment getContextEnvironment() {
        return ConditionEnvironment.ALL;
    }

    public static List<Condition> listFromJSON(JsonElement jsonElement, ConditionEnvironment type) {
        List<Condition> conditions = new ArrayList<>();

        if (jsonElement.isJsonPrimitive()) {
            conditions.add(fromJSON(jsonElement, type));
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            for (JsonElement element : array) {
                conditions.add(fromJSON(element.getAsJsonObject(), type));
            }
        } else if (jsonElement.isJsonObject()) {
            conditions.add(fromJSON(jsonElement.getAsJsonObject(), type));
        }

        return conditions;
    }

    public static Condition fromJSON(JsonElement jsonElement, ConditionEnvironment type) {
        if (jsonElement.isJsonPrimitive()) {
            boolean result = GsonHelper.convertToBoolean(jsonElement, "conditions");
            return result ? new TrueCondition() : new FalseCondition();
        } else {
            var json = GsonHelper.convertToJsonObject(jsonElement, "conditions");
            var id = new ResourceLocation(GsonHelper.getAsString(json, "type"));
            ConditionSerializer conditionSerializer = ConditionSerializer.REGISTRY.get(id);

            if (conditionSerializer == null && id.equals(Palladium.id("under_water"))) {
                conditionSerializer = ConditionSerializers.IS_UNDER_WATER.get();
                AddonPackLog.warning("'under_water' condition found, please use 'is_under_water' instead!");
            }

            if (conditionSerializer == null) {
                throw new JsonParseException("Condition Serializer '" + GsonHelper.getAsString(json, "type") + "' does not exist!");
            }

            if ((type == ConditionEnvironment.DATA && !conditionSerializer.getContextEnvironment().forAbilities()) || (type == ConditionEnvironment.ASSETS && !conditionSerializer.getContextEnvironment().forRenderLayers())) {
                throw new JsonParseException("Condition Serializer '" + GsonHelper.getAsString(json, "type") + "' is not applicable for " + type.toString().toLowerCase(Locale.ROOT));
            }

            return conditionSerializer.make(json, type).setEnvironment(type);
        }
    }

    public static boolean checkConditions(Collection<Condition> conditions, DataContext context) {
        for (Condition condition : conditions) {
            if (!condition.active(context)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", this.getId().toString());
        this.propertyManager.values().forEach((palladiumProperty, o) -> json.add(palladiumProperty.getKey(), ((PalladiumProperty) palladiumProperty).toJSON(o)));

        return json;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "conditions"), "Conditions")
                .add(HTMLBuilder.heading("Conditions"))
                .addDocumentationSettings(REGISTRY.getValues().stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public ResourceLocation getId() {
        return REGISTRY.getKey(this);
    }

    public String getDocumentationDescription() {
        return "";
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        IDefaultDocumentedConfigurable.super.generateDocumentation(builder);
        builder.setTitle(this.getId().getPath());

        var desc = this.getDocumentationDescription();
        if (desc != null && !desc.isEmpty()) {
            builder.setDescription(desc + "<br><br>" + "Applicable for: " + this.getContextEnvironment().toString().toLowerCase(Locale.ROOT));
        } else {
            builder.setDescription("Applicable for: " + this.getContextEnvironment().toString().toLowerCase(Locale.ROOT));
        }
    }
}
