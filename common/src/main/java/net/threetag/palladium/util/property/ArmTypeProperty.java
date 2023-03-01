package net.threetag.palladium.util.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
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

        NONE,
        MAIN_ARM,
        OFF_ARM,
        RIGHT_ARM,
        LEFT_ARM,
        BOTH;

        @Environment(EnvType.CLIENT)
        public ModelPart[] getModelPart(LivingEntity entity, HumanoidModel<?> model) {
            if (this == NONE) {
                return new ModelPart[0];
            } else if (this == MAIN_ARM) {
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

        public InteractionHand[] getHand(LivingEntity entity) {
            if (this.isNone()) {
                return new InteractionHand[0];
            } else if (this.isBoth()) {
                return InteractionHand.values();
            } else if (this == MAIN_ARM) {
                return new InteractionHand[]{InteractionHand.MAIN_HAND};
            } else if (this == OFF_ARM) {
                return new InteractionHand[]{InteractionHand.OFF_HAND};
            } else if (this == RIGHT_ARM) {
                return new InteractionHand[]{entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND};
            } else {
                return new InteractionHand[]{entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND};
            }
        }

        public boolean isNone() {
            return this == NONE;
        }

        public boolean isBoth() {
            return this == BOTH;
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
