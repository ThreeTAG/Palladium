package net.threetag.palladium.entity;

import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.power.ability.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public static void hideParts(HumanoidModel<?> model, LivingEntity entity) {
        for (BodyPart part : getHiddenBodyParts(entity, false)) {
            part.setVisibility(model, false);
        }
    }

    @Environment(EnvType.CLIENT)
    public static List<BodyPart> getHiddenBodyParts(LivingEntity entity, boolean isFirstPerson) {
        return getHiddenBodyParts(entity, isFirstPerson, true);
    }

    @Environment(EnvType.CLIENT)
    public static List<BodyPart> getHiddenBodyParts(LivingEntity entity, boolean isFirstPerson, boolean includeAccessories) {
        List<BodyPart> bodyParts = new ArrayList<>();

        if (includeAccessories && entity instanceof Player player) {
            Accessory.getPlayerData(player).ifPresent(data -> data.getSlots().forEach((slot, accessories) -> {
                if (!accessories.isEmpty()) {
                    for (BodyPart part : slot.getHiddenBodyParts(player)) {
                        if (!bodyParts.contains(part)) {
                            bodyParts.add(part);
                        }
                    }
                }
            }));
        }

        for (AbilityEntry bodyPartHide : Ability.getEnabledEntries(entity, Abilities.HIDE_BODY_PARTS.get())) {
            if (isFirstPerson ? bodyPartHide.getProperty(HideBodyPartsAbility.AFFECTS_FIRST_PERSON) : true) {
                for (BodyPart part : bodyPartHide.getProperty(HideBodyPartsAbility.BODY_PARTS)) {
                    if (!bodyParts.contains(part)) {
                        bodyParts.add(part);
                    }
                }
            }
        }

        for (AbilityEntry entry : Ability.getEnabledEntries(entity, Abilities.RENDER_LAYER.get())) {
            IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(entry.getProperty(RenderLayerAbility.RENDER_LAYER));

            for (BodyPart part : layer.getHiddenBodyParts(entity)) {
                if (!bodyParts.contains(part)) {
                    bodyParts.add(part);
                }
            }
        }

        return bodyParts;
    }

}
