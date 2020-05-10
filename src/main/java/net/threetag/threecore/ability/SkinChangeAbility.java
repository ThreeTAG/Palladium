package net.threetag.threecore.ability;

import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ResourceLocationThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class SkinChangeAbility extends Ability {

    public static final ThreeData<ResourceLocation> TEXTURE = new ResourceLocationThreeData("texture").enableSetting("Texture for the new player skin");
    public static final ThreeData<Integer> PRIORITY = new IntegerThreeData("priority").enableSetting("Priority for the skin (in case multiple skin changes are applied, the one with the highest priority will be used)");

    public SkinChangeAbility() {
        super(AbilityType.SKIN_CHANGE);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(TEXTURE, new ResourceLocation("textures/entity/zombie/drowned.png"));
        this.register(PRIORITY, 50);
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
