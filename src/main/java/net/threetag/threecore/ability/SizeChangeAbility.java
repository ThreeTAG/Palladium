package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.sizechanging.SizeChangeType;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.icon.TexturedIcon;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.SizeChangeTypeThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class SizeChangeAbility extends Ability {

    public static final ThreeData<Float> SIZE = new FloatThreeData("size").setSyncType(EnumSync.NONE).enableSetting("size", "Determines the size for the size change");
    public static final ThreeData<SizeChangeType> SIZE_CHANGE_TYPE = new SizeChangeTypeThreeData("size_change_type").setSyncType(EnumSync.NONE).enableSetting("size_change_type", "Determines the size change type for the size change");

    public boolean prevEnabled;

    public SizeChangeAbility() {
        super(AbilityType.SIZE_CHANGE);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 160, 0, 16, 16));
        this.dataManager.register(SIZE, 0.1F);
        this.dataManager.register(SIZE_CHANGE_TYPE, SizeChangeType.DEFAULT_TYPE);
    }

    @Override
    public void firstTick(LivingEntity entity) {
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            if (this.getConditionManager().isEnabled()) {
                sizeChanging.startSizeChange(this.dataManager.get(SIZE_CHANGE_TYPE), this.dataManager.get(SIZE));
            } else {
                sizeChanging.startSizeChange(this.dataManager.get(SIZE_CHANGE_TYPE), 1F);
            }
        });
    }

    @Override
    public void action(LivingEntity entity) {
        if (prevEnabled != this.getConditionManager().isEnabled()) {
            entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                if (this.getConditionManager().isEnabled()) {
                    sizeChanging.startSizeChange(this.dataManager.get(SIZE_CHANGE_TYPE), this.dataManager.get(SIZE));
                } else {
                    sizeChanging.startSizeChange(this.dataManager.get(SIZE_CHANGE_TYPE), 1F);
                }
            });
        }

        prevEnabled = this.getConditionManager().isEnabled();
    }
}
