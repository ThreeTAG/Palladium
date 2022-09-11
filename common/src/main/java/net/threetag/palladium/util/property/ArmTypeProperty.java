package net.threetag.palladium.util.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

import java.util.Locale;

public class ArmTypeProperty extends EnumPalladiumProperty<ArmTypeProperty.ArmType> {

    public ArmTypeProperty(String key) {
        super(key);
    }

    @Override
    public ArmType[] getValues() {
        return ArmType.values();
    }

    @Override
    public String getNameFromEnum(ArmType value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

    public enum ArmType {

        MAIN_ARM,
        OFF_ARM,
        RIGHT_ARM,
        LEFT_ARM,
        BOTH;

        @Environment(EnvType.CLIENT)
        public ModelPart[] getModelPart(LivingEntity entity, HumanoidModel<?> model) {
            if (this == MAIN_ARM) {
                if (entity.getMainArm() == HumanoidArm.RIGHT) {
                    return new ModelPart[]{model.rightArm};
                } else {
                    return new ModelPart[]{model.leftArm};
                }
            } else if (this == OFF_ARM) {
                if (entity.getMainArm() == HumanoidArm.RIGHT) {
                    return new ModelPart[]{model.leftArm};
                } else {
                    return new ModelPart[]{model.rightArm};
                }
            } else if (this == RIGHT_ARM) {
                return new ModelPart[]{model.rightArm};
            } else if (this == LEFT_ARM) {
                return new ModelPart[]{model.leftArm};
            } else {
                return new ModelPart[]{model.rightArm, model.leftArm};
            }
        }

        public boolean isRight(LivingEntity entity) {
            if (this == RIGHT_ARM || this == BOTH) {
                return true;
            } else if (this == MAIN_ARM) {
                return entity.getMainArm() == HumanoidArm.RIGHT;
            } else if (this == OFF_ARM) {
                return entity.getMainArm() != HumanoidArm.RIGHT;
            } else {
                return false;
            }
        }

        public boolean isLeft(LivingEntity entity) {
            if (this == LEFT_ARM || this == BOTH) {
                return true;
            } else if (this == MAIN_ARM) {
                return entity.getMainArm() == HumanoidArm.LEFT;
            } else if (this == OFF_ARM) {
                return entity.getMainArm() != HumanoidArm.LEFT;
            } else {
                return false;
            }
        }

    }

}
