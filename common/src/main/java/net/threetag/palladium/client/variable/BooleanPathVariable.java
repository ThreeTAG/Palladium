package net.threetag.palladium.client.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public abstract class BooleanPathVariable extends PathVariable {

    public final String onTrue;
    public final String onFalse;

    public BooleanPathVariable(String onTrue, String onFalse) {
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override
    public Object get(DataContext context) {
        return this.getBoolean(context) ? this.onTrue : this.onFalse;
    }

    public abstract boolean getBoolean(DataContext context);

    protected static <B extends BooleanPathVariable> RecordCodecBuilder<B, String> onTrueCodec() {
        return Codec.STRING.fieldOf("on_true").forGetter(v -> v.onTrue);
    }

    protected static <B extends BooleanPathVariable> RecordCodecBuilder<B, String> onFalseCodec() {
        return Codec.STRING.fieldOf("on_false").forGetter(v -> v.onFalse);
    }

    public static abstract class BooleanSerializer<T extends BooleanPathVariable> extends PathVariableSerializer<T> {

        @Override
        public CodecDocumentationBuilder<PathVariable, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).add("on_true", TYPE_STRING, "The value to return if the result is true.")
                    .add("on_false", TYPE_STRING, "The value to return if the result is false.");
        }
    }
}
