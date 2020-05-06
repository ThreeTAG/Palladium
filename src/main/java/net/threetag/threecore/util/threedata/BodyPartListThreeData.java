package net.threetag.threecore.util.threedata;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class BodyPartListThreeData extends ThreeData<List<BodyPartListThreeData.BodyPart>> {

    public BodyPartListThreeData(String key) {
        super(key);
    }

    @Override
    public List<BodyPart> parseValue(JsonObject jsonObject, List<BodyPart> defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        List<BodyPart> list = Lists.newArrayList();
        JsonElement jsonElement = jsonObject.get(this.jsonKey);

        if (jsonElement.isJsonPrimitive()) {
            BodyPart bodyPart = BodyPart.byName(jsonElement.getAsString());
            if (bodyPart == null) {
                throw new JsonSyntaxException("Body part " + jsonElement.getAsString() + " does not exist!");
            } else {
                list.add(bodyPart);
            }
        } else if (jsonElement.isJsonArray()) {
            for (int i = 0; i < jsonElement.getAsJsonArray().size(); i++) {
                BodyPart bodyPart = BodyPart.byName(jsonElement.getAsJsonArray().get(i).getAsString());
                if (bodyPart == null) {
                    throw new JsonSyntaxException("Body part " + jsonElement.getAsJsonArray().get(i).getAsString() + " does not exist!");
                } else {
                    list.add(bodyPart);
                }
            }
        } else {
            throw new JsonSyntaxException("Body part setting must be either a string or an array of strings!");
        }

        return list;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, List<BodyPart> value) {
        ListNBT list = new ListNBT();
        for (BodyPart part : value) {
            list.add(IntNBT.valueOf(part.ordinal()));
        }
        nbt.put(this.key, list);
    }

    @Override
    public List<BodyPart> readFromNBT(CompoundNBT nbt, List<BodyPart> defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        List<BodyPart> list = Lists.newArrayList();
        ListNBT listNBT = nbt.getList(this.key, Constants.NBT.TAG_INT);
        for (int i = 0; i < listNBT.size(); i++) {
            BodyPart bodyPart = BodyPart.values()[listNBT.getInt(i)];
            if (bodyPart != null) {
                list.add(bodyPart);
            }
        }
        return list;
    }

    @Override
    public JsonElement serializeJson(List<BodyPart> value) {
        if (value.size() == 1) {
            return new JsonPrimitive(value.get(0).name);
        } else {
            JsonArray jsonArray = new JsonArray();
            for (BodyPart bodyPart : value) {
                jsonArray.add(bodyPart.name);
            }
            return jsonArray;
        }
    }

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
        LEFT_LEG_OVERLAY("left_leg_overlay");

        private String name;

        BodyPart(String name) {
            this.name = name;
        }

        public void setVisibility(PlayerModel model, boolean visible) {
            switch (this) {
                case HEAD:
                    model.bipedHead.showModel = visible;
                    return;
                case HEAD_OVERLAY:
                    model.bipedHeadwear.showModel = visible;
                    return;
                case CHEST:
                    model.bipedBody.showModel = visible;
                    return;
                case CHEST_OVERLAY:
                    model.bipedBodyWear.showModel = visible;
                    return;
                case RIGHT_ARM:
                    model.bipedRightArm.showModel = visible;
                    return;
                case RIGHT_ARM_OVERLAY:
                    model.bipedRightArmwear.showModel = visible;
                    return;
                case LEFT_ARM:
                    model.bipedLeftArm.showModel = visible;
                    return;
                case LEFT_ARM_OVERLAY:
                    model.bipedLeftArmwear.showModel = visible;
                    return;
                case RIGHT_LEG:
                    model.bipedRightLeg.showModel = visible;
                    return;
                case RIGHT_LEG_OVERLAY:
                    model.bipedRightLegwear.showModel = visible;
                    return;
                case LEFT_LEG:
                    model.bipedLeftLeg.showModel = visible;
                    return;
                default:
                    model.bipedLeftLegwear.showModel = visible;
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

    }

}
