package net.threetag.palladium.client.dynamictexture.variable;

import net.threetag.palladium.util.context.DataContext;

public interface ITextureVariable {

    Object get(DataContext context);

    default String replace(String base, String key, DataContext context) {
        return base.replaceAll("#" + key, get(context).toString());
    }
}
