package net.threetag.threecore.ability;

import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ResourceLocationThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class HUDAbility extends Ability {

    public static final ThreeData<ResourceLocation> TEXTURE = new ResourceLocationThreeData("hud").setSyncType(EnumSync.SELF).enableSetting("Texture for the hud");

    public HUDAbility() {
        super(AbilityType.HUD);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(TEXTURE, new ResourceLocation("textures/misc/pumpkinblur.png"));
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
