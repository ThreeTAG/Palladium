package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

public record IntervalCondition(int activeTicks, int disabledTicks) implements Condition {

    // TODO

    public static final MapCodec<IntervalCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    PalladiumCodecs.TIME.fieldOf("active_ticks").forGetter(IntervalCondition::activeTicks),
                    PalladiumCodecs.TIME.fieldOf("disabled_ticks").forGetter(IntervalCondition::disabledTicks)
            ).apply(instance, IntervalCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, IntervalCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, IntervalCondition::activeTicks,
            ByteBufCodecs.VAR_INT, IntervalCondition::disabledTicks,
            IntervalCondition::new
    );

//    @Override
//    public void registerAbilityProperties(AbilityInstance entry, PropertyManager manager) {
//        manager.register(TICKS, 0);
//        manager.register(ACTIVE, false);
//    }

    @Override
    public boolean test(DataContext context) {
//        var entity = context.get(DataContextType.ENTITY);
//        var entry = context.get(DataContextType.ABILITY_INSTANCE);
//
//        if (entity == null || entry == null) {
            return false;
//        }
//
//        var active = Objects.requireNonNull(entry).getProperty(ACTIVE);
//        var ticks = entry.getProperty(TICKS);
//
//        var maxTicks = active ? this.activeTicks : this.disabledTicks;
//        if (ticks < maxTicks) {
//            entry.setUniqueProperty(TICKS, ticks + 1);
//        } else {
//            entry.setUniqueProperty(TICKS, 0);
//            entry.setUniqueProperty(ACTIVE, !active);
//            active = !active;
//        }
//
//        return active;
    }

    @Override
    public ConditionSerializer<IntervalCondition> getSerializer() {
        return ConditionSerializers.INTERVAL.get();
    }

    public static class Serializer extends ConditionSerializer<IntervalCondition> {

        @Override
        public MapCodec<IntervalCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IntervalCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Allows you to set an amount of ticks the ability will be active and an amount of ticks the ability will be disabled. The ability will be active for the first amount of ticks and then disabled for the second amount of ticks and so on.";
        }
    }
}
