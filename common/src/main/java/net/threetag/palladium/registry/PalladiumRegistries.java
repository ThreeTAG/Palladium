package net.threetag.palladium.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationSerializer;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.client.icon.IconSerializer;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.core.registry.DataPackRegistryBuilder;
import net.threetag.palladium.core.registry.RegistryBuilder;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.FlightTypeSerializer;
import net.threetag.palladium.entity.number.EntityDependentNumberType;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.power.ability.enabling.EnablingHandlerSerializer;
import net.threetag.palladium.power.ability.keybind.KeyBindTypeSerializer;
import net.threetag.palladium.power.ability.unlocking.UnlockingHandlerSerializer;
import net.threetag.palladium.power.provider.PowerProvider;

public class PalladiumRegistries {

    public static final Registry<PalladiumEntityDataType<?>> ENTITY_DATA_TYPE = RegistryBuilder.create(PalladiumRegistryKeys.ENTITY_DATA_TYPE).build();
    public static final Registry<AbilitySerializer<?>> ABILITY_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.ABILITY_SERIALIZER).build();
    public static final Registry<UnlockingHandlerSerializer<?>> ABILITY_UNLOCKING_HANDLER_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER).build();
    public static final Registry<EnablingHandlerSerializer<?>> ABILITY_ENABLING_HANDLER_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.ABILITY_ENABLING_HANDLER_SERIALIZER).build();
    public static final Registry<KeyBindTypeSerializer<?>> KEY_BIND_TYPE_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.KEY_BIND_TYPE_SERIALIZER).build();
    public static final Registry<PowerProvider> POWER_PROVIDER = RegistryBuilder.create(PalladiumRegistryKeys.POWER_PROVIDER).build();
    public static final Registry<ConditionSerializer<?>> CONDITION_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.CONDITION_SERIALIZER).build();
    public static final Registry<IconSerializer<?>> ICON_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.ICON_SERIALIZER).build();
    public static final Registry<EntityDependentNumberType<?>> ENTITY_DEPENDENT_NUMBER_TYPE = RegistryBuilder.create(PalladiumRegistryKeys.ENTITY_DEPENDENT_NUMBER_TYPE).build();
    public static final Registry<MapCodec<? extends Item>> ITEM_TYPE = RegistryBuilder.create(PalladiumRegistryKeys.ITEM_TYPE).build();
    public static final Registry<EntityEffect> ENTITY_EFFECT = RegistryBuilder.create(PalladiumRegistryKeys.ENTITY_EFFECT).build();
    public static final Registry<CustomizationSerializer<?>> CUSTOMIZATION_SERIALIZERS = RegistryBuilder.create(PalladiumRegistryKeys.CUSTOMIZATION_SERIALIZERS).build();
    public static final Registry<FlightTypeSerializer<?>> FLIGHT_TYPE_SERIALIZERS = RegistryBuilder.create(PalladiumRegistryKeys.FLIGHT_TYPE_SERIALIZERS).build();

    public static void init() {
        DataPackRegistryBuilder.create(PalladiumRegistryKeys.POWER, Power.CODEC, Power.CODEC);
        DataPackRegistryBuilder.create(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY, CustomizationCategory.CODEC, CustomizationCategory.CODEC);
        DataPackRegistryBuilder.create(PalladiumRegistryKeys.CUSTOMIZATION, Customization.Codecs.SIMPLE_CODEC, Customization.Codecs.SIMPLE_CODEC);
        DataPackRegistryBuilder.create(PalladiumRegistryKeys.FLIGHT_TYPES, FlightType.CODEC, FlightType.CODEC);
    }

}
