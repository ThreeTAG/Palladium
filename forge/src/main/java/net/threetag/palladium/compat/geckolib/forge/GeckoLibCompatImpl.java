package net.threetag.palladium.compat.geckolib.forge;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.threetag.palladium.compat.geckolib.ability.ArmorAnimationAbility;
import net.threetag.palladium.compat.geckolib.ability.RenderLayerAnimationAbility;
import net.threetag.palladium.compat.geckolib.armor.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.armor.PackGeckoArmorItem;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import net.threetag.palladium.item.AddonAttributeContainer;
import net.threetag.palladium.item.ExtendedArmor;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.mixin.client.GeoArmorRendererInvoker;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.PlayerSlot;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GeckoLibCompatImpl {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerAbility);
    }

    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerRenderers);
    }

    public static void registerAbility(RegisterEvent e) {
        e.register(Ability.REGISTRY.getRegistryKey(), new ResourceLocation(GeckoLib.ModID, "render_layer_animation"), RenderLayerAnimationAbility::new);
        e.register(Ability.REGISTRY.getRegistryKey(), new ResourceLocation(GeckoLib.ModID, "armor_animation"), ArmorAnimationAbility::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(ArmorItemImpl.class, () -> GeckoArmorRenderer.INSTANCE);
    }

    public static ArmorItem createArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slot, Item.Properties properties, boolean hideSecondLayer) {
        var item = new ArmorItemImpl(armorMaterial, slot, properties);

        if (hideSecondLayer) {
            item.hideSecondLayer();
        }

        return item;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderFirstPerson(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
        if (stack.getItem() instanceof PackGeckoArmorItem gecko && stack.getItem() instanceof ArmorItem armorItem) {
            var renderer = GeoArmorRenderer.getRenderer(armorItem.getClass(), player);

            if (renderer != null) {
                renderer.setCurrentItem(player, stack, EquipmentSlot.CHEST);

                (rightArm ? renderer.rightArm : renderer.leftArm).copyFrom(rendererArm);
                renderer.attackTime = 0.0F;
                renderer.crouching = false;
                renderer.swimAmount = 0.0F;
                renderer.rightArm.xRot = 0.0F;
                renderer.leftArm.xRot = 0.0F;

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

                    var buffer1 = buffer.getBuffer(renderer.getRenderType(gecko, Minecraft.getInstance().getFrameTime(), poseStack, buffer, null, combinedLight,
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

    public static GeoArmorRenderer getArmorRenderer(Class<? extends ArmorItem> clazz, Entity entity) {
        return GeoArmorRenderer.getRenderer(clazz, entity);
    }

    public static class ArmorItemImpl extends GeoArmorItem implements IAnimatable, IAddonItem, ExtendedArmor, PackGeckoArmorItem {

        private List<Component> tooltipLines;
        private final AddonAttributeContainer attributeContainer = new AddonAttributeContainer();
        private RenderLayerContainer renderLayerContainer = null;
        private boolean hideSecondLayer = false;
        private ResourceLocation texture, model, animationLocation;
        public List<ParsedAnimationController<IAnimatable>> animationControllers;
        private AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public ArmorItemImpl(ArmorMaterial materialIn, EquipmentSlot equipmentSlot, Properties builder) {
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

        public ArmorItemImpl hideSecondLayer() {
            this.hideSecondLayer = true;
            return this;
        }

        @Override
        public boolean hideSecondPlayerLayer(Player player, ItemStack stack, EquipmentSlot slot) {
            return this.hideSecondLayer;
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
            super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
            if (this.tooltipLines != null) {
                tooltipComponents.addAll(this.tooltipLines);
            }
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
            return this.attributeContainer.get(PlayerSlot.get(slot), super.getDefaultAttributeModifiers(slot));
        }

        @Override
        public AddonAttributeContainer getAttributeContainer() {
            return this.attributeContainer;
        }

        @Override
        public void setTooltip(List<Component> lines) {
            this.tooltipLines = lines;
        }

        @Override
        public void setRenderLayerContainer(RenderLayerContainer container) {
            this.renderLayerContainer = container;
        }

        @Override
        public RenderLayerContainer getRenderLayerContainer() {
            return this.renderLayerContainer;
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
