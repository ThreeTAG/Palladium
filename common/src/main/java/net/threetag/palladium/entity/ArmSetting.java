package net.threetag.palladium.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum ArmSetting implements StringRepresentable {

    NONE(0, "none"),
    MAIN_ARM(1, "main_arm"),
    OFF_ARM(2, "off_arm"),
    RIGHT_ARM(3, "right_arm"),
    LEFT_ARM(4, "left_arm"),
    BOTH(5, "both");

    public static final IntFunction<ArmSetting> BY_ID = ByIdMap.continuous(ArmSetting::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<ArmSetting> CODEC = StringRepresentable.fromEnum(ArmSetting::values);
    public static final StreamCodec<ByteBuf, ArmSetting> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ArmSetting::id);

    private final int id;
    private final String name;

    ArmSetting(int id, String name) {
        this.id = id;
        this.name = name;
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


    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }
}
