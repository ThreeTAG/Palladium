// TODO

//package net.threetag.palladium.condition;
//
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.threetag.palladium.item.SuitSet;
//import net.threetag.palladium.registry.PalladiumRegistries;
//import net.threetag.palladium.registry.PalladiumRegistryKeys;
//import net.threetag.palladium.logic.context.DataContext;
//
//public record WearsSuitSetCondition(SuitSet suitSet) implements Condition {
//
//    public static final MapCodec<WearsSuitSetCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
//            .group(PalladiumRegistries.SUIT_SET.byNameCodec().fieldOf("suit_set").forGetter(WearsSuitSetCondition::suitSet)
//            ).apply(instance, WearsSuitSetCondition::new)
//    );
//    public static final StreamCodec<RegistryFriendlyByteBuf, WearsSuitSetCondition> STREAM_CODEC = StreamCodec.composite(
//            ByteBufCodecs.registry(PalladiumRegistryKeys.SUIT_SET), WearsSuitSetCondition::suitSet, WearsSuitSetCondition::new
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
//        return this.suitSet != null && this.suitSet.isWearing(entity);
//    }
//
//    @Override
//    public ConditionSerializer<WearsSuitSetCondition> getSerializer() {
//        return ConditionSerializers.WEARS_SUIT_SET.get();
//    }
//
//    public static class Serializer extends ConditionSerializer<WearsSuitSetCondition> {
//
//        @Override
//        public MapCodec<WearsSuitSetCondition> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, WearsSuitSetCondition> streamCodec() {
//            return STREAM_CODEC;
//        }
//
//        @Override
//        public String getDocumentationDescription() {
//            return "Checks if the entity is wearing a specific suit set.";
//        }
//    }
//}
