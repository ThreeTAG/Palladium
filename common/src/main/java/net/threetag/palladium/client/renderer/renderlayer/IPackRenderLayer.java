package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionContextType;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IPackRenderLayer {

    void render(IRenderLayerContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    default void renderArm(IRenderLayerContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

    }

    IPackRenderLayer addCondition(Condition condition, ConditionContext context);

    List<BodyPart> getHiddenBodyParts(LivingEntity entity);

    static <T extends IPackRenderLayer> T parseConditions(T layer, JsonObject json) {
        for (ConditionContext context : ConditionContext.values()) {
            if (GsonHelper.isValidNode(json, context.key)) {
                var el = json.get(context.key);

                if (el.isJsonPrimitive()) {
                    var result = el.getAsBoolean();
                    layer.addCondition(new Condition() {
                        @Override
                        public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
                            return result;
                        }

                        @Override
                        public ConditionSerializer getSerializer() {
                            return null;
                        }
                    }, context);
                } else {
                    ConditionSerializer.listFromJSON(el, ConditionContextType.RENDER_LAYERS).forEach(cond -> layer.addCondition(cond, context));
                }
            }
        }

        return layer;
    }

    static boolean conditionsFulfilled(LivingEntity entity, List<Condition> bothConditions, List<Condition> specificConditions) {
        for (Condition condition : bothConditions) {
            if (!condition.active(entity, null, null, null)) {
                return false;
            }
        }

        for (Condition condition : specificConditions) {
            if (!condition.active(entity, null, null, null)) {
                return false;
            }
        }

        return true;
    }

    enum ConditionContext {

        BOTH("conditions"),
        FIRST_PERSON("first_person_conditions"),
        THIRD_PERSON("third_person_conditions");

        public final String key;

        ConditionContext(String key) {
            this.key = key;
        }
    }

}
