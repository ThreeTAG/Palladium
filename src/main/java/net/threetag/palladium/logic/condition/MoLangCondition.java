package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.logic.molang.EntityContext;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;
import net.threetag.palladium.util.molang.BooleanQuerySupplier;
import team.unnamed.mocha.MochaEngine;

public class MoLangCondition implements Condition, EntityContext {

    public static final MapCodec<MoLangCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("molang").forGetter(MoLangCondition::getMolang)
    ).apply(instance, MoLangCondition::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, MoLangCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, MoLangCondition::getMolang,
            MoLangCondition::new
    );

    public final String molang;
    public final BooleanQuerySupplier supplier;
    private Entity cachedEntity;
    private boolean cachedAnimationFinished = false;

    public MoLangCondition(String molang) {
        this.molang = molang;
        MochaEngine<?> mocha = MoLangQueryRegistry.create(this);
        this.supplier = mocha.compile(this.molang, BooleanQuerySupplier.class);
    }

    public String getMolang() {
        return molang;
    }

    @Override
    public boolean test(DataContext context) {
        this.cachedEntity = context.getEntity();
        this.cachedAnimationFinished = context.getOrDefault(DataContextKeys.ANY_ANIMATION_FINISHED, false);
        return this.cachedEntity != null && this.supplier != null && this.supplier.getAsBoolean();
    }

    @Override
    public Entity entity() {
        return this.cachedEntity;
    }

    @Override
    public boolean hasAnimationFinished() {
        return this.cachedAnimationFinished;
    }

    @Override
    public ConditionSerializer<?> getSerializer() {
        return ConditionSerializers.MOLANG.get();
    }

    public static class Serializer extends ConditionSerializer<MoLangCondition> {

        @Override
        public MapCodec<MoLangCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MoLangCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, MoLangCondition> builder, HolderLookup.Provider provider) {
            builder.setName("MoLang")
                    .setDescription("Uses a MoLang query as a condition")
                    .addExampleObject(new MoLangCondition("query.is_moving()"));
        }
    }
}
