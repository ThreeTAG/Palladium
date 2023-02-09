package net.threetag.palladium.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.renderlayer.ModelLookup;
import net.threetag.palladium.item.ExtendedArmor;
import net.threetag.palladium.util.SkinTypedValue;

import java.util.HashMap;
import java.util.Map;

public class ArmorModelManager implements ResourceManagerReloadListener {

    private static final Map<Item, Handler> HANDLERS = new HashMap<>();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        HANDLERS.values().forEach(handler -> handler.onReload(Minecraft.getInstance().getEntityModels()));
    }

    public static void register(Item item, Handler handler) {
        HANDLERS.put(item, handler);
    }

    public static void register(Item item, SkinTypedValue<ModelLookup.Model> modelLookup, SkinTypedValue<ModelLayerLocation> modelLayerLocation) {
        register(item, new Simple(modelLookup, modelLayerLocation));
    }

    public static void register(Item item, ModelLayerLocation modelLayerLocation) {
        register(item, new Simple(new SkinTypedValue<>(ModelLookup.HUMANOID), new SkinTypedValue<>(modelLayerLocation)));
    }

    public static Handler get(Item item) {
        return HANDLERS.get(item);
    }

    public static void renderFirstPerson(AbstractClientPlayer player, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
        var stack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!stack.isEmpty() && HANDLERS.containsKey(stack.getItem()) && stack.getItem() instanceof ExtendedArmor customArmorTexture) {
            var handler = get(stack.getItem());
            var armorModel = handler.getArmorModel(stack, player, EquipmentSlot.CHEST);
            var vertex = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(customArmorTexture.getArmorTexture(stack, player, EquipmentSlot.CHEST, null)), false, stack.hasFoil());
            var arm = rightArm ? armorModel.rightArm : armorModel.leftArm;
            arm.copyFrom(rendererArm);
            arm.xRot = 0.0F;
            arm.render(poseStack, vertex, combinedLight, OverlayTexture.NO_OVERLAY);

            if (armorModel instanceof PlayerModel<?> playerModel) {
                arm = rightArm ? playerModel.rightSleeve : playerModel.leftSleeve;
                arm.copyFrom(rendererArm);
                arm.xRot = 0.0F;
                arm.render(poseStack, vertex, combinedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    public interface Handler {

        HumanoidModel<?> getArmorModel(ItemStack stack, LivingEntity entity, EquipmentSlot slot);

        void onReload(EntityModelSet entityModelSet);
    }

    public static class Simple implements Handler {

        private final SkinTypedValue<ModelLayerLocation> modelLayerLocation;
        private final SkinTypedValue<ModelLookup.Model> modelLookup;
        private SkinTypedValue<EntityModel<LivingEntity>> model;

        public Simple(SkinTypedValue<ModelLookup.Model> modelLookup, SkinTypedValue<ModelLayerLocation> modelLayerLocation) {
            this.modelLayerLocation = modelLayerLocation;
            this.modelLookup = modelLookup;
        }

        @Override
        public HumanoidModel<?> getArmorModel(ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
            return (HumanoidModel<?>) this.model.get(entity);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void onReload(EntityModelSet entityModelSet) {
            this.model = new SkinTypedValue(modelLookup.getNormal().getModel(entityModelSet.bakeLayer(modelLayerLocation.getNormal())),
                    modelLookup.getSlim().getModel(entityModelSet.bakeLayer(modelLayerLocation.getSlim())));
        }
    }

}
