package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CompoundPackRenderLayer implements IPackRenderLayer {

    private final List<IPackRenderLayer> layers;
    private final List<Condition> conditions = new ArrayList<>();

    public CompoundPackRenderLayer(
            List<IPackRenderLayer> layers) {
        this.layers = layers;
    }

    @Override
    public void render(LivingEntity entity, AbilityEntry abilityEntry, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions)) {
            for (IPackRenderLayer layer : this.layers) {
                layer.render(entity, abilityEntry, poseStack, bufferSource, parentModel, packedLight, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    @Override
    public IPackRenderLayer addCondition(Condition condition) {
        this.conditions.add(condition);
        return this;
    }

    public static CompoundPackRenderLayer parse(JsonObject json) {
        JsonArray list = GsonHelper.getAsJsonArray(json, "layers");
        List<IPackRenderLayer> layers = new ArrayList<>();

        for (JsonElement el : list) {
            layers.add(PackRenderLayerManager.parseLayer(el.getAsJsonObject()));
        }

        return IPackRenderLayer.parseConditions(new CompoundPackRenderLayer(layers), json);
    }

    public List<IPackRenderLayer> layers() {
        return layers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CompoundPackRenderLayer) obj;
        return Objects.equals(this.layers, that.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layers);
    }

    @Override
    public String toString() {
        return "CompoundPackRenderLayer[" +
                "layers=" + layers + ']';
    }

}
