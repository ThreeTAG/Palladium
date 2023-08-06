package net.threetag.palladium.accessory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladiumcore.util.Platform;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    private final ResourceLocation name;
    private boolean multiple = false;
    private EquipmentSlot equipmentSlot;
    private ResourceLocation icon;
    private List<Condition> visible = new ArrayList<>();

    private AccessorySlot(ResourceLocation name) {
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

    public AccessorySlot addVisibilityCondition(Condition condition) {
        if (Platform.isClient()) {
            this.visible.add(condition);
        }
        return this;
    }

    public EquipmentSlot getCorrespondingEquipmentSlot() {
        return this.equipmentSlot;
    }

    public ResourceLocation getName() {
        return name;
    }

    public String getTranslationKey() {
        return Util.makeDescriptionId("accessory_slot", this.name);
    }

    public Component getDisplayName() {
        return Component.translatable(this.getTranslationKey());
    }

    public boolean allowsMultiple() {
        return multiple;
    }

    public ResourceLocation getIcon() {
        return this.icon;
    }

    public boolean isVisible(DataContext context) {
        return ConditionSerializer.checkConditions(this.visible, context);
    }

    @Environment(EnvType.CLIENT)
    public boolean wasHidden(Player player, boolean isFirstPerson) {
        var result = BodyPart.getModifiedBodyParts(player, isFirstPerson, false);
        return this.getHiddenBodyParts(player).stream().anyMatch(result::isHiddenOrRemoved);
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

    public static AccessorySlot register(ResourceLocation name) {
        AccessorySlot slot = new AccessorySlot(name);
        SLOTS.add(slot);
        return slot;
    }

    private static AccessorySlot register(String name) {
        AccessorySlot slot = new AccessorySlot(Palladium.id(name));
        SLOTS.add(slot);
        return slot;
    }

    @Nullable
    public static AccessorySlot getSlotByName(ResourceLocation name) {
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
