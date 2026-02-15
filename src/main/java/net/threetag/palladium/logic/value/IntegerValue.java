package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.molang.EntityContext;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;
import net.threetag.palladium.util.molang.ModifyIntFunction;
import team.unnamed.mocha.MochaEngine;

public abstract class IntegerValue extends Value implements EntityContext {

    public final String molang;
    public final ModifyIntFunction function;
    private Entity cachedEntity;

    public IntegerValue(String molang) {
        this.molang = molang;

        if (this.molang != null && !this.molang.isEmpty() && !this.molang.isBlank()) {
            MochaEngine<EntityContext> mocha = MoLangQueryRegistry.create(this);
            this.function = mocha.compile(this.molang, ModifyIntFunction.class);
        } else {
            this.function = null;
        }
    }

    @Override
    public Object get(DataContext context) {
        int i = this.getInteger(context);

        if (this.function != null) {
            this.cachedEntity = context.getEntity();

            if (this.cachedEntity != null) {
                i = this.function.modify(i);
            }
        }

        return i;
    }

    @Override
    public Entity entity() {
        return this.cachedEntity;
    }

    public abstract int getInteger(DataContext context);

    protected static <B extends IntegerValue> RecordCodecBuilder<B, String> modifyFunctionCodec() {
        return Codec.STRING.optionalFieldOf("modify", "").forGetter(v -> v.molang);
    }

    public static abstract class IntSerializer<T extends IntegerValue> extends ValueSerializer<T> {

        @Override
        public CodecDocumentationBuilder<Value, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).addOptional("modify", TYPE_MOLANG, "MoLang function to modify the value.");
        }
    }
}
