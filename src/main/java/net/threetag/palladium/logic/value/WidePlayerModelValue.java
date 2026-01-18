package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PlayerUtil;

public class WidePlayerModelValue extends BooleanValue {

    public static final MapCodec<WidePlayerModelValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            onTrueCodec(), onFalseCodec()
    ).apply(instance, WidePlayerModelValue::new));

    public WidePlayerModelValue(String onTrue, String onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return !PlayerUtil.hasSmallArms(context.getPlayer());
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.WIDE_PLAYER_MODEL.get();
    }

    public static class Serializer extends BooleanSerializer<WidePlayerModelValue> {

        @Override
        public MapCodec<WidePlayerModelValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, WidePlayerModelValue> builder, HolderLookup.Provider provider) {
            builder.setName("Wide Player Model").setDescription("Checks if the entity is a player and has the wide-armed (Steve) player model.")
                    .addExampleObject(new WidePlayerModelValue("wide_arms", "slim_arms"));
        }
    }
}
