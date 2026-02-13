package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.threetag.palladium.entity.data.PalladiumEntityData;

public class EntityRenderLayers extends PalladiumEntityData<LivingEntity, EntityRenderLayers> {

    public static final Codec<EntityRenderLayers> CODEC = MapCodec.unit(EntityRenderLayers::create).codec();

    public static EntityRenderLayers create() {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            return new ClientEntityRenderLayers();
        } else {
            return new EntityRenderLayers();
        }
    }

    @Override
    public Codec<EntityRenderLayers> codec() {
        return CODEC;
    }
}
