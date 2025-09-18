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
//public class IsFastFlyingCondition implements Condition {
//
//    public static final IsFastFlyingCondition INSTANCE = new IsFastFlyingCondition();
//
//    public static final MapCodec<IsFastFlyingCondition> CODEC = MapCodec.unit(INSTANCE);
//    public static final StreamCodec<RegistryFriendlyByteBuf, IsFastFlyingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);
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
//            return extension.palladium$getFlightHandler().getFlightAnimation(1F) > 1F;
//        }
//        return false;
//    }
//
//    @Override
//    public ConditionSerializer<IsFastFlyingCondition> getSerializer() {
//        return ConditionSerializers.IS_FAST_FLYING.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<IsFastFlyingCondition> {
//
//        @Override
//        public MapCodec<IsFastFlyingCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, IsFastFlyingCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity is currently flying fast.";
//        }
//    }
//}
