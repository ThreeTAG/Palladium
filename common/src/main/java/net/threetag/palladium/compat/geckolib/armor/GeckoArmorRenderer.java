package net.threetag.palladium.compat.geckolib.armor;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class GeckoArmorRenderer<T extends AddonGeoArmorItem> extends GeoArmorRenderer<T> {

    public GeckoArmorRenderer(ResourceLocation itemId) {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(itemId.getNamespace(), "armor/" + itemId.getPath())));

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
