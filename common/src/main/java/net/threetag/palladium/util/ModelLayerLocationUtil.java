package net.threetag.palladium.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public record ModelLayerLocationUtil(ResourceLocation model, String layer) {

    @Environment(EnvType.CLIENT)
    public ModelLayerLocation toModelLayer() {
        return new ModelLayerLocation(this.model, this.layer);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ModelLayerLocationUtil modelLayerLocation)) {
            return false;
        } else {
            return this.model.equals(modelLayerLocation.model) && this.layer.equals(modelLayerLocation.layer);
        }
    }

    @Override
    public String toString() {
        return this.model + "#" + this.layer;
    }

}
