package net.threetag.palladium.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.threetag.palladium.Palladium;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumModelLayers {

    public static final ModelLayerLocation SUIT_STAND = new ModelLayerLocation(Palladium.id("suit_stand"), "main");
    public static final ModelLayerLocation SUIT_STAND_BASE_PLATE = new ModelLayerLocation(Palladium.id("suit_stand"), "base_plate");

    @SubscribeEvent
    static void modelLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(SUIT_STAND, SuitStandModel::createLayer);
        e.registerLayerDefinition(SUIT_STAND_BASE_PLATE, SuitStandBasePlateModel::createLayer);
    }

}
