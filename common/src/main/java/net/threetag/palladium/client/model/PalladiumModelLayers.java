package net.threetag.palladium.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.threetag.palladium.Palladium;

public class PalladiumModelLayers {

    public static final ModelLayerLocation SUIT_STAND = new ModelLayerLocation(Palladium.id("suit_stand"), "main");
    public static final ModelLayerLocation SUIT_STAND_BASE_PLATE = new ModelLayerLocation(Palladium.id("suit_stand"), "base_plate");

    public static void init() {
        EntityModelLayerRegistry.register(SUIT_STAND, SuitStandModel::createLayer);
        EntityModelLayerRegistry.register(SUIT_STAND_BASE_PLATE, SuitStandBasePlateModel::createLayer);
    }

}
