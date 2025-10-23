package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.threetag.palladium.logic.context.DataContext;

public record HasEffectCondition(Holder<MobEffect> mobEffect) implements Condition {

    public static final MapCodec<HasEffectCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(HasEffectCondition::mobEffect)
            ).apply(instance, HasEffectCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HasEffectCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT), HasEffectCondition::mobEffect, HasEffectCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        return entity != null && entity.hasEffect(this.mobEffect);
    }

    @Override
    public ConditionSerializer<HasEffectCondition> getSerializer() {
        return ConditionSerializers.HAS_EFFECT.get();
    }

    public static class Serializer extends ConditionSerializer<HasEffectCondition> {

        @Override
        public MapCodec<HasEffectCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HasEffectCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a (potion) effect.";
        }
    }
}
