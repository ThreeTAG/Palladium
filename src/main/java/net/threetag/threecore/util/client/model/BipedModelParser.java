package net.threetag.threecore.util.client.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;

import java.util.List;

public class BipedModelParser extends EntityModelParser {

    @Override
    public EntityModel apply(JsonObject jsonObject) {
        ParsedBipedModel model = new ParsedBipedModel(JSONUtils.getFloat(jsonObject, "scale", 0F), JSONUtils.getBoolean(jsonObject, "small_arms", false));
        model.textureWidth = JSONUtils.getInt(jsonObject, "texture_width", 64);
        model.textureHeight = JSONUtils.getInt(jsonObject, "texture_height", 64);

        if (JSONUtils.hasField(jsonObject, "cubes")) {
            JsonArray cubes = JSONUtils.getJsonArray(jsonObject, "cubes");

            for (int i = 0; i < cubes.size(); i++) {
                JsonObject cubeJson = cubes.get(i).getAsJsonObject();
                RendererModel parent = getPart(JSONUtils.getString(cubeJson, "parent", ""), model);

                if (parent != null)
                    parent.addChild(parseRendererModel(cubeJson, model));
                else
                    model.addCube(parseRendererModel(cubeJson, model));
            }
        }

        return model;
    }

    public RendererModel getPart(String name, PlayerModel model) {
        if (name.equalsIgnoreCase("head"))
            return model.bipedHead;
        else if (name.equalsIgnoreCase("head_overlay"))
            return model.bipedHeadwear;
        else if (name.equalsIgnoreCase("chest"))
            return model.bipedBody;
        else if (name.equalsIgnoreCase("chest_overlay"))
            return model.bipedBodyWear;
        else if (name.equalsIgnoreCase("right_arm"))
            return model.bipedRightArm;
        else if (name.equalsIgnoreCase("right_arm_overlay"))
            return model.bipedRightArmwear;
        else if (name.equalsIgnoreCase("left_arm"))
            return model.bipedLeftArm;
        else if (name.equalsIgnoreCase("left_arm_overlay"))
            return model.bipedLeftArmwear;
        else if (name.equalsIgnoreCase("right_leg"))
            return model.bipedRightLeg;
        else if (name.equalsIgnoreCase("right_leg_overlay"))
            return model.bipedRightLegwear;
        else if (name.equalsIgnoreCase("left_leg"))
            return model.bipedLeftLeg;
        else if (name.equalsIgnoreCase("left_leg_overlay"))
            return model.bipedLeftLegwear;
        return null;
    }

    public static class ParsedBipedModel extends PlayerModel {

        public List<RendererModel> cubes = Lists.newLinkedList();

        public ParsedBipedModel(float modelSize, boolean smallArmsIn) {
            super(modelSize, smallArmsIn);
        }

        public ParsedBipedModel addCube(RendererModel rendererModel) {
            this.cubes.add(rendererModel);
            return this;
        }

        @Override
        public void render(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            for (RendererModel cube : this.cubes) {
                cube.render(scale);
            }
        }
    }

}
