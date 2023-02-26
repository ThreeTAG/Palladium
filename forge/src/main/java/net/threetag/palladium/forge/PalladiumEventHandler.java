package net.threetag.palladium.forge;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.loot.LootTableModificationManager;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PalladiumEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLootTableLoad(LootTableLoadEvent e) {
        var mod = LootTableModificationManager.getInstance().getFor(e.getName());

        if (mod != null) {
            for (LootPool lootPool : mod.getLootPools()) {
                e.getTable().addPool(lootPool);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed e) {
        if (e.getEntity().getAttributes().hasAttribute(PalladiumAttributes.DESTROY_SPEED.get())) {
            e.setNewSpeed((float) (e.getNewSpeed() * e.getEntity().getAttributeValue(PalladiumAttributes.DESTROY_SPEED.get())));
        }
    }

}
