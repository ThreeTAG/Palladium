package net.threetag.threecore.util.modellayer.texture.variable;

import net.threetag.threecore.util.modellayer.IModelLayerContext;

public interface ITextureVariable {

    Object get(IModelLayerContext context);

    default String replace(String base, String key, IModelLayerContext context) {
        return base.replaceAll("#" + key, get(context).toString());
    }
}
