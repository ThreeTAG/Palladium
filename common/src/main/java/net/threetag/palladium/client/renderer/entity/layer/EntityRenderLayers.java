package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.serialization.MapCodec;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.data.PalladiumEntityData;

public class EntityRenderLayers extends PalladiumEntityData<LivingEntity, EntityRenderLayers> {

    public static final MapCodec<EntityRenderLayers> CODEC = MapCodec.unit(EntityRenderLayers::create);

    public static EntityRenderLayers create() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            return new ClientEntityRenderLayers();
        } else {
            return new EntityRenderLayers();
        }
    }

    @Override
    public MapCodec<EntityRenderLayers> codec() {
        return CODEC;
    }
}
