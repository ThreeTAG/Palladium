package net.threetag.palladium.entity;

import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.power.ability.*;

import java.util.ArrayList;
import java.util.List;

public enum BodyPart {

    HEAD("head"),
    HEAD_OVERLAY("head_overlay"),
    CHEST("chest"),
    CHEST_OVERLAY("chest_overlay"),
    RIGHT_ARM("right_arm"),
    RIGHT_ARM_OVERLAY("right_arm_overlay"),
    LEFT_ARM("left_arm"),
    LEFT_ARM_OVERLAY("left_arm_overlay"),
    RIGHT_LEG("right_leg"),
    RIGHT_LEG_OVERLAY("right_leg_overlay"),
    LEFT_LEG("left_leg"),
    LEFT_LEG_OVERLAY("left_leg_overlay"),
    CAPE("cape");

    private final String name;

    BodyPart(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Environment(EnvType.CLIENT)
    public void setVisibility(HumanoidModel<?> model, boolean visible) {
        PlayerModel<?> playerModel = model instanceof PlayerModel<?> pl ? pl : null;

        switch (this) {
            case HEAD:
                model.head.visible = visible;
                return;
            case HEAD_OVERLAY:
                model.hat.visible = visible;
                return;
            case CHEST:
                model.body.visible = visible;
                return;
            case CHEST_OVERLAY:
                if (playerModel != null)
                    playerModel.jacket.visible = visible;
                return;
            case RIGHT_ARM:
                model.rightArm.visible = visible;
                return;
            case RIGHT_ARM_OVERLAY:
                if (playerModel != null)
                    playerModel.rightSleeve.visible = visible;
                return;
            case LEFT_ARM:
                model.leftArm.visible = visible;
                return;
            case LEFT_ARM_OVERLAY:
                if (playerModel != null)
                    playerModel.leftSleeve.visible = visible;
                return;
            case RIGHT_LEG:
                model.rightLeg.visible = visible;
                return;
            case RIGHT_LEG_OVERLAY:
                if (playerModel != null)
                    playerModel.rightPants.visible = visible;
                return;
            case LEFT_LEG:
                model.leftLeg.visible = visible;
                return;
            case LEFT_LEG_OVERLAY:
                if (playerModel != null)
                    playerModel.leftPants.visible = visible;
                return;
            case CAPE:
                if (playerModel != null)
                    playerModel.cloak.visible = visible;
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

    public static List<BodyPart> getHiddenBodyParts(LivingEntity entity, boolean isFirstPerson) {
        List<BodyPart> bodyParts = new ArrayList<>();

        for (AbilityEntry bodyPartHide : Ability.getEnabledEntries(Minecraft.getInstance().player, Abilities.HIDE_BODY_PARTS.get())) {
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

            if (layer instanceof PackRenderLayer renderLayer) {
                for (BodyPart part : renderLayer.getHiddenBodyParts()) {
                    if (!bodyParts.contains(part)) {
                        bodyParts.add(part);
                    }
                }
            }
        }

        return bodyParts;
    }

}
