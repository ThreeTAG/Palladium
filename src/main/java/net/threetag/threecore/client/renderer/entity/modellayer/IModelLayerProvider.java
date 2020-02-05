package net.threetag.threecore.client.renderer.entity.modellayer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IModelLayerProvider {

    @OnlyIn(Dist.CLIENT)
    List<IModelLayer> getModelLayers(IModelLayerContext context);

}
