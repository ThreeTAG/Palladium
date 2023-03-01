package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.BodyPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CompoundPackRenderLayer extends AbstractPackRenderLayer {

    private final List<IPackRenderLayer> layers;

    public CompoundPackRenderLayer(
            List<IPackRenderLayer> layers) {
        this.layers = layers;
    }

    @Override
    public void render(IRenderLayerContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (IPackRenderLayer.conditionsFulfilled(context.getEntity(), this.conditions, this.thirdPersonConditions)) {
            for (IPackRenderLayer layer : this.layers) {
                layer.render(context, poseStack, bufferSource, parentModel, packedLight, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    @Override
    public void renderArm(IRenderLayerContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (IPackRenderLayer.conditionsFulfilled(context.getEntity(), this.conditions, this.firstPersonConditions)) {
            for (IPackRenderLayer layer : this.layers) {
                layer.renderArm(context, arm, playerRenderer, poseStack, bufferSource, packedLight);
            }
        }
    }

    @Override
    public List<BodyPart> getHiddenBodyParts(LivingEntity entity) {
        List<BodyPart> bodyParts = new ArrayList<>();

        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions)) {
            for (IPackRenderLayer layer : this.layers) {
                for (BodyPart hiddenBodyPart : layer.getHiddenBodyParts(entity)) {
                    if (!bodyParts.contains(hiddenBodyPart)) {
                        bodyParts.add(hiddenBodyPart);
                    }
                }
            }
        }

        return bodyParts;
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
