package net.threetag.palladium.logic.value;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistries;

public abstract class Value {

    public static final Codec<Value> DIRECT_CODEC = PalladiumRegistries.VALUE_SERIALIZER.byNameCodec().dispatch(Value::getSerializer, ValueSerializer::codec);

    public static final Codec<Value> CODEC = Codec.either(DIRECT_CODEC, Codec.PASSTHROUGH)
            .xmap(either -> either.map(
                    value -> value,
                    StaticValue::new
            ), value -> value instanceof StaticValue s ? Either.right(s.object) : Either.left(value));

    public abstract Object get(DataContext context);

    public abstract ValueSerializer<?> getSerializer();

    public String replace(String base, String key, DataContext context) {
        return base.replaceAll("#" + key, getAsString(context));
    }

    public int getAsInt(DataContext context, int fallback) {
        var value = this.get(context);

        if (value instanceof Number n) {
            return n.intValue();
        } else {
            return fallback;
        }
    }

    public int getAsInt(DataContext context) {
        return this.getAsInt(context, 0);
    }

    public float getAsFloat(DataContext context, int fallback) {
        var value = this.get(context);

        if (value instanceof Number n) {
            return n.floatValue();
        } else {
            return fallback;
        }
    }

    public float getAsFloat(DataContext context) {
        return this.getAsFloat(context, 0);
    }

    public double getAsDouble(DataContext context, int fallback) {
        var value = this.get(context);

        if (value instanceof Number n) {
            return n.doubleValue();
        } else {
            return fallback;
        }
    }

    public double getAsDouble(DataContext context) {
        return this.getAsDouble(context, 0);
    }

    public String getAsString(DataContext context) {
        var value = this.get(context);
        return value.toString();
    }

    public boolean getAsBoolean(DataContext context) {
        var value = this.get(context);

        if (value instanceof Number n) {
            return n.doubleValue() > 0;
        } else if (value instanceof Boolean b) {
            return b;
        } else {
            return false;
        }
    }

}
