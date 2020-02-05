package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public interface ITextureVariable {

    Object get(IModelLayerContext context);

    default String replace(String base, String key, IModelLayerContext context) {
        return base.replaceAll("#" + key, get(context).toString());
    }
}
