package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.logic.context.DataContext;

public record HasPowerCondition(ResourceLocation powerId) implements Condition {

    public static final MapCodec<HasPowerCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(ResourceLocation.CODEC.fieldOf("power").forGetter(HasPowerCondition::powerId)
            ).apply(instance, HasPowerCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HasPowerCondition> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, HasPowerCondition::powerId, HasPowerCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        EntityPowerHandler handler = PowerUtil.getPowerHandler(entity);
        return handler != null && handler.getPowerHolders().containsKey(this.powerId);
    }

    @Override
    public ConditionSerializer<HasPowerCondition> getSerializer() {
        return ConditionSerializers.HAS_POWER.get();
    }

    public static class Serializer extends ConditionSerializer<HasPowerCondition> {

        @Override
        public MapCodec<HasPowerCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HasPowerCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a power with the given ID.";
        }
    }
}
