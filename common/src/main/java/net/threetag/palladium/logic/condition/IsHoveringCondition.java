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
//public class IsHoveringCondition implements Condition {
//
//    public static final IsHoveringCondition INSTANCE = new IsHoveringCondition();
//
//    public static final MapCodec<IsHoveringCondition> CODEC = MapCodec.unit(INSTANCE);
//    public static final StreamCodec<RegistryFriendlyByteBuf, IsHoveringCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);
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
//            return extension.palladium$getFlightHandler().getHoveringAnimation(1F) > 0F;
//        }
//        return false;
//    }
//
//    @Override
//    public ConditionSerializer<IsHoveringCondition> getSerializer() {
//        return ConditionSerializers.IS_HOVERING.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<IsHoveringCondition> {
//
//        @Override
//        public MapCodec<IsHoveringCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, IsHoveringCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity is hovering mid-air.";
//        }
//    }
//}
