package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PlayerUtil;

public class SlimArmorModelCondition implements Condition {

    public static final SlimArmorModelCondition INSTANCE = new SlimArmorModelCondition();

    public static final MapCodec<SlimArmorModelCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SlimArmorModelCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var player = context.getPlayer();
        return player != null && PlayerUtil.hasSmallArms(player);
    }

    @Override
    public ConditionSerializer<SlimArmorModelCondition> getSerializer() {
        return ConditionSerializers.SLIM_ARMOR_MODEL.get();
    }

    public static class Serializer extends ConditionSerializer<SlimArmorModelCondition> {

        @Override
        public MapCodec<SlimArmorModelCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SlimArmorModelCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.ASSETS;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, SlimArmorModelCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Slim Armor Model")
                    .setDescription("Checks if the entity has the slim armor model (Alex). Returns false if the entity is not a player or if this condition is being checked sever-side.")
                    .addExampleObject(new SlimArmorModelCondition());
        }
    }
}
