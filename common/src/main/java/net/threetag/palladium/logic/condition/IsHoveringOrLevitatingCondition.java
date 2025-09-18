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
//public class IsHoveringOrLevitatingCondition implements Condition {
//
//    public static final IsHoveringOrLevitatingCondition INSTANCE = new IsHoveringOrLevitatingCondition();
//
//    public static final MapCodec<IsHoveringOrLevitatingCondition> CODEC = MapCodec.unit(INSTANCE);
//    public static final StreamCodec<RegistryFriendlyByteBuf, IsHoveringOrLevitatingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);
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
//            return extension.palladium$getFlightHandler().getHoveringAnimation(1F) > 0F || (flight > 0F && flight <= 1F);
//        }
//        return false;
//    }
//
//    @Override
//    public ConditionSerializer<IsHoveringOrLevitatingCondition> getSerializer() {
//        return ConditionSerializers.IS_HOVERING_OR_LEVITATING.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<IsHoveringOrLevitatingCondition> {
//
//        @Override
//        public MapCodec<IsHoveringOrLevitatingCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, IsHoveringOrLevitatingCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity is hovering mid-air or levitating.";
//        }
//    }
//}
