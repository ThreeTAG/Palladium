package net.threetag.threecore.accessoires;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;

import javax.annotation.Nullable;
import java.util.List;

public class AccessoireSlot {

    private static final List<AccessoireSlot> SLOTS = Lists.newArrayList();

    public static final AccessoireSlot HAT = register("hat").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/hat.png")).setCorrespondingEquipmentSlot(EquipmentSlotType.HEAD);
    public static final AccessoireSlot HEAD = register("head").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/head.png"));
    public static final AccessoireSlot CHEST = register("chest").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/chest.png"));
    public static final AccessoireSlot BACK = register("back").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/back.png"));
    public static final AccessoireSlot MAIN_ARM = register("main_arm").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/main_arm.png"));
    public static final AccessoireSlot OFF_ARM = register("off_arm").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/off_arm.png"));
    public static final AccessoireSlot MAIN_HAND = register("main_hand").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/main_hand.png")).setCorrespondingEquipmentSlot(EquipmentSlotType.MAINHAND);
    public static final AccessoireSlot OFF_HAND = register("off_hand").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/off_hand.png")).setCorrespondingEquipmentSlot(EquipmentSlotType.OFFHAND);
    public static final AccessoireSlot RIGHT_LEG = register("right_leg").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/right_leg.png"));
    public static final AccessoireSlot LEFT_LEG = register("left_leg").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/left_leg.png"));
    public static final AccessoireSlot SPECIAL = register("special").setIcon(new ResourceLocation(ThreeCore.MODID, "textures/gui/accessoire_slots/special.png")).allowMultiple();

    private final String name;
    private boolean multiple = false;
    private EquipmentSlotType equipmentSlot;
    private ResourceLocation icon;

    public AccessoireSlot(String name) {
        this.name = name;
    }

    public AccessoireSlot allowMultiple() {
        this.multiple = true;
        return this;
    }

    public AccessoireSlot setCorrespondingEquipmentSlot(EquipmentSlotType slot) {
        this.equipmentSlot = slot;
        return this;
    }

    public AccessoireSlot setIcon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public EquipmentSlotType getCorrespondingEquipmentSlot() {
        return this.equipmentSlot;
    }

    public String getName() {
        return name;
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("accessoire_slot." + this.name);
    }

    public boolean allowsMultiple() {
        return multiple;
    }

    public ResourceLocation getIcon() {
        return this.icon;
    }

    @OnlyIn(Dist.CLIENT)
    public void setVisibility(PlayerModel model, PlayerEntity player, boolean visible) {
        if (this == HEAD) {
            model.bipedHead.showModel = model.bipedBodyWear.showModel = visible;
        } else if (this == CHEST) {
            model.bipedBody.showModel = model.bipedBodyWear.showModel = visible;
        } else if (this == MAIN_ARM) {
            if (player.getPrimaryHand() == HandSide.RIGHT) {
                model.bipedRightArm.showModel = model.bipedRightArmwear.showModel = visible;
            } else {
                model.bipedLeftArm.showModel = model.bipedLeftArmwear.showModel = visible;
            }
        } else if (this == OFF_ARM) {
            if (player.getPrimaryHand() == HandSide.RIGHT) {
                model.bipedLeftArm.showModel = model.bipedLeftArmwear.showModel = visible;
            } else {
                model.bipedRightArm.showModel = model.bipedRightArmwear.showModel = visible;
            }
        } else if (this == RIGHT_LEG) {
            model.bipedRightLeg.showModel = model.bipedRightLegwear.showModel = visible;
        } else if (this == LEFT_LEG) {
            model.bipedLeftLeg.showModel = model.bipedLeftLegwear.showModel = visible;
        }
    }

    public static AccessoireSlot register(String name) {
        AccessoireSlot slot = new AccessoireSlot(name);
        SLOTS.add(slot);
        return slot;
    }

    @Nullable
    public static AccessoireSlot getSlotByName(String name) {
        for (AccessoireSlot slot : SLOTS) {
            if (slot.getName().equals(name)) {
                return slot;
            }
        }
        return null;
    }

    public static List<AccessoireSlot> getSlots() {
        return ImmutableList.copyOf(SLOTS);
    }
}
