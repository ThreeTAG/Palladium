package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.accessory.RenderLayerAccessory;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.util.property.AccessorySlotProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

import java.util.concurrent.atomic.AtomicReference;

public class RenderLayerByAccessorySlotAbility extends Ability implements RenderLayerProviderAbility {

    public static final PalladiumProperty<ResourceLocation> DEFAULT = new ResourceLocationProperty("default_layer").configurable("ID of the render layer that will be used if none is specified in the accessory slot. Can be null.");
    public static final PalladiumProperty<AccessorySlot> SLOT = new AccessorySlotProperty("accessory_slot").configurable("ID of the slot that will be looked in for to get a render layer. There must be a render_layer accessory in it to get it from.");

    public RenderLayerByAccessorySlotAbility() {
        this.withProperty(DEFAULT, new ResourceLocation("namespace", "render_layer_id"));
        this.withProperty(SLOT, AccessorySlot.CHEST);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public IPackRenderLayer getRenderLayer(AbilityEntry entry, LivingEntity entity, PackRenderLayerManager manager) {
        AtomicReference<ResourceLocation> layerId = new AtomicReference<>(entry.getProperty(DEFAULT));

        if(entity instanceof Player player) {
            Accessory.getPlayerData(player).ifPresent(data -> {
                var slot = entry.getProperty(SLOT);
                var slots = data.getSlots();

                if(slot != null && slots.containsKey(slot)) {
                    slots.get(slot).stream().filter(a -> a instanceof RenderLayerAccessory).findFirst().ifPresent(a -> {
                        layerId.set(((RenderLayerAccessory) a).renderLayerId);
                    });
                }
            });
        }

        return manager.getLayer(layerId.get());
    }
}
