package net.threetag.palladium.client.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.runtime.compiled.MochaCompiledFunction;
import team.unnamed.mocha.runtime.compiled.Named;

public abstract class IntegerPathVariable extends PathVariable {

    public final String molang;
    public final ModifyIntFunction function;

    public IntegerPathVariable(String molang) {
        this.molang = molang;

        if (this.molang != null && !this.molang.isEmpty() && !this.molang.isBlank()) {
            MochaEngine<?> mocha = MochaEngine.createStandard();
            this.function = mocha.compile(this.molang, ModifyIntFunction.class);
        } else {
            this.function = null;
        }
    }

    @Override
    public Object get(DataContext context) {
        int i = this.getInteger(context);

        if (this.function != null) {
            i = this.function.modify(i);
        }

        return i;
    }

    public abstract int getInteger(DataContext context);

    protected static <B extends IntegerPathVariable> RecordCodecBuilder<B, String> modifyFunctionCodec() {
        return Codec.STRING.optionalFieldOf("modify", "").forGetter(v -> v.molang);
    }

    public interface ModifyIntFunction extends MochaCompiledFunction {

        int modify(@Named("value") int value);

    }

    public static abstract class IntSerializer<T extends IntegerPathVariable> extends PathVariableSerializer<T> {

        @Override
        public CodecDocumentationBuilder<PathVariable, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).addOptional("modify", TYPE_MOLANG, "MoLang function to modify the value.");
        }
    }
}
