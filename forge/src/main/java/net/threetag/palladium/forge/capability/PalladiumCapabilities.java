package net.threetag.palladium.forge.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.IPowerHolder;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumCapabilities {

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent e) {
        e.register(IPowerHolder.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof LivingEntity) {
            e.addCapability(new ResourceLocation(Palladium.MOD_ID, "power"), new PowerProvider(new PowerCapability((LivingEntity) e.getObject())));
        }
    }

}
