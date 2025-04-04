package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PlayerUtil;

public class WidePlayerModelVariable extends BooleanPathVariable {

    public static final MapCodec<WidePlayerModelVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            onTrueCodec(), onFalseCodec()
    ).apply(instance, WidePlayerModelVariable::new));

    public WidePlayerModelVariable(String onTrue, String onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return !PlayerUtil.hasSmallArms(context.getPlayer());
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.WIDE_PLAYER_MODEL;
    }

    public static class Serializer extends BooleanSerializer<WidePlayerModelVariable> {

        @Override
        public MapCodec<WidePlayerModelVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, WidePlayerModelVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Wide Player Model").setDescription("Checks if the entity is a player and has the wide-armed (Steve) player model.")
                    .setExampleObject(new WidePlayerModelVariable("wide_arms", "slim_arms"));
        }
    }
}
