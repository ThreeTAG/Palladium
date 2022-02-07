package net.threetag.palladium.power.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.forge.capability.PowerCapability;
import net.threetag.palladium.power.IPowerHolder;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID)
public class PowerManagerImpl {

    public static IPowerHolder getPowerHolder(LivingEntity entity) {
        return entity.getCapability(PowerCapability.POWER).orElseThrow(() -> new RuntimeException("Entity does not have power capability!"));
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        getPowerHolder(e.getEntityLiving()).tick(e.getEntityLiving());
    }

}
