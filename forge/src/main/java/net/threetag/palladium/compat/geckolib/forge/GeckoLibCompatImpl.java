package net.threetag.palladium.compat.geckolib.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.threetag.palladium.compat.geckolib.ability.ArmorAnimationAbility;
import net.threetag.palladium.compat.geckolib.ability.RenderLayerAnimationAbility;
import net.threetag.palladium.compat.geckolib.armor.AddonGeoArmorItem;
import net.threetag.palladium.compat.geckolib.armor.GeckoArmorRenderer;
import net.threetag.palladium.power.ability.Ability;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.GeckoLib;

import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes"})
public class GeckoLibCompatImpl {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerAbility);
    }

    public static void registerAbility(RegisterEvent e) {
        e.register(Ability.REGISTRY.getRegistryKey(), new ResourceLocation(GeckoLib.MOD_ID, "render_layer_animation"), RenderLayerAnimationAbility::new);
        e.register(Ability.REGISTRY.getRegistryKey(), new ResourceLocation(GeckoLib.MOD_ID, "armor_animation"), ArmorAnimationAbility::new);
    }

    public static AddonGeoArmorItem createArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Item.Properties builder) {
        return new ArmorItemImpl(materialIn, type, builder);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderFirstPerson(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
//        if (stack.getItem() instanceof PackGeckoArmorItem gecko && stack.getItem() instanceof ArmorItem armorItem) {
//            var renderer = GeoArmorRenderer.getRenderer(armorItem.getClass(), player);
//
//            if (renderer != null) {
//                renderer.setCurrentItem(player, stack, EquipmentSlot.CHEST);
//
//                (rightArm ? renderer.rightArm : renderer.leftArm).copyFrom(rendererArm);
//                renderer.attackTime = 0.0F;
//                renderer.crouching = false;
//                renderer.swimAmount = 0.0F;
//                renderer.rightArm.xRot = 0.0F;
//                renderer.leftArm.xRot = 0.0F;
//
//                GeoModel model = renderer.getGeoModelProvider().getModel(renderer.getGeoModelProvider().getModelResource(armorItem));
//
//                model.getBone(rightArm ? renderer.rightArmBone : renderer.leftArmBone).ifPresent(bone -> {
//                    AnimationEvent<?> animationEvent = new AnimationEvent<>(gecko, 0, 0,
//                            Minecraft.getInstance().getFrameTime(), false,
//                            List.of());
//
//                    poseStack.pushPose();
//                    poseStack.translate(0, 24 / 16F, 0);
//                    poseStack.scale(-1, -1, 1);
//
//                    renderer.getGeoModelProvider().setCustomAnimations(gecko, renderer.getInstanceId(armorItem), animationEvent);
//                    renderer.setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
//
//                    if (renderer instanceof GeoArmorRendererInvoker invoker) {
//                        invoker.invokeApplyBaseTransformations();
//                    }
//
//                    RenderSystem.setShaderTexture(0, renderer.getTextureLocation(armorItem));
//
//                    var buffer1 = buffer.getBuffer(renderer.getRenderType(gecko, Minecraft.getInstance().getFrameTime(), poseStack, buffer, null, combinedLight,
//                            renderer.getTextureLocation(armorItem)));
//                    Color renderColor = renderer.getRenderColor(armorItem, 0, poseStack, null, buffer1, combinedLight);
//
//                    renderer.setCurrentRTB(buffer);
//                    renderer.renderEarly(armorItem, poseStack, Minecraft.getInstance().getFrameTime(), buffer, buffer1, combinedLight,
//                            OverlayTexture.NO_OVERLAY, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
//                            renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
//
//                    renderer.renderLate(armorItem, poseStack, Minecraft.getInstance().getFrameTime(), buffer, buffer1, combinedLight,
//                            OverlayTexture.NO_OVERLAY, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
//                            renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
//                    renderer.renderRecursively(bone,
//                            poseStack, buffer1, combinedLight, OverlayTexture.NO_OVERLAY, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
//                            renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
//                    renderer.setCurrentModelRenderCycle(EModelRenderCycle.REPEATED);
//
//                    poseStack.popPose();
//                });
//            }
//        }
    }

    public static class ArmorItemImpl extends AddonGeoArmorItem {

        public ArmorItemImpl(ArmorMaterial materialIn, Type type, Properties builder) {
            super(materialIn, type, builder);
        }

        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                private GeckoArmorRenderer<?> renderer;

                @Override
                public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                    if (this.renderer == null)
                        this.renderer = new GeckoArmorRenderer(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())));

                    this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                    return this.renderer;
                }
            });
        }
    }

}
