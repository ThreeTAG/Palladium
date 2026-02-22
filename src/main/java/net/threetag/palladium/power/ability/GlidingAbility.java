package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class GlidingAbility extends Ability {

    public static final MapCodec<GlidingAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(), stateCodec(), energyBarUsagesCodec()
    ).apply(instance, GlidingAbility::new));

    public GlidingAbility(AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
    }

    @Override
    public boolean tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        return enabled && entity.isFallFlying();
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return AbilitySerializers.GLIDING.get();
    }

    public static class Serializer extends AbilitySerializer<GlidingAbility> {

        @Override
        public MapCodec<GlidingAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, GlidingAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows the player to use the elytra gliding")
                    .addExampleObject(new GlidingAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
