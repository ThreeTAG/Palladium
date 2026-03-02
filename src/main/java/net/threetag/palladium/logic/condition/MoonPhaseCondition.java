package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.MoonPhase;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;

public record MoonPhaseCondition(List<MoonPhase> moonPhases) implements Condition {

    public static final MapCodec<MoonPhaseCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    PalladiumCodecs.listOrPrimitive(MoonPhase.CODEC).fieldOf("moon_phase").forGetter(c -> c.moonPhases)
            ).apply(instance, MoonPhaseCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MoonPhaseCondition> STREAM_CODEC = StreamCodec.composite(
            PalladiumCodecs.MOON_PHASE_STREAM_CODEC.apply(ByteBufCodecs.list()), c -> c.moonPhases,
            MoonPhaseCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var level = context.getLevel();
        var pos = context.getBlockPos();

        if (level == null || pos == null) {
            return false;
        }

        var moonPhase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos);
        return this.moonPhases().contains(moonPhase);
    }

    @Override
    public ConditionSerializer<MoonPhaseCondition> getSerializer() {
        return ConditionSerializers.MOON_PHASE.get();
    }

    public static class Serializer extends ConditionSerializer<MoonPhaseCondition> {

        @Override
        public MapCodec<MoonPhaseCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, MoonPhaseCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Moon Phase")
                    .setDescription("Checks and compares the current moon phase in the world.")
                    .add("moon_phase", SettingType.enumList(MoonPhase.values()), "Moon phase(s) that is/are required")
                    .addExampleObject(new MoonPhaseCondition(Collections.singletonList(MoonPhase.FULL_MOON)))
                    .addExampleObject(new MoonPhaseCondition(List.of(MoonPhase.NEW_MOON, MoonPhase.THIRD_QUARTER)));
        }
    }
}
