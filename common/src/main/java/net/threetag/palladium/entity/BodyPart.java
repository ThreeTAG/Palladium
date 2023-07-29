package net.threetag.palladium.entity;

import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.client.renderer.item.armor.ArmorRendererData;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.item.ArmorWithRenderer;
import net.threetag.palladium.power.ability.*;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BodyPart {

    HEAD("head", false),
    HEAD_OVERLAY("head_overlay", true),
    CHEST("chest", false),
    CHEST_OVERLAY("chest_overlay", true),
    RIGHT_ARM("right_arm", false),
    RIGHT_ARM_OVERLAY("right_arm_overlay", true),
    LEFT_ARM("left_arm", false),
    LEFT_ARM_OVERLAY("left_arm_overlay", true),
    RIGHT_LEG("right_leg", false),
    RIGHT_LEG_OVERLAY("right_leg_overlay", true),
    LEFT_LEG("left_leg", false),
    LEFT_LEG_OVERLAY("left_leg_overlay", true),
    CAPE("cape", false);

    private final String name;
    private final boolean overlay;
    public static final List<Item> HIDES_LAYER = new ArrayList<>();

    BodyPart(String name, boolean overlay) {
        this.name = name;
        this.overlay = overlay;
    }

    public String getName() {
        return this.name;
    }

    public boolean isOverlay() {
        return this.overlay;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public ModelPart getModelPart(HumanoidModel<?> model) {
        PlayerModel<?> playerModel = model instanceof PlayerModel<?> pl ? pl : null;

        return switch (this) {
            case HEAD -> model.head;
            case HEAD_OVERLAY -> model.hat;
            case CHEST -> model.body;
            case CHEST_OVERLAY -> playerModel != null ? playerModel.jacket : null;
            case RIGHT_ARM -> model.rightArm;
            case RIGHT_ARM_OVERLAY -> playerModel != null ? playerModel.rightSleeve : null;
            case LEFT_ARM -> model.leftArm;
            case LEFT_ARM_OVERLAY -> playerModel != null ? playerModel.leftSleeve : null;
            case RIGHT_LEG -> model.rightLeg;
            case RIGHT_LEG_OVERLAY -> playerModel != null ? playerModel.rightPants : null;
            case LEFT_LEG -> model.leftLeg;
            case LEFT_LEG_OVERLAY -> playerModel != null ? playerModel.leftPants : null;
            case CAPE -> playerModel != null ? playerModel.cloak : null;
        };
    }

    @Environment(EnvType.CLIENT)
    public void setVisibility(HumanoidModel<?> model, boolean visible) {
        ModelPart part = getModelPart(model);

        if (part != null) {
            part.visible = visible;
        }
    }

    public static BodyPart fromJson(String name) {
        var part = byName(name);

        if (part != null) {
            return part;
        } else {
            throw new JsonParseException("Unknown body part '" + name + "'");
        }
    }

    public static BodyPart byName(String name) {
        for (BodyPart bodyPart : values()) {
            if (name.equalsIgnoreCase(bodyPart.name)) {
                return bodyPart;
            }
        }

        return null;
    }

    @Environment(EnvType.CLIENT)
    public static void hideHiddenOrRemovedParts(HumanoidModel<?> model, LivingEntity entity, ModifiedBodyPartResult result) {
        for (BodyPart part : values()) {
            if (result.isHiddenOrRemoved(part)) {
                part.setVisibility(model, false);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void hideRemovedParts(HumanoidModel<?> model, LivingEntity entity, ModifiedBodyPartResult result) {
        for (BodyPart part : values()) {
            if (result.isRemoved(part)) {
                part.setVisibility(model, false);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void resetBodyParts(LivingEntity entity, HumanoidModel<?> model) {
        if (entity instanceof Player player) {
            if (player.isSpectator()) {
                model.setAllVisible(false);
                model.head.visible = true;
                model.hat.visible = true;
            } else {
                model.setAllVisible(true);
                model.hat.visible = player.isModelPartShown(PlayerModelPart.HAT);

                if (model instanceof PlayerModel<?> playerModel) {
                    playerModel.jacket.visible = player.isModelPartShown(PlayerModelPart.JACKET);
                    playerModel.leftPants.visible = player.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
                    playerModel.rightPants.visible = player.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
                    playerModel.leftSleeve.visible = player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
                    playerModel.rightSleeve.visible = player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
                    playerModel.cloak.visible = player.isModelPartShown(PlayerModelPart.CAPE);
                }
            }
        } else {
            model.setAllVisible(true);
        }
    }

    @Environment(EnvType.CLIENT)
    public static ModifiedBodyPartResult getModifiedBodyParts(LivingEntity entity, boolean isFirstPerson) {
        return getModifiedBodyParts(entity, isFirstPerson, true);
    }

    @Environment(EnvType.CLIENT)
    public static ModifiedBodyPartResult getModifiedBodyParts(LivingEntity entity, boolean isFirstPerson, boolean includeAccessories) {
        ModifiedBodyPartResult result = new ModifiedBodyPartResult();

        if (entity instanceof Player player) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    var stack = player.getItemBySlot(slot);

                    if (HIDES_LAYER.contains(stack.getItem()) || (stack.getItem() instanceof ArmorWithRenderer armorWithRenderer
                            && armorWithRenderer.getCachedArmorRenderer() instanceof ArmorRendererData renderer
                            && renderer.hidesSecondPlayerLayer(DataContext.forArmorInSlot(player, slot)))) {
                        if (slot == EquipmentSlot.HEAD) {
                            result.remove(BodyPart.HEAD_OVERLAY);
                        } else if (slot == EquipmentSlot.CHEST) {
                            result.remove(BodyPart.CHEST_OVERLAY);
                            result.remove(BodyPart.RIGHT_ARM_OVERLAY);
                            result.remove(BodyPart.LEFT_ARM_OVERLAY);
                        } else {
                            result.remove(BodyPart.RIGHT_LEG_OVERLAY);
                            result.remove(BodyPart.LEFT_LEG_OVERLAY);
                        }
                    }
                }
            }

            if (includeAccessories) {
                Accessory.getPlayerData(player).ifPresent(data -> data.getSlots().forEach((slot, accessories) -> {
                    if (!accessories.isEmpty()) {
                        for (BodyPart part : slot.getHiddenBodyParts(player)) {
                            result.hide(part);
                        }
                    }
                }));
            }
        }

        for (AbilityEntry bodyPartHide : AbilityUtil.getEnabledEntries(entity, Abilities.HIDE_BODY_PART.get())) {
            if (isFirstPerson ? bodyPartHide.getProperty(HideBodyPartAbility.AFFECTS_FIRST_PERSON) : true) {
                for (BodyPart part : bodyPartHide.getProperty(HideBodyPartAbility.BODY_PARTS)) {
                    result.hide(part);
                }
            }
        }

        for (AbilityEntry bodyPartHide : AbilityUtil.getEnabledEntries(entity, Abilities.REMOVE_BODY_PART.get())) {
            if (isFirstPerson ? bodyPartHide.getProperty(RemoveBodyPartAbility.AFFECTS_FIRST_PERSON) : true) {
                for (BodyPart part : bodyPartHide.getProperty(RemoveBodyPartAbility.BODY_PARTS)) {
                    result.remove(part);
                }
            }
        }

        PackRenderLayerManager.forEachLayer(entity, (context, layer) -> {
            for (BodyPart part : layer.getHiddenBodyParts(entity)) {
                result.hide(part);
            }
        });

        return result;
    }

    public static class ModifiedBodyPartResult {

        private final Map<BodyPart, Integer> states = new HashMap<>();

        public ModifiedBodyPartResult hide(BodyPart part) {
            return this.set(part, false);
        }

        public ModifiedBodyPartResult remove(BodyPart part) {
            return this.set(part, true);
        }

        public ModifiedBodyPartResult set(BodyPart part, boolean remove) {
            int mod = remove ? 2 : 1;
            if (!this.states.containsKey(part)) {
                this.states.put(part, mod);
            } else {
                this.states.put(part, Math.max(this.states.get(part), mod));
            }

            return this;
        }

        public boolean isHiddenOrRemoved(BodyPart bodyPart) {
            return this.states.containsKey(bodyPart);
        }

        public boolean isHidden(BodyPart bodyPart) {
            return this.states.containsKey(bodyPart) && this.states.get(bodyPart) == 1;
        }

        public boolean isRemoved(BodyPart bodyPart) {
            return this.states.containsKey(bodyPart) && this.states.get(bodyPart) == 2;
        }

    }

}
