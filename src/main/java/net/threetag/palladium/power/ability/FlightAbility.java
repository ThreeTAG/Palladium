package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.PalladiumFlightTypes;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlightAbility extends Ability {

    public static final MapCodec<FlightAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(PalladiumRegistryKeys.FLIGHT_TYPE).optionalFieldOf("flight_type", PalladiumFlightTypes.PROPULSION).forGetter(a -> a.flightTypeKey),
            propertiesCodec(), stateCodec(), energyBarUsagesCodec()
    ).apply(instance, FlightAbility::new));

    private final ResourceKey<FlightType> flightTypeKey;
    private Holder<FlightType> flightType;

    public FlightAbility(ResourceKey<FlightType> flightTypeKey, AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
        this.flightTypeKey = flightTypeKey;
    }

    @Override
    public boolean tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        if (enabled) {
            if (this.flightType == null) {
                this.flightType = entity.registryAccess().get(this.flightTypeKey).orElse(null);
            }
        }

        return super.tick(entity, abilityInstance, enabled);
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        var flightHandler = EntityFlightHandler.get(entity);
        flightHandler.reevaluateFlightType();
    }

    @Nullable
    public FlightType getFlightType() {
        return this.flightType != null ? this.flightType.value() : null;
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return AbilitySerializers.FLIGHT.get();
    }

    public static class Serializer extends AbilitySerializer<FlightAbility> {

        @Override
        public MapCodec<FlightAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, FlightAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows the player to fly using a pre-defined flight type")
                    .addOptional("flight_type", TYPE_FLIGHT_TYPE, "The ID of the flight type that will be used.", PalladiumFlightTypes.PROPULSION.identifier())
                    .addExampleObject(new FlightAbility(PalladiumFlightTypes.PROPULSION, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
