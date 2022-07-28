package net.threetag.palladium.accessory;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.PlayerUtil;

import java.util.Arrays;
import java.util.Collection;

public class WinterSoldierArmAccessory extends AbstractReplaceLimbTextureAccessory {

    public static final ResourceLocation TEXTURE = Palladium.id("textures/models/accessories/winter_soldier_arms.png");
    public static final ResourceLocation TEXTURE_SLIM = Palladium.id("textures/models/accessories/winter_soldier_slim_arms.png");

    @Override
    public ResourceLocation getTexture(AbstractClientPlayer player, AccessorySlot slot) {
        return PlayerUtil.hasSmallArms(player) ? TEXTURE_SLIM : TEXTURE;
    }

    @Override
    public Collection<AccessorySlot> getPossibleSlots() {
        return Arrays.asList(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM);
    }
}
