package net.threetag.threecore.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayer;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerProvider;
import net.threetag.threecore.client.renderer.entity.modellayer.ModelLayerContext;

public class PlayerHandRenderer {

    public static void renderRightArm(PlayerRenderer playerRenderer, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity clientPlayer) {
        playerRenderer.renderRightArm(matrixStack, bufferIn, combinedLightIn, clientPlayer);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.HEAD, playerRenderer, HandSide.RIGHT);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.CHEST, playerRenderer, HandSide.RIGHT);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.LEGS, playerRenderer, HandSide.RIGHT);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.FEET, playerRenderer, HandSide.RIGHT);

        ModelLayerContext context = new ModelLayerContext(clientPlayer);
        for (Ability ability : AbilityHelper.getAbilities(clientPlayer)) {
            if (ability instanceof IModelLayerProvider && ability.getConditionManager().isEnabled()) {
                renderLayers(matrixStack, bufferIn, combinedLightIn, (IModelLayerProvider) ability, context, playerRenderer, HandSide.RIGHT);
            }
        }
    }

    public static void renderLeftArm(PlayerRenderer playerRenderer, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity clientPlayer) {
        playerRenderer.renderLeftArm(matrixStack, bufferIn, combinedLightIn, clientPlayer);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.HEAD, playerRenderer, HandSide.LEFT);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.CHEST, playerRenderer, HandSide.LEFT);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.LEGS, playerRenderer, HandSide.LEFT);
        renderItemLayers(matrixStack, bufferIn, combinedLightIn, clientPlayer, EquipmentSlotType.FEET, playerRenderer, HandSide.LEFT);

        ModelLayerContext context = new ModelLayerContext(clientPlayer);
        for (Ability ability : AbilityHelper.getAbilities(clientPlayer)) {
            if (ability instanceof IModelLayerProvider && ability.getConditionManager().isEnabled()) {
                renderLayers(matrixStack, bufferIn, combinedLightIn, (IModelLayerProvider) ability, context, playerRenderer, HandSide.LEFT);
            }
        }
    }

    public static void renderItemLayers(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, AbstractClientPlayerEntity playerEntity, EquipmentSlotType slot, PlayerRenderer playerRenderer, HandSide handSide) {
        ItemStack stack = playerEntity.getItemStackFromSlot(slot);
        ModelLayerContext context = new ModelLayerContext(playerEntity, stack, slot);

        if (stack.getItem() instanceof IModelLayerProvider) {
            for (IModelLayer layer : ((IModelLayerProvider) stack.getItem()).getModelLayers(context)) {
                if (layer.isActive(context)) {
                    layer.renderArm(handSide, context, playerRenderer, matrixStack, renderTypeBuffer, packedLightIn);
                }
            }
        }
    }

    public static void renderLayers(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, IModelLayerProvider provider, IModelLayerContext context, PlayerRenderer playerRenderer, HandSide handSide) {
        for (IModelLayer layer : provider.getModelLayers(context)) {
            if (layer.isActive(context)) {
                layer.renderArm(handSide, context, playerRenderer, matrixStack, renderTypeBuffer, packedLightIn);
            }
        }
    }

}
