package net.threetag.palladium.client.variable;

import com.mojang.serialization.Codec;
import net.threetag.palladium.data.DataContext;

public abstract class PathVariable {

    public static final Codec<PathVariable> CODEC = PathVariableSerializer.TYPE_CODEC.dispatch(PathVariable::getSerializer, PathVariableSerializer::codec);

    public abstract Object get(DataContext context);

    public abstract PathVariableSerializer<?> getSerializer();

    public String replace(String base, String key, DataContext context) {
        return base.replaceAll("#" + key, get(context).toString());
    }

}
