package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public class FireAspectAbility extends Ability {

    public static final MapCodec<FireAspectAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    PalladiumCodecs.TIME.fieldOf("time").forGetter(ab -> ab.time),
                    Codec.BOOL.optionalFieldOf("should_stack_time", false).forGetter(ab -> ab.shouldStackTime),
                    PalladiumCodecs.TIME.optionalFieldOf("max_time", 60 * 20).forGetter(ab -> ab.maxTime),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, FireAspectAbility::new));

    public final int time;
    public final boolean shouldStackTime;
    public final int maxTime;

    public FireAspectAbility(int time, boolean shouldStackTime, int maxTime, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.time = time;
        this.shouldStackTime = shouldStackTime;
        this.maxTime = maxTime;
    }

    @Override
    public AbilitySerializer<FireAspectAbility> getSerializer() {
        return AbilitySerializers.FIRE_ASPECT.get();
    }

    public static class Serializer extends AbilitySerializer<FireAspectAbility> {

        @Override
        public MapCodec<FireAspectAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, FireAspectAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes this entity's attacks light targets on fire as if fire aspect was used.")
                    .add("time", TYPE_INT, "The amount of time, in ticks, that the victim entity will be set on fire for")
                    .addOptional("should_stack_time", TYPE_BOOLEAN, "If true, attacking an entity that's already on fire will add the \"time\" field to their current burn time instead of setting it", false)
                    .addOptional("max_time", TYPE_INT, "If \"should_stack_time\" is true, the victim's burn time (in ticks) will not exceed this value after being hit", 60 * 20)
                    .setExampleObject(new FireAspectAbility(5, false, 5, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }

}
