package net.threetag.palladium.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;

public enum BodyPart implements StringRepresentable {

    HEAD(0, "head", false),
    HEAD_OVERLAY(1, "head_overlay", true),
    CHEST(2, "chest", false),
    CHEST_OVERLAY(3, "chest_overlay", true),
    RIGHT_ARM(4, "right_arm", false),
    RIGHT_ARM_OVERLAY(5, "right_arm_overlay", true),
    LEFT_ARM(6, "left_arm", false),
    LEFT_ARM_OVERLAY(7, "left_arm_overlay", true),
    RIGHT_LEG(8, "right_leg", false),
    RIGHT_LEG_OVERLAY(9, "right_leg_overlay", true),
    LEFT_LEG(10, "left_leg", false),
    LEFT_LEG_OVERLAY(11, "left_leg_overlay", true),
    CAPE(12, "cape", false);

    public static final IntFunction<BodyPart> BY_ID = ByIdMap.continuous(BodyPart::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<BodyPart> CODEC = StringRepresentable.fromEnum(BodyPart::values);
    public static final StreamCodec<ByteBuf, BodyPart> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, BodyPart::id);

    private final int id;
    private final String name;
    private final boolean overlay;

    BodyPart(int id, String name, boolean overlay) {
        this.id = id;
        this.name = name;
        this.overlay = overlay;
    }

    public int id() {
        return id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean isOverlay() {
        return this.overlay;
    }

    @Nullable
    public BodyPart getOverlayPart() {
        return switch (this) {
            case HEAD -> HEAD_OVERLAY;
            case CHEST -> CHEST_OVERLAY;
            case RIGHT_ARM -> RIGHT_ARM_OVERLAY;
            case LEFT_ARM -> LEFT_ARM_OVERLAY;
            case RIGHT_LEG -> RIGHT_LEG_OVERLAY;
            case LEFT_LEG -> LEFT_LEG_OVERLAY;
            default -> null;
        };
    }

    public static BodyPart byName(String name) {
        for (BodyPart bodyPart : values()) {
            if (name.equalsIgnoreCase(bodyPart.name)) {
                return bodyPart;
            }
        }

        return null;
    }

    public static Set<BodyPart> getHiddenBodyParts(LivingEntity entity) {
        Set<BodyPart> parts = new HashSet<>();

        // Customizations
        for (Holder<Customization> customization : EntityCustomizationHandler.get(entity).getSelected()) {
            parts.addAll(customization.value().getHiddenBodyParts());
        }

        return parts;
    }

    public static Set<BodyPart> getRemovedBodyParts(LivingEntity entity) {
        return Collections.emptySet();
    }

}
