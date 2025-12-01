package net.threetag.palladium.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationSerializer;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.FlightTypeSerializer;
import net.threetag.palladium.icon.IconSerializer;
import net.threetag.palladium.logic.condition.ConditionSerializer;
import net.threetag.palladium.logic.value.ValueSerializer;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.power.ability.enabling.EnablingHandlerSerializer;
import net.threetag.palladium.power.ability.keybind.KeyBindTypeSerializer;
import net.threetag.palladium.power.ability.unlocking.UnlockingHandlerSerializer;
import net.threetag.palladium.power.dampening.PowerDampeningSource;
import net.threetag.palladium.power.provider.PowerProvider;

public class PalladiumRegistryKeys {

    public static final ResourceKey<Registry<PalladiumEntityDataType<?>>> ENTITY_DATA_TYPE = createRegistryKey("entity_data_type");
    public static final ResourceKey<Registry<AbilitySerializer<?>>> ABILITY_SERIALIZER = createRegistryKey("ability_serializer");
    public static final ResourceKey<Registry<UnlockingHandlerSerializer<?>>> ABILITY_UNLOCKING_HANDLER_SERIALIZER = createRegistryKey("ability_unlocking_handler");
    public static final ResourceKey<Registry<EnablingHandlerSerializer<?>>> ABILITY_ENABLING_HANDLER_SERIALIZER = createRegistryKey("ability_enabling_handler");
    public static final ResourceKey<Registry<KeyBindTypeSerializer<?>>> KEY_BIND_TYPE_SERIALIZER = createRegistryKey("key_bind_type");
    public static final ResourceKey<Registry<ConditionSerializer<?>>> CONDITION_SERIALIZER = createRegistryKey("condition_serializer");
    public static final ResourceKey<Registry<ValueSerializer<?>>> VALUE_SERIALIZER = createRegistryKey("value_serializer");
    public static final ResourceKey<Registry<Power>> POWER = createRegistryKey("power");
    public static final ResourceKey<Registry<PowerProvider>> POWER_PROVIDER = createRegistryKey("power_provider");
    public static final ResourceKey<Registry<PowerDampeningSource>> POWER_DAMPENING_SOURCE = createRegistryKey("power_dampening_source");
    public static final ResourceKey<Registry<IconSerializer<?>>> ICON_SERIALIZER = createRegistryKey("icon_serializer");
    public static final ResourceKey<Registry<MapCodec<? extends Item>>> ITEM_TYPE = createRegistryKey("item_type");
    public static final ResourceKey<Registry<EntityEffect>> ENTITY_EFFECT = createRegistryKey("entity_effect");
    public static final ResourceKey<Registry<CustomizationSerializer<?>>> CUSTOMIZATION_SERIALIZERS = createRegistryKey("customization_serializer");
    public static final ResourceKey<Registry<Customization>> CUSTOMIZATION = createRegistryKey("customization");
    public static final ResourceKey<Registry<CustomizationCategory>> CUSTOMIZATION_CATEGORY = createRegistryKey("customization_category");
    public static final ResourceKey<Registry<FlightTypeSerializer<?>>> FLIGHT_TYPE_SERIALIZERS = createRegistryKey("flight_type_serializer");
    public static final ResourceKey<Registry<FlightType>> FLIGHT_TYPE = createRegistryKey("flight_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Palladium.id(name));
    }

    public static String getPackFolder(ResourceKey<? extends Registry<?>> key) {
        if (key.location().getNamespace().equalsIgnoreCase(ResourceLocation.DEFAULT_NAMESPACE)) {
            return key.location().getPath();
        } else {
            return key.location().getNamespace() + "/" + key.location().getPath();
        }
    }

}
