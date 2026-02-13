package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PlayerUtil;

public class WideArmorModelCondition implements Condition {

    public static final WideArmorModelCondition INSTANCE = new WideArmorModelCondition();

    public static final MapCodec<WideArmorModelCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, WideArmorModelCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var player = context.getPlayer();
        return player != null && !PlayerUtil.hasSmallArms(player);
    }

    @Override
    public ConditionSerializer<WideArmorModelCondition> getSerializer() {
        return ConditionSerializers.WIDE_ARMOR_MODEL.get();
    }

    public static class Serializer extends ConditionSerializer<WideArmorModelCondition> {

        @Override
        public MapCodec<WideArmorModelCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WideArmorModelCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.ASSETS;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, WideArmorModelCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Wide Armor Model")
                    .setDescription("Checks if the entity has the wide armor model (Steve). Returns false if the entity is not a player or if this condition is being checked sever-side.")
                    .addExampleObject(new WideArmorModelCondition());
        }
    }
}
