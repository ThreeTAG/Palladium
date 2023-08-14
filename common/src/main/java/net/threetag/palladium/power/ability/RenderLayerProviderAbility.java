package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;

public interface RenderLayerProviderAbility {

    @Environment(EnvType.CLIENT)
    IPackRenderLayer getRenderLayer(AbilityEntry entry, LivingEntity entity, PackRenderLayerManager manager);

}
