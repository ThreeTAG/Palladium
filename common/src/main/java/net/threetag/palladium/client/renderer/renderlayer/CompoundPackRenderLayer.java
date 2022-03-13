package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.ArrayList;
import java.util.List;

public record CompoundPackRenderLayer(
        List<IPackRenderLayer> layers) implements IPackRenderLayer {

    @Override
    public void render(LivingEntity entity, AbilityEntry abilityEntry, PoseStack poseStack, MultiBufferSource bufferSource, HumanoidModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (IPackRenderLayer layer : this.layers) {
            layer.render(entity, abilityEntry, poseStack, bufferSource, parentModel, packedLight, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    public static CompoundPackRenderLayer parse(JsonObject json) {
        JsonArray list = GsonHelper.getAsJsonArray(json, "layers");
        List<IPackRenderLayer> layers = new ArrayList<>();

        for (JsonElement el : list) {
            layers.add(PackRenderLayerManager.parseLayer(el.getAsJsonObject()));
        }

        return new CompoundPackRenderLayer(layers);
    }
}
