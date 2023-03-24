package net.threetag.palladium.compat.geckolib.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.threetag.palladium.compat.geckolib.*;
import net.threetag.palladium.item.AddonArmorItem;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladiumcore.registry.DeferredRegister;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

@SuppressWarnings("ALL")
public class GeckoLibCompatImpl {

    public static void init() {
        DeferredRegister<Ability> deferredRegister = DeferredRegister.create(GeckoLib.ModID, Ability.REGISTRY);
        deferredRegister.register("render_layer_animation", RenderLayerAnimationAbility::new);
        deferredRegister.register("armor_animation", ArmorAnimationAbility::new);
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
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

    public static GeoArmorRenderer getArmorRenderer(Class<? extends ArmorItem> clazz, LivingEntity wearer) {
        return GeoArmorRenderer.getRenderer(clazz);
    }

    public static class ArmorItemImpl extends AddonArmorItem implements IAnimatable, PackGeckoArmorItem {

        private ResourceLocation texture, model, animationLocation;
        public List<ParsedAnimationController<IAnimatable>> animationControllers;
        private AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public ArmorItemImpl(ArmorMaterial materialIn, EquipmentSlot equipmentSlot, Item.Properties builder) {
            super(materialIn, equipmentSlot, builder);
        }

        @Override
        public void registerControllers(AnimationData data) {
            if (this.animationLocation != null) {
                for (ParsedAnimationController<IAnimatable> parsed : this.animationControllers) {
                    var controller = parsed.createController(this);
                    data.addAnimationController(controller);
                }
            }
        }

        @Override
        public AnimationFactory getFactory() {
            return this.factory;
        }

        @Override
        public PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, List<ParsedAnimationController<IAnimatable>> animationControllers) {
            this.model = modelLocation;
            this.texture = textureLocation;
            this.animationLocation = animationLocation;
            this.animationControllers = animationControllers;
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
            return this.animationLocation;
        }
    }

}
