package net.threetag.threecore.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.HandSide;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.util.PlayerUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class BipedModelParser extends EntityModelParser {

    @Override
    public EntityModel apply(JsonObject jsonObject) {
        ParsedBipedModel model = new ParsedBipedModel(parseModelScales(jsonObject.get("scale")), BipedArmType.getFromName(JSONUtils.getString(jsonObject, "arm_type", "default")), JSONUtils.getInt(jsonObject, "texture_width", 64), JSONUtils.getInt(jsonObject, "texture_height", 64));

        if (JSONUtils.hasField(jsonObject, "cubes")) {
            JsonArray cubes = JSONUtils.getJsonArray(jsonObject, "cubes");

            for (int i = 0; i < cubes.size(); i++) {
                JsonObject cubeJson = cubes.get(i).getAsJsonObject();
                ModelRenderer parent = getPart(JSONUtils.getString(cubeJson, "parent", ""), model);

                if (parent != null)
                    parent.addChild(parseRendererModel(cubeJson, model));
                else
                    model.addCube(parseRendererModel(cubeJson, model));
            }
        }

        if (JSONUtils.hasField(jsonObject, "visibility_overrides")) {
            JsonObject overrides = JSONUtils.getJsonObject(jsonObject, "visibility_overrides");

            overrides.entrySet().forEach(entry -> {
                ModelRenderer part = getPart(entry.getKey(), model);

                if (part != null) {
                    model.addVisibilityOverride(part, JSONUtils.getBoolean(overrides, entry.getKey()));
                }
            });
        }

        return model;
    }

    public ModelRenderer getPart(String name, ParsedBipedModel model) {
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

    public Function<String, Float> parseModelScales(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return s -> jsonElement.getAsFloat();
        } else if (jsonElement.isJsonObject()) {
            Map<String, Float> scales = Maps.newHashMap();
            AtomicReference<Float> fallback = new AtomicReference<>(-999F);
            jsonElement.getAsJsonObject().entrySet().forEach(entry -> {
                if (entry.getKey().equalsIgnoreCase("fallback")) {
                    fallback.set(entry.getValue().getAsFloat());
                } else {
                    scales.put(entry.getKey(), entry.getValue().getAsFloat());
                }
            });
            return s -> {
                if (scales.containsKey(s)) {
                    return scales.get(s);
                }
                return fallback.get() <= -999F ? s.equalsIgnoreCase("head") ? 0.5F : 0.25F : fallback.get();
            };
        } else {
            throw new JsonParseException("Model scale must be either a single float or a json object with each model part!");
        }
    }

    public static class ParsedBipedModel<T extends LivingEntity> extends BipedModel<T> implements ISlotDependentVisibility, IArmRenderingModel {

        public List<NamedModelRenderer> cubes = Lists.newLinkedList();
        public Map<ModelRenderer, Boolean> visibilityOverrides = Maps.newHashMap();
        public final ModelRenderer bipedLeftArmwear;
        public final ModelRenderer bipedRightArmwear;
        public final ModelRenderer bipedLeftLegwear;
        public final ModelRenderer bipedRightLegwear;
        public final ModelRenderer bipedBodyWear;
        private final BipedArmType bipedArmType;

        public ParsedBipedModel(Function<String, Float> scales, BipedArmType bipedArmType, int textureWidth, int textureHeight) {
            super(0F, 0.0F, textureWidth, textureHeight);
            this.bipedArmType = bipedArmType;

            this.bipedHead = new ModelRenderer(this, 0, 0);
            this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scales.apply("head"));
            this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedHeadwear = new ModelRenderer(this, 32, 0);
            this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scales.apply("head") + 0.5F);
            this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedBody = new ModelRenderer(this, 16, 16);
            this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scales.apply("chest"));
            this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedRightLeg = new ModelRenderer(this, 0, 16);
            this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scales.apply("right_leg"));
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

            if (bipedArmType == BipedArmType.SMALL) {
                this.bipedLeftArm = new ModelRenderer(this, 32, 48);
                this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, scales.apply("left_arm"));
                this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

                this.bipedRightArm = new ModelRenderer(this, 40, 16);
                this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, scales.apply("right_arm"));
                this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

                this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
                this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, scales.apply("left_arm") + 0.25F);
                this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

                this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
                this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, scales.apply("right_arm") + 0.25F);
                this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
            } else {
                this.bipedLeftArm = new ModelRenderer(this, 32, 48);
                this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scales.apply("left_arm"));
                this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

                this.bipedRightArm = new ModelRenderer(this, 40, 16);
                this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scales.apply("right_arm"));
                this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

                this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
                this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scales.apply("left_arm") + 0.25F);
                this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

                this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
                this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scales.apply("right_arm") + 0.25F);
                this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
            }

            this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scales.apply("left_leg"));
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
            this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scales.apply("left_leg") + 0.25F);
            this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
            this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scales.apply("right_leg") + 0.25F);
            this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
            this.bipedBodyWear = new ModelRenderer(this, 16, 32);
            this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scales.apply("chest") + 0.25F);
            this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
        }

        public ParsedBipedModel addCube(NamedModelRenderer rendererModel) {
            this.cubes.add(rendererModel);
            return this;
        }

        public void addVisibilityOverride(ModelRenderer rendererModel, boolean visible) {
            this.visibilityOverrides.put(rendererModel, visible);
        }

        public ModelRenderer getNamedPart(String name) {
            switch (name) {
                case "right_arm_overlay":
                    return this.bipedRightArmwear;
                case "left_arm_overlay":
                    return this.bipedLeftArmwear;
                case "right_leg_overlay":
                    return this.bipedRightLeg;
                case "left_leg_overlay":
                    return this.bipedLeftLegwear;
                case "body_overlay":
                    return this.bipedBodyWear;
            }
            
            for (NamedModelRenderer modelRenderer : this.cubes) {
                if (modelRenderer.getName().equals(name)) {
                    return modelRenderer;
                }
            }
            return null;
        }

        @Override
        public void setSlotVisibility(EquipmentSlotType slot) {
            this.setVisible(false);
            switch (slot) {
                case HEAD:
                    this.bipedHead.showModel = true;
                    this.bipedHeadwear.showModel = true;
                    break;
                case CHEST:
                    this.bipedBody.showModel = true;
                    this.bipedBodyWear.showModel = true;
                    this.bipedRightArm.showModel = true;
                    this.bipedRightArmwear.showModel = true;
                    this.bipedLeftArm.showModel = true;
                    this.bipedLeftArmwear.showModel = true;
                    break;
                case LEGS:
                    this.bipedBody.showModel = true;
                    this.bipedBodyWear.showModel = true;
                    this.bipedRightLeg.showModel = true;
                    this.bipedRightLegwear.showModel = true;
                    this.bipedLeftLeg.showModel = true;
                    this.bipedLeftLegwear.showModel = true;
                    break;
                case FEET:
                    this.bipedRightLeg.showModel = true;
                    this.bipedRightLegwear.showModel = true;
                    this.bipedLeftLeg.showModel = true;
                    this.bipedLeftLegwear.showModel = true;
            }

            for (Map.Entry<ModelRenderer, Boolean> override : this.visibilityOverrides.entrySet()) {
                override.getKey().showModel = override.getValue();
            }
        }

        @Override
        protected Iterable<ModelRenderer> getBodyParts() {
            return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.bipedBodyWear, this.bipedRightArmwear, this.bipedLeftArmwear, this.bipedRightLegwear, this.bipedLeftLegwear), this.cubes);
        }

        @Override
        public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            if (this.bipedArmType == BipedArmType.FIXED) {
                boolean smallArms = entityIn instanceof PlayerEntity && PlayerUtil.hasSmallArms((PlayerEntity) entityIn);
                this.bipedLeftArm.rotationPointY =
                        this.bipedLeftArmwear.rotationPointY =
                                this.bipedRightArm.rotationPointY =
                                        this.bipedRightArmwear.rotationPointY = smallArms ? 2.5F : 2.0F;
            }

            if (entityIn instanceof ArmorStandEntity) {
                ArmorStandEntity armorStandEntity = (ArmorStandEntity) entityIn;
                this.bipedRightArm.showModel = this.bipedRightArmwear.showModel = this.bipedLeftArm.showModel = this.bipedLeftArmwear.showModel = this.bipedRightArm.showModel && ((ArmorStandEntity) entityIn).getShowArms();
                this.bipedHead.rotateAngleX = ((float) Math.PI / 180F) * armorStandEntity.getHeadRotation().getX();
                this.bipedHead.rotateAngleY = ((float) Math.PI / 180F) * armorStandEntity.getHeadRotation().getY();
                this.bipedHead.rotateAngleZ = ((float) Math.PI / 180F) * armorStandEntity.getHeadRotation().getZ();
                this.bipedBody.rotateAngleX = ((float) Math.PI / 180F) * armorStandEntity.getBodyRotation().getX();
                this.bipedBody.rotateAngleY = ((float) Math.PI / 180F) * armorStandEntity.getBodyRotation().getY();
                this.bipedBody.rotateAngleZ = ((float) Math.PI / 180F) * armorStandEntity.getBodyRotation().getZ();
                this.bipedLeftArm.rotateAngleX = ((float) Math.PI / 180F) * armorStandEntity.getLeftArmRotation().getX();
                this.bipedLeftArm.rotateAngleY = ((float) Math.PI / 180F) * armorStandEntity.getLeftArmRotation().getY();
                this.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 180F) * armorStandEntity.getLeftArmRotation().getZ();
                this.bipedRightArm.rotateAngleX = ((float) Math.PI / 180F) * armorStandEntity.getRightArmRotation().getX();
                this.bipedRightArm.rotateAngleY = ((float) Math.PI / 180F) * armorStandEntity.getRightArmRotation().getY();
                this.bipedRightArm.rotateAngleZ = ((float) Math.PI / 180F) * armorStandEntity.getRightArmRotation().getZ();
                this.bipedLeftLeg.rotateAngleX = ((float) Math.PI / 180F) * armorStandEntity.getLeftLegRotation().getX();
                this.bipedLeftLeg.rotateAngleY = ((float) Math.PI / 180F) * armorStandEntity.getLeftLegRotation().getY();
                this.bipedLeftLeg.rotateAngleZ = ((float) Math.PI / 180F) * armorStandEntity.getLeftLegRotation().getZ();
                this.bipedRightLeg.rotateAngleX = ((float) Math.PI / 180F) * armorStandEntity.getRightLegRotation().getX();
                this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 180F) * armorStandEntity.getRightLegRotation().getY();
                this.bipedRightLeg.rotateAngleZ = ((float) Math.PI / 180F) * armorStandEntity.getRightLegRotation().getZ();
                this.bipedHeadwear.copyModelAngles(this.bipedHead);

                this.bipedRightArm.rotationPointZ = 0.0F;
                this.bipedRightArm.rotationPointX = -5.0F;
                this.bipedLeftArm.rotationPointZ = 0.0F;
                this.bipedLeftArm.rotationPointX = 5.0F;

                this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
                this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
                this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
                this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

                if (bipedArmType == BipedArmType.SMALL) {
                    this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
                    this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
                    this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
                    this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
                } else {
                    this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
                    this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
                    this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
                }

            } else {
                super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                this.bipedHead.rotateAngleZ = 0;
            }

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

        @Override
        public void renderArm(HandSide handSide, MatrixStack matrixStack, IVertexBuilder vertexBuilder, int combinedLight) {
            if (handSide == HandSide.RIGHT) {
                this.bipedRightArm.rotateAngleX = 0.0F;
                this.bipedRightArmwear.rotateAngleX = 0.0F;
                this.bipedRightArm.render(matrixStack, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY);
                this.bipedRightArmwear.render(matrixStack, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY);
            } else {
                this.bipedLeftArm.rotateAngleX = 0.0F;
                this.bipedLeftArmwear.rotateAngleX = 0.0F;
                this.bipedLeftArm.render(matrixStack, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY);
                this.bipedLeftArmwear.render(matrixStack, vertexBuilder, combinedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    public enum BipedArmType {

        NORMAL("normal"),
        SMALL("small"),
        FIXED("fixed");

        private final String name;

        BipedArmType(String name) {
            this.name = name;
        }

        public static BipedArmType getFromName(String name) {
            for (BipedArmType type : values()) {
                if (type.name.equalsIgnoreCase(name)) {
                    return type;
                }
            }

            return NORMAL;
        }

    }

}
