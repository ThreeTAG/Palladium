package net.threetag.threecore.util.client.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;

import java.util.List;

public class BipedModelParser extends EntityModelParser {

    @Override
    public EntityModel apply(JsonObject jsonObject) {
        ParsedBipedModel model = new ParsedBipedModel(JSONUtils.getFloat(jsonObject, "scale", 0F), JSONUtils.getBoolean(jsonObject, "small_arms", false), JSONUtils.getInt(jsonObject, "texture_width", 64), JSONUtils.getInt(jsonObject, "texture_height", 64));

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

    public RendererModel getPart(String name, ParsedBipedModel model) {
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

    public static class ParsedBipedModel<T extends LivingEntity> extends BipedModel<T> {

        public List<RendererModel> cubes = Lists.newLinkedList();
        public final RendererModel bipedLeftArmwear;
        public final RendererModel bipedRightArmwear;
        public final RendererModel bipedLeftLegwear;
        public final RendererModel bipedRightLegwear;
        public final RendererModel bipedBodyWear;
        private final boolean smallArms;

        public ParsedBipedModel(float modelSize, boolean smallArmsIn, int textureWidth, int textureHeight) {
            super(modelSize, 0.0F, textureWidth, textureHeight);
            this.smallArms = smallArmsIn;
            if (smallArmsIn) {
                this.bipedLeftArm = new RendererModel(this, 32, 48);
                this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
                this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
                this.bipedRightArm = new RendererModel(this, 40, 16);
                this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
                this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
                this.bipedLeftArmwear = new RendererModel(this, 48, 48);
                this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
                this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
                this.bipedRightArmwear = new RendererModel(this, 40, 32);
                this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
                this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
            } else {
                this.bipedLeftArm = new RendererModel(this, 32, 48);
                this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
                this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
                this.bipedLeftArmwear = new RendererModel(this, 48, 48);
                this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
                this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
                this.bipedRightArmwear = new RendererModel(this, 40, 32);
                this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
                this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
            }

            this.bipedLeftLeg = new RendererModel(this, 16, 48);
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedLeftLegwear = new RendererModel(this, 0, 48);
            this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedRightLegwear = new RendererModel(this, 0, 32);
            this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
            this.bipedBodyWear = new RendererModel(this, 16, 32);
            this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
            this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
        }

        public ParsedBipedModel addCube(RendererModel rendererModel) {
            this.cubes.add(rendererModel);
            return this;
        }

        @Override
        public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            if (this.isChild) {
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
                GlStateManager.translatef(0.0F, 24.0F * scale, 0.0F);
                this.bipedLeftLegwear.render(scale);
                this.bipedRightLegwear.render(scale);
                this.bipedLeftArmwear.render(scale);
                this.bipedRightArmwear.render(scale);
                this.bipedBodyWear.render(scale);
            } else {
                if (entityIn.shouldRenderSneaking()) {
                    GlStateManager.translatef(0.0F, 0.2F, 0.0F);
                }

                this.bipedLeftLegwear.render(scale);
                this.bipedRightLegwear.render(scale);
                this.bipedLeftArmwear.render(scale);
                this.bipedRightArmwear.render(scale);
                this.bipedBodyWear.render(scale);
            }

            for (RendererModel cube : this.cubes) {
                cube.render(scale);
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        @Override
        public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
            this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
            this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
            this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
            this.bipedBodyWear.copyModelAngles(this.bipedBody);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            this.bipedLeftArmwear.showModel = visible;
            this.bipedRightArmwear.showModel = visible;
            this.bipedLeftLegwear.showModel = visible;
            this.bipedRightLegwear.showModel = visible;
            this.bipedBodyWear.showModel = visible;
        }
    }

}
