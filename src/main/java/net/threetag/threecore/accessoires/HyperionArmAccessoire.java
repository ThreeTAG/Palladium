package net.threetag.threecore.accessoires;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.PlayerUtil;

import javax.annotation.Nullable;

public class HyperionArmAccessoire extends AbstractReplaceLimbTextureAccessoire {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/hyperion_arm.png");
    public static final ResourceLocation TEXTURE_SLIM = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/hyperion_arm_slim.png");

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture(AbstractClientPlayerEntity player) {
        return PlayerUtil.hasSmallArms(player) ? TEXTURE_SLIM : TEXTURE;
    }

    @Nullable
    @Override
    public PlayerPart getPlayerPart() {
        return PlayerPart.RIGHT_ARM;
    }
}
