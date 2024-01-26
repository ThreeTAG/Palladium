package net.threetag.palladium.compat.geckolib.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.compat.geckolib.ability.ArmorAnimationAbility;
import net.threetag.palladium.compat.geckolib.ability.RenderLayerAnimationAbility;
import net.threetag.palladium.compat.geckolib.armor.AddonGeoArmorItem;
import net.threetag.palladium.compat.geckolib.armor.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayer;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayerModel;
import net.threetag.palladium.mixin.client.GeoArmorRendererInvoker;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladiumcore.registry.DeferredRegister;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.util.RenderUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes", "ConstantValue"})
public class GeckoLibCompatImpl {

    public static void init() {
        DeferredRegister<Ability> deferredRegister = DeferredRegister.create(GeckoLib.MOD_ID, Ability.REGISTRY);
        deferredRegister.register();
        deferredRegister.register("render_layer_animation", RenderLayerAnimationAbility::new);
        deferredRegister.register("armor_animation", ArmorAnimationAbility::new);
    }

    public static AddonGeoArmorItem createArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Item.Properties builder) {
        return new ArmorItemImpl(materialIn, type, builder);
    }

    @Environment(EnvType.CLIENT)
    public static void renderFirstPerson(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, ModelPart rendererArm, boolean rightArm) {
        if (stack.getItem() instanceof ArmorItemImpl gecko) {
            var rendererProvider = gecko.getRenderProvider().get();

            if (rendererProvider instanceof RenderProvider provider) {
                PlayerModel origModel = ((PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getModel();
                GeckoArmorRenderer<AddonGeoArmorItem> renderer = (GeckoArmorRenderer) provider.getHumanoidArmorModel(player, stack, EquipmentSlot.CHEST, origModel);

                if (rendererProvider instanceof GeoArmorRendererInvoker invoker) {
                    invoker.invokeGrabRelevantBones(renderer.getGeoModel().getBakedModel(renderer.getGeoModel().getModelResource(gecko)));
                }

                var bone = (rightArm ? renderer.getRightArmBone() : renderer.getLeftArmBone());

                if (bone != null) {
                    var partialTick = Minecraft.getInstance().getFrameTime();
                    RenderType renderType = renderer.getRenderType(gecko, renderer.getTextureLocation(gecko), bufferSource, partialTick);
                    VertexConsumer buffer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, stack.hasFoil());

                    RenderUtils.matchModelPartRot(rendererArm, bone);
                    GeckoRenderLayerModel.copyScaleAndVisibility(rendererArm, bone);
                    bone.updatePosition(rendererArm.x + (rightArm ? 5 : -5), 2 - rendererArm.y, rendererArm.z);

                    poseStack.pushPose();
                    poseStack.translate(0, 24 / 16F, 0);
                    poseStack.scale(-1, -1, 1);

                    Color renderColor = renderer.getRenderColor(gecko, partialTick, combinedLight);
                    float red = renderColor.getRedFloat();
                    float green = renderColor.getGreenFloat();
                    float blue = renderColor.getBlueFloat();
                    float alpha = renderColor.getAlphaFloat();
                    int packedOverlay = renderer.getPackedOverlay(gecko, 0, partialTick);

                    AnimationState<AddonGeoArmorItem> animationState = new AnimationState<>(gecko, 0, 0, partialTick, false);
                    long instanceId = renderer.getInstanceId(gecko);

                    animationState.setData(DataTickets.TICK, gecko.getTick(player));
                    animationState.setData(DataTickets.ITEMSTACK, stack);
                    animationState.setData(DataTickets.ENTITY, player);
                    animationState.setData(DataTickets.EQUIPMENT_SLOT, EquipmentSlot.CHEST);
                    renderer.getGeoModel().addAdditionalStateData(gecko, instanceId, animationState::setData);
                    renderer.getGeoModel().handleAnimations(gecko, instanceId, animationState);
                    renderer.renderRecursively(poseStack, gecko, bone, renderType, bufferSource, buffer, false, partialTick, combinedLight, packedOverlay, red, green, blue, alpha);

                    poseStack.popPose();
                }
            }
        }
    }

    public static class ArmorItemImpl extends AddonGeoArmorItem {

        private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

        public ArmorItemImpl(ArmorMaterial materialIn, Type type, Properties builder) {
            super(materialIn, type, builder);
        }

        @Override
        public void createRenderer(Consumer<Object> consumer) {
            consumer.accept(new RenderProvider() {
                private GeckoArmorRenderer<?> renderer;

                @Override
                public HumanoidModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original) {
                    if (this.renderer == null)
                        this.renderer = new GeckoArmorRenderer((AddonGeoArmorItem) itemStack.getItem());

                    this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                    return this.renderer;
                }
            });
        }

        @Override
        public Supplier<Object> getRenderProvider() {
            return this.renderProvider;
        }
    }

}
