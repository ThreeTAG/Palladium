package net.threetag.palladium.accessory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.BodyPart;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AccessorySlot {

    private static final List<AccessorySlot> SLOTS = Lists.newArrayList();

    public static final AccessorySlot HAT = register("hat").setIcon(Palladium.id("textures/gui/accessory_slots/hat.png")).setCorrespondingEquipmentSlot(EquipmentSlot.HEAD);
    public static final AccessorySlot HEAD = register("head").setIcon(Palladium.id("textures/gui/accessory_slots/head.png"));
    public static final AccessorySlot FACE = register("face").setIcon(Palladium.id("textures/gui/accessory_slots/face.png"));
    public static final AccessorySlot CHEST = register("chest").setIcon(Palladium.id("textures/gui/accessory_slots/chest.png"));
    public static final AccessorySlot BACK = register("back").setIcon(Palladium.id("textures/gui/accessory_slots/back.png"));
    public static final AccessorySlot MAIN_ARM = register("main_arm").setIcon(Palladium.id("textures/gui/accessory_slots/main_arm.png"));
    public static final AccessorySlot OFF_ARM = register("off_arm").setIcon(Palladium.id("textures/gui/accessory_slots/off_arm.png"));
    public static final AccessorySlot MAIN_HAND = register("main_hand").setIcon(Palladium.id("textures/gui/accessory_slots/main_hand.png")).setCorrespondingEquipmentSlot(EquipmentSlot.MAINHAND);
    public static final AccessorySlot OFF_HAND = register("off_hand").setIcon(Palladium.id("textures/gui/accessory_slots/off_hand.png")).setCorrespondingEquipmentSlot(EquipmentSlot.OFFHAND);
    public static final AccessorySlot RIGHT_LEG = register("right_leg").setIcon(Palladium.id("textures/gui/accessory_slots/right_leg.png"));
    public static final AccessorySlot LEFT_LEG = register("left_leg").setIcon(Palladium.id("textures/gui/accessory_slots/left_leg.png"));
    public static final AccessorySlot SPECIAL = register("special").setIcon(Palladium.id("textures/gui/accessory_slots/special.png")).allowMultiple();

    private final String name;
    private boolean multiple = false;
    private EquipmentSlot equipmentSlot;
    private ResourceLocation icon;

    public AccessorySlot(String name) {
        this.name = name;
    }

    public AccessorySlot allowMultiple() {
        this.multiple = true;
        return this;
    }

    public AccessorySlot setCorrespondingEquipmentSlot(EquipmentSlot slot) {
        this.equipmentSlot = slot;
        return this;
    }

    public AccessorySlot setIcon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public EquipmentSlot getCorrespondingEquipmentSlot() {
        return this.equipmentSlot;
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return Component.translatable("accessory_slot." + this.name);
    }

    public boolean allowsMultiple() {
        return multiple;
    }

    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Environment(EnvType.CLIENT)
    public boolean wasHidden(Player player, boolean isFirstPerson) {
        return BodyPart.getHiddenBodyParts(player, isFirstPerson, false).stream().anyMatch(part -> this.getHiddenBodyParts(player).contains(part));
    }

    public Collection<BodyPart> getHiddenBodyParts(Player player) {
        if (this == HEAD) {
            return Arrays.asList(BodyPart.HEAD, BodyPart.HEAD_OVERLAY);
        } else if (this == CHEST) {
            return Arrays.asList(BodyPart.CHEST, BodyPart.CHEST_OVERLAY);
        } else if (this == MAIN_ARM) {
            return player.getMainArm() == HumanoidArm.RIGHT ? Arrays.asList(BodyPart.RIGHT_ARM, BodyPart.RIGHT_ARM_OVERLAY) : Arrays.asList(BodyPart.LEFT_ARM, BodyPart.LEFT_ARM_OVERLAY);
        } else if (this == OFF_ARM) {
            return player.getMainArm() != HumanoidArm.RIGHT ? Arrays.asList(BodyPart.RIGHT_ARM, BodyPart.RIGHT_ARM_OVERLAY) : Arrays.asList(BodyPart.LEFT_ARM, BodyPart.LEFT_ARM_OVERLAY);
        } else if (this == RIGHT_LEG) {
            return Arrays.asList(BodyPart.RIGHT_LEG, BodyPart.RIGHT_LEG_OVERLAY);
        } else if (this == LEFT_LEG) {
            return Arrays.asList(BodyPart.LEFT_LEG, BodyPart.LEFT_LEG_OVERLAY);
        }
        return Collections.emptyList();
    }

    public static AccessorySlot register(String name) {
        AccessorySlot slot = new AccessorySlot(name);
        SLOTS.add(slot);
        return slot;
    }

    @Nullable
    public static AccessorySlot getSlotByName(String name) {
        for (AccessorySlot slot : SLOTS) {
            if (slot.getName().equals(name)) {
                return slot;
            }
        }
        return null;
    }

    public static List<AccessorySlot> getSlots() {
        return ImmutableList.copyOf(SLOTS);
    }
}
