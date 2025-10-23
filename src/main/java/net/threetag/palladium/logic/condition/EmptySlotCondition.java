package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.entity.PlayerSlot;

public record EmptySlotCondition(PlayerSlot slot) implements Condition {

    public static final MapCodec<EmptySlotCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(PlayerSlot.CODEC.fieldOf("slot").forGetter(EmptySlotCondition::slot)
            ).apply(instance, EmptySlotCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EmptySlotCondition> STREAM_CODEC = StreamCodec.composite(
            PlayerSlot.STREAM_CODEC, EmptySlotCondition::slot, EmptySlotCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        for (ItemStack item : this.slot.getItems(entity)) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer<EmptySlotCondition> getSerializer() {
        return ConditionSerializers.EMPTY_SLOT.get();
    }

    public static class Serializer extends ConditionSerializer<EmptySlotCondition> {

        @Override
        public MapCodec<EmptySlotCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EmptySlotCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the given slot is empty.";
        }
    }
}
