package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.molang.EntityContext;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;
import net.threetag.palladium.util.molang.ModifyStringFunction;
import team.unnamed.mocha.MochaEngine;

public abstract class StringValue extends Value implements EntityContext {

    public final String molang;
    public final ModifyStringFunction function;
    private Entity cachedEntity;

    public StringValue(String molang) {
        this.molang = molang;

        if (this.molang != null && !this.molang.isEmpty() && !this.molang.isBlank()) {
            MochaEngine<?> mocha = MoLangQueryRegistry.create(this);
            this.function = mocha.compile(this.molang, ModifyStringFunction.class);
        } else {
            this.function = null;
        }
    }

    @Override
    public Object get(DataContext context) {
        String s = this.getString(context);

        if (this.function != null) {
            this.cachedEntity = context.getEntity();

            if (this.cachedEntity != null) {
                s = this.function.modify(s);
            }
        }

        return s;
    }

    @Override
    public Entity entity() {
        return this.cachedEntity;
    }

    public abstract String getString(DataContext context);

    protected static <B extends StringValue> RecordCodecBuilder<B, String> modifyFunctionCodec() {
        return Codec.STRING.optionalFieldOf("modify", "").forGetter(v -> v.molang);
    }

    public static abstract class StringSerializer<T extends StringValue> extends ValueSerializer<T> {

        @Override
        public CodecDocumentationBuilder<Value, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).addOptional("modify", TYPE_MOLANG, "MoLang function to modify the value.");
        }
    }
}
