package net.threetag.threecore.ability;

import net.minecraft.util.DamageSource;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.StringArrayThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class DamageImmunityAbility extends Ability {

    public static final ThreeData<String[]> DAMAGE_SOURCES = new StringArrayThreeData("damage_sources").enableSetting("Determines which damage sources have no effect on the entity").setSyncType(EnumSync.NONE);

    public DamageImmunityAbility() {
        super(AbilityType.DAMAGE_IMMUNITY);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(DAMAGE_SOURCES, new String[]{"lightningBolt", "cactus"});
    }

    public boolean isImmuneAgainst(DamageSource source) {
        for (String s : this.get(DAMAGE_SOURCES)) {
            if (s.equals(source.getDamageType())) {
                return true;
            }
        }
        return false;
    }
}
