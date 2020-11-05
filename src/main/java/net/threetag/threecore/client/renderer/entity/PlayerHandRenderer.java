package net.threetag.threecore.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.HandSide;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.HideBodyPartsAbility;
import net.threetag.threecore.client.renderer.entity.modellayer.ModelLayerManager;
import net.threetag.threecore.util.threedata.BodyPartListThreeData;

public class PlayerHandRenderer {

    public static void renderRightArm(PlayerRenderer playerRenderer, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity clientPlayer) {
        boolean renderArm = true;
        for (HideBodyPartsAbility ability : AbilityHelper.getAbilitiesFromClass(clientPlayer, HideBodyPartsAbility.class)) {
            if (ability.getConditionManager().isEnabled() && ability.get(HideBodyPartsAbility.AFFECTS_FIRST_PERSON) && ability.get(HideBodyPartsAbility.BODY_PARTS).contains(BodyPartListThreeData.BodyPart.RIGHT_ARM)) {
                renderArm = false;
                break;
            }
        }

        if (renderArm) {
            playerRenderer.renderRightArm(matrixStack, bufferIn, combinedLightIn, clientPlayer);
        }

        ModelLayerManager.forEachLayer(clientPlayer, (layer, context) -> {
            if (layer.isActive(context)) {
                layer.renderArm(HandSide.RIGHT, context, playerRenderer, matrixStack, bufferIn, combinedLightIn);
            }
        });
    }

    public static void renderLeftArm(PlayerRenderer playerRenderer, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity clientPlayer) {
        boolean renderArm = true;
        for (HideBodyPartsAbility ability : AbilityHelper.getAbilitiesFromClass(clientPlayer, HideBodyPartsAbility.class)) {
            if (ability.getConditionManager().isEnabled() && ability.get(HideBodyPartsAbility.AFFECTS_FIRST_PERSON) && ability.get(HideBodyPartsAbility.BODY_PARTS).contains(BodyPartListThreeData.BodyPart.LEFT_ARM)) {
                renderArm = false;
                break;
            }
        }

        if (renderArm) {
            playerRenderer.renderLeftArm(matrixStack, bufferIn, combinedLightIn, clientPlayer);
        }

        ModelLayerManager.forEachLayer(clientPlayer, (layer, context) -> {
            if (layer.isActive(context)) {
                layer.renderArm(HandSide.LEFT, context, playerRenderer, matrixStack, bufferIn, combinedLightIn);
            }
        });
    }

}
