package net.threetag.threecore.util.client.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraft.util.JSONUtils;

import java.util.List;
import java.util.function.Function;

public class EntityModelParser implements Function<JsonObject, EntityModel> {

    @Override
    public EntityModel apply(JsonObject jsonObject) {
        ParsedModel model = new ParsedModel();
        model.textureWidth = JSONUtils.getInt(jsonObject, "texture_width", 64);
        model.textureHeight = JSONUtils.getInt(jsonObject, "texture_height", 32);

        if (JSONUtils.hasField(jsonObject, "cubes")) {
            JsonArray cubes = JSONUtils.getJsonArray(jsonObject, "cubes");

            for (int i = 0; i < cubes.size(); i++) {
                JsonObject cubeJson = cubes.get(i).getAsJsonObject();
                model.addCube(parseRendererModel(cubeJson, model));
            }
        }

        return model;
    }

    public static RendererModel parseRendererModel(JsonObject json, Model model) {
        RendererModel rendererModel = new RendererModel(model, JSONUtils.getInt(json, "texture_offset_x", 0), JSONUtils.getInt(json, "texture_offset_y", 0));
        rendererModel.addBox(JSONUtils.getFloat(json, "offset_x", 0F), JSONUtils.getFloat(json, "offset_y", 0F), JSONUtils.getFloat(json, "offset_z", 0F), JSONUtils.getInt(json, "width", 1), JSONUtils.getInt(json, "height", 1), JSONUtils.getInt(json, "depth", 1), JSONUtils.getFloat(json, "scale", 0F));
        rendererModel.setRotationPoint(JSONUtils.getFloat(json, "rotation_point_x", 0F), JSONUtils.getFloat(json, "rotation_point_y", 0F), JSONUtils.getFloat(json, "rotation_point_z", 0F));

        if (JSONUtils.hasField(json, "children")) {
            JsonArray children = JSONUtils.getJsonArray(json, "children");
            for (int i = 0; i < children.size(); i++) {
                rendererModel.addChild(parseRendererModel(children.get(i).getAsJsonObject(), model));
            }
        }

        return rendererModel;
    }

    public static class ParsedModel extends EntityModel {

        public List<RendererModel> cubes = Lists.newLinkedList();

        public ParsedModel(List<RendererModel> cubes) {
            this.cubes = cubes;
        }

        public ParsedModel() {

        }

        public ParsedModel addCube(RendererModel rendererModel) {
            this.cubes.add(rendererModel);
            return this;
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            for (RendererModel cube : this.cubes) {
                cube.render(scale);
            }
        }
    }

}
