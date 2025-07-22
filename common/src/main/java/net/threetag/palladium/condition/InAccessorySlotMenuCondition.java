// TODO

//package net.threetag.palladium.condition;
//
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.threetag.palladium.customization.AccessorySlot;
//import net.threetag.palladium.data.DataContext;
//
//public record InAccessorySlotMenuCondition(AccessorySlot slot) implements Condition {
//
//    public static final MapCodec<InAccessorySlotMenuCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
//            .group(AccessorySlot.BY_NAME_CODEC.fieldOf("accessory_slot").forGetter(InAccessorySlotMenuCondition::slot)
//            ).apply(instance, InAccessorySlotMenuCondition::new)
//    );
//    public static final StreamCodec<RegistryFriendlyByteBuf, InAccessorySlotMenuCondition> STREAM_CODEC = StreamCodec.composite(
//            AccessorySlot.STREAM_CODEC, InAccessorySlotMenuCondition::slot, InAccessorySlotMenuCondition::new
//    );
//
//    public static AccessorySlot CURRENT_SLOT = null;
//
//    @Override
//    public boolean active(DataContext context) {
//        return CURRENT_SLOT != null && CURRENT_SLOT == this.slot;
//    }
//
//    @Override
//    public ConditionSerializer<InAccessorySlotMenuCondition> getSerializer() {
//        return ConditionSerializers.IN_ACCESSORY_SLOT_MENU.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<InAccessorySlotMenuCondition> {
//
//        @Override
//        public MapCodec<InAccessorySlotMenuCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, InAccessorySlotMenuCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Let's you check if the customization menu is currently opened and the specified slot is selected. Only available for client-side conditions.";
//        }
//
//        @Override
//        public ConditionEnvironment getContextEnvironment() {
//            return ConditionEnvironment.ASSETS;
//        }
//    }
//}
