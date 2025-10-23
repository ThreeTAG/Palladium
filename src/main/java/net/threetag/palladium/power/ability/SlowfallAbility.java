package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class SlowfallAbility extends Ability {

    public static final MapCodec<SlowfallAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, SlowfallAbility::new));

    public SlowfallAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<SlowfallAbility> getSerializer() {
        return AbilitySerializers.SLOWFALL.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, boolean enabled) {
        if (enabled && !entity.onGround() && entity.getDeltaMovement().y() < 0D) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y * 0.6D, entity.getDeltaMovement().z);
            entity.fallDistance = 0F;
        }
    }

    public static class Serializer extends AbilitySerializer<SlowfallAbility> {

        @Override
        public MapCodec<SlowfallAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, SlowfallAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes the entity fall slower.")
                    .setExampleObject(new SlowfallAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
