package net.threetag.palladium.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelLayerLocationUtil {

    private final ResourceLocation model;
    private final String layer;

    public ModelLayerLocationUtil(ResourceLocation model, String layer) {
        this.model = model;
        this.layer = layer;
    }

    public ResourceLocation getModel() {
        return this.model;
    }

    public String getLayer() {
        return this.layer;
    }

    @Environment(EnvType.CLIENT)
    public ModelLayerLocation toModelLayer() {
        return new ModelLayerLocation(this.model, this.layer);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ModelLayerLocationUtil)) {
            return false;
        } else {
            ModelLayerLocationUtil modelLayerLocation = (ModelLayerLocationUtil) object;
            return this.model.equals(modelLayerLocation.model) && this.layer.equals(modelLayerLocation.layer);
        }
    }

    @Override
    public int hashCode() {
        int i = this.model.hashCode();
        i = 31 * i + this.layer.hashCode();
        return i;
    }

    @Override
    public String toString() {
        return this.model + "#" + this.layer;
    }

}
