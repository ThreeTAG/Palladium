package net.threetag.palladium.compat.geckolib.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayerModel;
import net.threetag.palladium.util.context.DataContext;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@SuppressWarnings("rawtypes")
public class GeckoArmorRenderer<T extends ArmorItem & PackGeckoArmorItem> extends GeoArmorRenderer<T> {

    public static LivingEntity RENDERED_ENTITY;
    public static final GeckoArmorRenderer INSTANCE = new GeckoArmorRenderer();

    public GeckoArmorRenderer() {
        super(new AnimatedGeoModel<T>() {
            @Override
            public ResourceLocation getModelResource(T object) {
                return object.getGeckoModelLocation();
            }

            @Override
            public ResourceLocation getTextureResource(T object) {
                return object.getGeckoTextureLocation().getTexture(DataContext.forEntity(RENDERED_ENTITY));
            }

            @Override
            public ResourceLocation getAnimationResource(T animatable) {
                return animatable.getGeckoAnimationLocation();
            }
        });
    }
}
