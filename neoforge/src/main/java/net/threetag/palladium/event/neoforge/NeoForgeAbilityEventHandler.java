package net.threetag.palladium.event.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class NeoForgeAbilityEventHandler {

    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent e) {
        AbilityUtil.getEnabledInstances(e.getEntity(), AbilitySerializers.INVISIBILITY.get()).forEach(instance -> {
            e.modifyVisibility(instance.getAbility().mobVisibilityModifier);
        });
    }

}
