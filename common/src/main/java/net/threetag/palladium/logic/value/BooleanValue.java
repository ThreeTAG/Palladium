package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public abstract class BooleanValue extends Value {

    public final String onTrue;
    public final String onFalse;

    public BooleanValue(String onTrue, String onFalse) {
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override
    public Object get(DataContext context) {
        return this.getBoolean(context) ? this.onTrue : this.onFalse;
    }

    public abstract boolean getBoolean(DataContext context);

    protected static <B extends BooleanValue> RecordCodecBuilder<B, String> onTrueCodec() {
        return Codec.STRING.fieldOf("on_true").forGetter(v -> v.onTrue);
    }

    protected static <B extends BooleanValue> RecordCodecBuilder<B, String> onFalseCodec() {
        return Codec.STRING.fieldOf("on_false").forGetter(v -> v.onFalse);
    }

    public static abstract class BooleanSerializer<T extends BooleanValue> extends ValueSerializer<T> {

        @Override
        public CodecDocumentationBuilder<Value, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).add("on_true", TYPE_STRING, "The value to return if the result is true.")
                    .add("on_false", TYPE_STRING, "The value to return if the result is false.");
        }
    }
}
