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
//public class IsLevitatingCondition implements Condition {
//
//    public static final IsLevitatingCondition INSTANCE = new IsLevitatingCondition();
//
//    public static final MapCodec<IsLevitatingCondition> CODEC = MapCodec.unit(INSTANCE);
//    public static final StreamCodec<RegistryFriendlyByteBuf, IsLevitatingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);
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
//            float flight = extension.palladium$getFlightHandler().getFlightAnimation(1F);
//            return flight > 0F && flight <= 1F;
//        }
//        return false;
//    }
//
//    @Override
//    public ConditionSerializer<IsLevitatingCondition> getSerializer() {
//        return ConditionSerializers.IS_LEVITATING.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<IsLevitatingCondition> {
//
//        @Override
//        public MapCodec<IsLevitatingCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, IsLevitatingCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity is levitating.";
//        }
//    }
//}
