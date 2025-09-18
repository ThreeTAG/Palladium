// TODO

//package net.threetag.palladium.condition;
//
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.world.item.ItemStack;
//import net.threetag.palladium.item.Openable;
//import net.threetag.palladium.util.PlayerSlot;
//import net.threetag.palladium.logic.context.DataContext;
//
//public record ItemInSlotOpenCondition(PlayerSlot slot) implements Condition {
//
//    public static final MapCodec<ItemInSlotOpenCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
//            .group(PlayerSlot.CODEC.fieldOf("slot").forGetter(ItemInSlotOpenCondition::slot)
//            ).apply(instance, ItemInSlotOpenCondition::new)
//    );
//    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInSlotOpenCondition> STREAM_CODEC = StreamCodec.composite(
//            PlayerSlot.STREAM_CODEC, ItemInSlotOpenCondition::slot, ItemInSlotOpenCondition::new
//    );
//
//    @Override
//    public boolean active(DataContext context) {
//        var entity = context.getLivingEntity();
//
//        if (entity == null) {
//            return false;
//        }
//
//        var stacks = this.slot.getItems(entity);
//
//        for (ItemStack stack : stacks) {
//            if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
//                if (openable.isOpen(stack)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public ConditionSerializer<ItemInSlotOpenCondition> getSerializer() {
//        return ConditionSerializers.ITEM_IN_SLOT_OPEN.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<ItemInSlotOpenCondition> {
//
//        @Override
//        public MapCodec<ItemInSlotOpenCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, ItemInSlotOpenCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the item in the given slot is opened. Needs to be using the openable-system for items.";
//        }
//    }
//}
