package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionContextType;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.List;

public interface IPackRenderLayer {

    void render(LivingEntity entity, AbilityEntry abilityEntry, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    IPackRenderLayer addCondition(Condition condition);

    static <T extends IPackRenderLayer> T parseConditions(T layer, JsonObject json) {
        if (GsonHelper.isValidNode(json, "conditions")) {
            ConditionSerializer.listFromJSON(json.get("conditions"), ConditionContextType.RENDER_LAYERS).forEach(layer::addCondition);
        }
        return layer;
    }

    static boolean conditionsFulfilled(LivingEntity entity, List<Condition> conditions) {
        if(conditions.isEmpty()) {
            return true;
        }

        for (Condition condition : conditions) {
            if(!condition.active(entity, null, null, null)) {
                return false;
            }
        }

        return true;
    }
}
