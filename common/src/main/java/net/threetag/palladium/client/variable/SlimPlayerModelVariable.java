package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PlayerUtil;

public class SlimPlayerModelVariable extends BooleanPathVariable {

    public static final MapCodec<SlimPlayerModelVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            onTrueCodec(), onFalseCodec()
    ).apply(instance, SlimPlayerModelVariable::new));

    public SlimPlayerModelVariable(String onTrue, String onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return PlayerUtil.hasSmallArms(context.getPlayer());
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.SLIM_PLAYER_MODEL;
    }

    public static class Serializer extends BooleanSerializer<SlimPlayerModelVariable> {

        @Override
        public MapCodec<SlimPlayerModelVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, SlimPlayerModelVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Slim Player Model").setDescription("Checks if the entity is a player and has the slim-armed (Alex) player model.")
                    .setExampleObject(new SlimPlayerModelVariable("slim_arms", "wide_arms"));
        }
    }
}
