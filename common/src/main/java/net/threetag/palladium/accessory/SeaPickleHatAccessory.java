package net.threetag.palladium.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;

import java.util.Collection;
import java.util.Collections;

public class SeaPickleHatAccessory extends Accessory {

    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        renderLayerParent.getModel().head.translateAndRotate(poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180F));
        poseStack.translate(-0.5F, 0.5F, -0.5F);
        var blockRendererDispatcher = Minecraft.getInstance().getBlockRenderer();
        blockRendererDispatcher.renderSingleBlock(player.isEyeInFluid(FluidTags.WATER) ? Blocks.SEA_PICKLE.defaultBlockState() : Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.WATERLOGGED, false), poseStack, bufferSource, packedLightIn, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public Collection<AccessorySlot> getPossibleSlots() {
        return Collections.singletonList(AccessorySlot.HAT);
    }
}
