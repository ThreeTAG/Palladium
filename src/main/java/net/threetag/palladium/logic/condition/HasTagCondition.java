package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public record HasTagCondition(String tag) implements Condition {

    public static final MapCodec<HasTagCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Codec.STRING.fieldOf("tag").forGetter(HasTagCondition::tag)
            ).apply(instance, HasTagCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HasTagCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, HasTagCondition::tag, HasTagCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.getTags().contains(this.tag);
    }

    @Override
    public ConditionSerializer<HasTagCondition> getSerializer() {
        return ConditionSerializers.HAS_TAG.get();
    }

    public static class Serializer extends ConditionSerializer<HasTagCondition> {

        @Override
        public MapCodec<HasTagCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, HasTagCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Has Tag")
                    .setDescription("Checks if the entity has a specific tag. These tags are added to entities via /tag command.")
                    .add("tag", TYPE_STRING, "The tag that needs to be on the entity")
                    .addExampleObject(new HasTagCondition("example_tag"));
        }
    }
}
