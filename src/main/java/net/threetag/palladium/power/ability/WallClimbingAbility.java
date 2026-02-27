package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class WallClimbingAbility extends Ability {

    public static final MapCodec<WallClimbingAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, WallClimbingAbility::new));

    public WallClimbingAbility(AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return AbilitySerializers.WALL_CLIMBING.get();
    }

    public static class Serializer extends AbilitySerializer<WallClimbingAbility> {

        @Override
        public MapCodec<WallClimbingAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, WallClimbingAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows the player to climb up walls.")
                    .addExampleObject(new WallClimbingAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
