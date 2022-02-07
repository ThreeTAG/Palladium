package net.threetag.palladium.fabric.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;

public class PalladiumComponents implements EntityComponentInitializer {

    public static final ComponentKey<EntityPowerHolderComponent> POWER_HOLDER =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(Palladium.MOD_ID, "power_holder"), EntityPowerHolderComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(POWER_HOLDER, player -> new EntityPowerHolderComponent(), RespawnCopyStrategy.CHARACTER);
        registry.registerFor(LivingEntity.class, POWER_HOLDER, player -> new EntityPowerHolderComponent());
    }

}
