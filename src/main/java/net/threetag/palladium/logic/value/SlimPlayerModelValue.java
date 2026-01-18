package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PlayerUtil;

public class SlimPlayerModelValue extends BooleanValue {

    public static final MapCodec<SlimPlayerModelValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            onTrueCodec(), onFalseCodec()
    ).apply(instance, SlimPlayerModelValue::new));

    public SlimPlayerModelValue(String onTrue, String onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return PlayerUtil.hasSmallArms(context.getPlayer());
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.SLIM_PLAYER_MODEL.get();
    }

    public static class Serializer extends BooleanSerializer<SlimPlayerModelValue> {

        @Override
        public MapCodec<SlimPlayerModelValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, SlimPlayerModelValue> builder, HolderLookup.Provider provider) {
            builder.setName("Slim Player Model").setDescription("Checks if the entity is a player and has the slim-armed (Alex) player model.")
                    .addExampleObject(new SlimPlayerModelValue("slim_arms", "wide_arms"));
        }
    }
}
