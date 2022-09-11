package net.threetag.palladium.entity.effect;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class EntityEffects {

    public static final DeferredRegister<EntityEffect> EFFECTS = DeferredRegister.create(Palladium.MOD_ID, EntityEffect.REGISTRY);

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
