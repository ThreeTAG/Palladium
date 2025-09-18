// TODO

//package net.threetag.palladium.condition;
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.threetag.palladium.entity.PalladiumPlayerExtension;
//import net.threetag.palladium.logic.context.DataContext;
//import net.threetag.palladium.logic.context.DataContextType;
//
//public class IsFlyingCondition implements Condition {
//
//    public static final IsFlyingCondition INSTANCE = new IsFlyingCondition();
//
//    public static final MapCodec<IsFlyingCondition> CODEC = MapCodec.unit(INSTANCE);
//    public static final StreamCodec<RegistryFriendlyByteBuf, IsFlyingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);
//
//    @Override
//    public boolean active(DataContext context) {
//        var entity = context.get(DataContextType.ENTITY);
//
//        if (entity == null) {
//            return false;
//        }
//
//        if (entity instanceof PalladiumPlayerExtension extension) {
//            return extension.palladium$getFlightHandler().getFlightAnimation(1F) > 0F;
//        }
//        return false;
//    }
//
//    @Override
//    public ConditionSerializer<IsFlyingCondition> getSerializer() {
//        return ConditionSerializers.IS_FLYING.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<IsFlyingCondition> {
//
//        @Override
//        public MapCodec<IsFlyingCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, IsFlyingCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity is flying.";
//        }
//    }
//}
