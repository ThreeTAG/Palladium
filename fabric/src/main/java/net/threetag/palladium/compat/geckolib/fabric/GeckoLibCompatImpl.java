package net.threetag.palladium.compat.geckolib.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.threetag.palladium.compat.geckolib.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.PackGeckoArmorItem;
import net.threetag.palladium.item.AddonArmorItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GeckoLibCompatImpl {

    public static void init() {
        for (Item item : Registry.ITEM) {
            if (item instanceof ArmorItemImpl) {
                GeoArmorRenderer.registerArmorRenderer(GeckoArmorRenderer.INSTANCE, item);
            }
        }
    }

    public static ArmorItem createArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slot, Item.Properties properties, boolean hideSecondLayer) {
        var item = new ArmorItemImpl(armorMaterial, slot, properties);

        if (hideSecondLayer) {
            item.hideSecondLayer();
        }

        return item;
    }

    public static class ArmorItemImpl extends AddonArmorItem implements IAnimatable, PackGeckoArmorItem {

        private ResourceLocation texture, model, animation;
        private String animationName = "animation.armor.loop";
        private AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public ArmorItemImpl(ArmorMaterial materialIn, EquipmentSlot equipmentSlot, Item.Properties builder) {
            super(materialIn, equipmentSlot, builder);
        }

        @Override
        public void registerControllers(AnimationData data) {
            if(this.animation != null) {
                data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
            }
        }

        private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationName, ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        @Override
        public AnimationFactory getFactory() {
            return this.factory;
        }

        @Override
        public PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, String animationName) {
            this.model = modelLocation;
            this.texture = textureLocation;
            this.animation = animationLocation;
            this.animationName = animationName;
            return this;
        }

        @Override
        public ResourceLocation getGeckoModelLocation() {
            return this.model;
        }

        @Override
        public ResourceLocation getGeckoTextureLocation() {
            return this.texture;
        }

        @Override
        public ResourceLocation getGeckoAnimationLocation() {
            return this.animation;
        }
    }

}
