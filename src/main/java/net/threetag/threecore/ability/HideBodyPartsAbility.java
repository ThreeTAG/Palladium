package net.threetag.threecore.ability;

import net.threetag.threecore.util.threedata.BodyPartListThreeData;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.Arrays;
import java.util.List;

public class HideBodyPartsAbility extends Ability {

    public static final ThreeData<List<BodyPartListThreeData.BodyPart>> BODY_PARTS = new BodyPartListThreeData("body_parts").enableSetting("Determines which body parts should vanish. Available parts: " + BodyPartListThreeData.getBodyPartList());
    public static final ThreeData<Boolean> AFFECTS_FIRST_PERSON = new BooleanThreeData("affects_first_person").enableSetting("Determines if your first person arm should disappear aswell (if it's disabled)");

    public HideBodyPartsAbility() {
        super(AbilityType.HIDE_BODY_PARTS);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(BODY_PARTS, Arrays.asList(BodyPartListThreeData.BodyPart.LEFT_ARM, BodyPartListThreeData.BodyPart.LEFT_ARM_OVERLAY));
        this.register(AFFECTS_FIRST_PERSON, true);
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
