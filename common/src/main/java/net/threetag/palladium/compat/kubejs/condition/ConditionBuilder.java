package net.threetag.palladium.compat.kubejs.condition;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
import net.threetag.palladium.compat.kubejs.ability.AbilityBuilder;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyLookup;

import java.util.*;

@SuppressWarnings("rawtypes")
public class ConditionBuilder extends BuilderBase<ConditionSerializer> {

    public transient TestFunction test;

    public transient List<AbilityBuilder.DeserializePropertyInfo> extraProperties;

    public ConditionBuilder(ResourceLocation id) {
        super(id);
        this.test = null;
        this.extraProperties = new ArrayList<>();
    }

    @Override
    public RegistryObjectBuilderTypes<? super ConditionSerializer> getRegistryType() {
        return PalladiumKubeJSPlugin.CONDITION;
    }

    @Override
    public ConditionSerializer createObject() {
        return new Serializer(this);
    }

    public ConditionBuilder test(TestFunction testFunction) {
        this.test = testFunction;
        return this;
    }

    public ConditionBuilder addProperty(String key, String type, Object defaultValue, String configureDesc) {
        PalladiumProperty property = PalladiumPropertyLookup.get(type, key);

        if (property != null) {
            this.extraProperties.add(new AbilityBuilder.DeserializePropertyInfo(key, type, defaultValue, configureDesc));
        } else {
            AddonPackLog.error("Failed to register condition property \"%s\", type \"%s\" is not supported", key, type);
        }

        return this;
    }

    @FunctionalInterface
    public interface TestFunction {
        boolean test(LivingEntity entity, Map<String, Object> extraProperties);
    }

    @SuppressWarnings("unchecked")
    public static class Serializer extends ConditionSerializer {

        public Serializer(ConditionBuilder builder) {
            this.builder = builder;

            for (AbilityBuilder.DeserializePropertyInfo info : this.builder.extraProperties) {
                PalladiumProperty property = PalladiumPropertyLookup.get(info.type, info.key);

                if (info.configureDesc != null && !info.configureDesc.isEmpty()) {
                    Objects.requireNonNull(property).configurable(info.configureDesc);
                }

                this.withProperty(property, PalladiumProperty.fixValues(property, info.defaultValue));
            }
        }

        public final ConditionBuilder builder;

        @Override
        public Condition make(JsonObject json) {
            Map<String, Object> extraProps = new HashMap<>();

            for (Map.Entry<PalladiumProperty<?>, Object> propertyEntry : this.getPropertyManager().values().entrySet()) {
                PalladiumProperty<?> property = propertyEntry.getKey();
                extraProps.put(property.getKey(), this.getProperty(json, property));
            }

            return new ScriptableCondition(this.builder, this, extraProps);
        }
    }

}
