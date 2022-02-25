package net.threetag.palladium.components.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;

public class PalladiumComponents implements EntityComponentInitializer {

    public static final ComponentKey<EntityPropertyHandlerComponent> ENTITY_PROPERTIES =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(Palladium.MOD_ID, "properties"), EntityPropertyHandlerComponent.class);

    public static final ComponentKey<PowerHandlerComponent> POWER_HANDLER =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(Palladium.MOD_ID, "power_handler"), PowerHandlerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ENTITY_PROPERTIES, EntityPropertyHandlerComponent::new, RespawnCopyStrategy.CHARACTER);
        registry.registerFor(Entity.class, ENTITY_PROPERTIES, EntityPropertyHandlerComponent::new);

        registry.registerForPlayers(POWER_HANDLER, PowerHandlerComponent::new, RespawnCopyStrategy.CHARACTER);
        registry.registerFor(LivingEntity.class, POWER_HANDLER, PowerHandlerComponent::new);
    }

}
