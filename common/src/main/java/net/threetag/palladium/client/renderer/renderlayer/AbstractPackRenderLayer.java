package net.threetag.palladium.client.renderer.renderlayer;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.entity.BodyPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPackRenderLayer implements IPackRenderLayer {

    protected final List<Condition> conditions = new ArrayList<>();
    protected final List<Condition> thirdPersonConditions = new ArrayList<>();
    protected final List<Condition> firstPersonConditions = new ArrayList<>();
    protected final List<BodyPart> hiddenBodyParts = new ArrayList<>();

    @Override
    public IPackRenderLayer addCondition(Condition condition, PerspectiveConditionContext context) {
        if (context == PerspectiveConditionContext.BOTH) {
            this.conditions.add(condition);
        } else if (context == PerspectiveConditionContext.THIRD_PERSON) {
            this.thirdPersonConditions.add(condition);
        } else if (context == PerspectiveConditionContext.FIRST_PERSON) {
            this.firstPersonConditions.add(condition);
        }
        return this;
    }

    public AbstractPackRenderLayer addHiddenBodyPart(BodyPart bodyPart) {
        if (!this.hiddenBodyParts.contains(bodyPart)) {
            this.hiddenBodyParts.add(bodyPart);
        }
        return this;
    }

    @Override
    public List<BodyPart> getHiddenBodyParts(LivingEntity entity) {
        return IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions) ? this.hiddenBodyParts : Collections.emptyList();
    }
}
