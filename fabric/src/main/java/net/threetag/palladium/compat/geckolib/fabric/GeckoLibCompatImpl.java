package net.threetag.palladium.compat.geckolib.fabric;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.compat.geckolib.ability.ArmorAnimationAbility;
import net.threetag.palladium.compat.geckolib.ability.RenderLayerAnimationAbility;
import net.threetag.palladium.compat.geckolib.armor.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.armor.PackGeckoArmorItem;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import net.threetag.palladium.item.AddonArmorItem;
import net.threetag.palladium.mixin.client.GeoArmorRendererInvoker;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladiumcore.registry.DeferredRegister;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

@SuppressWarnings("ALL")
public class GeckoLibCompatImpl {

    public static void init() {
        DeferredRegister<Ability> deferredRegister = DeferredRegister.create(GeckoLib.ModID, Ability.REGISTRY);
        deferredRegister.register();
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

    public static GeoArmorRenderer getArmorRenderer(Class<? extends ArmorItem> clazz, Entity wearer) {
        return GeoArmorRenderer.getRenderer(clazz);
    }

    @Environment(EnvType.CLIENT)
    public static void renderFirstPerson(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
        if (stack.getItem() instanceof PackGeckoArmorItem gecko && stack.getItem() instanceof ArmorItem armorItem) {
            var renderer = GeoArmorRenderer.getRenderer(armorItem.getClass());

            if (renderer != null) {
                renderer.setCurrentItem(player, stack, EquipmentSlot.CHEST, ((PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getModel());

                GeoModel model = renderer.getGeoModelProvider().getModel(renderer.getGeoModelProvider().getModelResource(armorItem));

                model.getBone(rightArm ? renderer.rightArmBone : renderer.leftArmBone).ifPresent(bone -> {
                    AnimationEvent<?> animationEvent = new AnimationEvent<>(gecko, 0, 0,
                            Minecraft.getInstance().getFrameTime(), false,
                            List.of());

                    poseStack.pushPose();
                    poseStack.translate(0, 24 / 16F, 0);
                    poseStack.scale(-1, -1, 1);

                    renderer.getGeoModelProvider().setCustomAnimations(gecko, renderer.getInstanceId(armorItem), animationEvent);
                    renderer.setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);

                    if (renderer instanceof GeoArmorRendererInvoker invoker) {
                        invoker.invokeFitToBiped();
                    }

                    RenderSystem.setShaderTexture(0, renderer.getTextureLocation(armorItem));

                    var buffer1 = buffer.getBuffer(renderer.getRenderType(armorItem, Minecraft.getInstance().getFrameTime(), poseStack, buffer, null, combinedLight,
                            renderer.getTextureLocation(armorItem)));
                    Color renderColor = renderer.getRenderColor(armorItem, 0, poseStack, null, buffer1, combinedLight);

                    renderer.setCurrentRTB(buffer);
                    renderer.renderEarly(armorItem, poseStack, Minecraft.getInstance().getFrameTime(), buffer, buffer1, combinedLight,
                            OverlayTexture.NO_OVERLAY, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                            renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);

                    renderer.renderLate(armorItem, poseStack, Minecraft.getInstance().getFrameTime(), buffer, buffer1, combinedLight,
                            OverlayTexture.NO_OVERLAY, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                            renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
                    renderer.renderRecursively(bone,
                            poseStack, buffer1, combinedLight, OverlayTexture.NO_OVERLAY, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                            renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
                    renderer.setCurrentModelRenderCycle(EModelRenderCycle.REPEATED);

                    poseStack.popPose();
                });
            }
        }
    }

    public static class ArmorItemImpl extends AddonArmorItem implements IAnimatable, PackGeckoArmorItem {

        private TextureReference texture;
        private ResourceLocation model, animationLocation;
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
        public PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, TextureReference textureLocation, ResourceLocation animationLocation, List<ParsedAnimationController<IAnimatable>> animationControllers) {
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
        public TextureReference getGeckoTextureLocation() {
            return this.texture;
        }

        @Override
        public ResourceLocation getGeckoAnimationLocation() {
            return this.animationLocation;
        }
    }

}
