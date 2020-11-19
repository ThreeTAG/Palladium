package net.threetag.threecore.accessoires;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.PlayerUtil;

import java.util.Arrays;
import java.util.Collection;

public class HyperionArmAccessoire extends AbstractReplaceLimbTextureAccessoire {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/hyperion_arms.png");
    public static final ResourceLocation TEXTURE_SLIM = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/hyperion_slim_arms.png");

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture(AbstractClientPlayerEntity player, AccessoireSlot slot) {
        return PlayerUtil.hasSmallArms(player) ? TEXTURE_SLIM : TEXTURE;
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Arrays.asList(AccessoireSlot.MAIN_ARM, AccessoireSlot.OFF_ARM);
    }
}
