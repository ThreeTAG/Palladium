package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.logic.context.DataContext;

public record ItemInSlotCondition(Ingredient ingredient, PlayerSlot slot) implements Condition {

    public static final MapCodec<ItemInSlotCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Ingredient.CODEC.fieldOf("item").forGetter(ItemInSlotCondition::ingredient),
                    PlayerSlot.CODEC.fieldOf("slot").forGetter(ItemInSlotCondition::slot)
            ).apply(instance, ItemInSlotCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInSlotCondition> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ItemInSlotCondition::ingredient,
            PlayerSlot.STREAM_CODEC, ItemInSlotCondition::slot,
            ItemInSlotCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        for (ItemStack item : this.slot.getItems(entity)) {
            if (this.ingredient.test(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConditionSerializer<ItemInSlotCondition> getSerializer() {
        return ConditionSerializers.ITEM_IN_SLOT.get();
    }

    public static class Serializer extends ConditionSerializer<ItemInSlotCondition> {

        @Override
        public MapCodec<ItemInSlotCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemInSlotCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the given item is in the given slot.";
        }
    }
}
