// TODO

//package net.threetag.palladium.condition;
//
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.threetag.palladium.customization.Accessory;
//import net.threetag.palladium.customization.AccessoryPlayerData;
//import net.threetag.palladium.customization.AccessorySlot;
//import net.threetag.palladium.registry.PalladiumRegistries;
//import net.threetag.palladium.registry.PalladiumRegistryKeys;
//import net.threetag.palladium.data.DataContext;
//
//import java.util.Collection;
//import java.util.Optional;
//
//public record AccessorySelectedCondition(AccessorySlot accessorySlot, Accessory customization) implements Condition {
//
//    public static final MapCodec<AccessorySelectedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
//            .group(
//                    AccessorySlot.BY_NAME_CODEC.fieldOf("accessory_slot").forGetter(AccessorySelectedCondition::accessorySlot),
//                    PalladiumRegistries.ACCESSORY.byNameCodec().fieldOf("customization").forGetter(AccessorySelectedCondition::customization)
//            ).apply(instance, AccessorySelectedCondition::new)
//    );
//    public static final StreamCodec<RegistryFriendlyByteBuf, AccessorySelectedCondition> STREAM_CODEC = StreamCodec.composite(
//            AccessorySlot.STREAM_CODEC, AccessorySelectedCondition::accessorySlot,
//            ByteBufCodecs.registry(PalladiumRegistryKeys.ACCESSORY), AccessorySelectedCondition::customization,
//            AccessorySelectedCondition::new
//    );
//
//    @Override
//    public boolean active(DataContext context) {
//        Optional<AccessoryPlayerData> dataOptional = Accessory.getPlayerData(context.getPlayer());
//
//        if (dataOptional.isEmpty()) {
//            return false;
//        }
//
//        AccessoryPlayerData data = dataOptional.get();
//        Collection<Accessory> customizations = data.customizations.get(this.accessorySlot);
//        return customizations != null && customizations.contains(this.customization);
//    }
//
//    @Override
//    public ConditionSerializer<AccessorySelectedCondition> getSerializer() {
//        return ConditionSerializers.ACCESSORY_SELECTED.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<AccessorySelectedCondition> {
//
//        @Override
//        public MapCodec<AccessorySelectedCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, AccessorySelectedCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity has a specified customization selected from a specific customization slot. An empty string can be used to check for no customization.";
//        }
//    }
//}
