package net.threetag.palladium.entity.effect;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.event.PalladiumEvents;

public class EntityEffects {

    public static final DeferredRegister<EntityEffect> EFFECTS = DeferredRegister.create(Palladium.MOD_ID, EntityEffect.RESOURCE_KEY);

    public static final RegistrySupplier<EntityEffect> ENERGY_BLAST = EFFECTS.register("energy_blast", EnergyBlastEffect::new);

    public static void init() {
        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            if (handler.getEntity() instanceof EffectEntity) {
                for (EntityEffect entityEffect : EntityEffect.REGISTRY) {
                    entityEffect.registerProperties(handler);
                }
            }
        });
    }

}
