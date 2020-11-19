package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilityAccessoires;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, value = Dist.CLIENT)
public class AccessoireLayerRenderer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public AccessoireLayerRenderer(PlayerRenderer playerRenderer) {
        super(playerRenderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> accessoireHolder.getSlots().forEach(((slot, accessoires) -> {
            for (Accessoire accessoire : accessoires) {
                if (accessoire.isVisible(slot, player)) {
                    accessoire.render((PlayerRenderer) this.entityRenderer, slot, matrixStackIn, bufferIn, packedLightIn, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        })));
    }
}
