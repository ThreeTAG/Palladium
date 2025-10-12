package net.threetag.palladium.fabric.mixin.client;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.model.ModelLayerManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelLayerManager.class)
public abstract class ModelLayerManagerMixin implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return ModelLayerManager.ID;
    }

}
