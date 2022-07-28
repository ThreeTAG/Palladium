package net.threetag.palladium.capability.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.PowerHandler;
import net.threetag.palladium.util.property.EntityPropertyHandler;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumCapabilities {

    public static Capability<EntityPropertyHandler> ENTITY_PROPERTIES = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<IPowerHandler> POWER_HANDLER = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<AccessoryPlayerData> ACCESSORIES = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent e) {
        e.register(IPowerHandler.class);
        e.register(EntityPropertyHandler.class);
        e.register(AccessoryPlayerData.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        e.addCapability(new ResourceLocation(Palladium.MOD_ID, "properties"), new EntityPropertyProvider(new EntityPropertyHandler(e.getObject())));

        if (e.getObject() instanceof LivingEntity) {
            e.addCapability(new ResourceLocation(Palladium.MOD_ID, "power_handler"), new PowerProvider(new PowerHandler((LivingEntity) e.getObject())));
        }

        if (e.getObject() instanceof Player) {
            e.addCapability(new ResourceLocation(Palladium.MOD_ID, "accessories"), new AccessoriesProvider(new AccessoryPlayerData()));
        }
    }

}
