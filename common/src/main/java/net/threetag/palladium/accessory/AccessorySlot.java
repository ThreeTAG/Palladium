package net.threetag.palladium.accessory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;

import javax.annotation.Nullable;
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
        return new TranslatableComponent("accessory_slot." + this.name);
    }

    public boolean allowsMultiple() {
        return multiple;
    }

    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Environment(EnvType.CLIENT)
    public void setVisibility(HumanoidModel<?> model, Player player, boolean visible) {
        if (this == HEAD) {
            model.head.visible = model.hat.visible = visible;
            if (model instanceof PlayerModel<?> playerModel) {
                playerModel.hat.visible = visible;
            }
        } else if (this == CHEST) {
            model.body.visible = visible;
            if (model instanceof PlayerModel<?> playerModel) {
                playerModel.jacket.visible = visible;
            }
        } else if (this == MAIN_ARM) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                model.rightArm.visible = visible;
                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.rightSleeve.visible = visible;
                }
            } else {
                model.leftArm.visible = visible;
                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.leftSleeve.visible = visible;
                }
            }
        } else if (this == OFF_ARM) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                model.leftArm.visible = visible;
                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.leftSleeve.visible = visible;
                }
            } else {
                model.rightArm.visible = visible;
                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.rightSleeve.visible = visible;
                }
            }
        } else if (this == RIGHT_LEG) {
            model.rightLeg.visible = visible;
            if (model instanceof PlayerModel<?> playerModel) {
                playerModel.rightPants.visible = visible;
            }
        } else if (this == LEFT_LEG) {
            model.leftLeg.visible = visible;
            if (model instanceof PlayerModel<?> playerModel) {
                playerModel.leftPants.visible = visible;
            }
        }
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
