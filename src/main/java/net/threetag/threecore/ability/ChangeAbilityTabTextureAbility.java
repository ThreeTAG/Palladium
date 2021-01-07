package net.threetag.threecore.ability;

import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.StringThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class ChangeAbilityTabTextureAbility extends Ability {

    public static final ThreeData<String> TEXTURE = new StringThreeData("texture").setSyncType(EnumSync.SELF).enableSetting("Texture to set the ability tab to");

    public ChangeAbilityTabTextureAbility() {
        super(AbilityType.CHANGE_ABILITY_TAB_TEXTURE);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(TEXTURE, "minecraft:textures/block/red_wool.png");
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
