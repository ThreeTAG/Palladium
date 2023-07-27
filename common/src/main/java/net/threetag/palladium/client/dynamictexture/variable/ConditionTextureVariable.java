package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.condition.context.ConditionContext;

import java.util.List;

public class ConditionTextureVariable extends AbstractBooleanTextureVariable {

    private final List<Condition> conditions;

    public ConditionTextureVariable(JsonObject json) {
        super(json);

        this.conditions = ConditionSerializer.listFromJSON(json.get("conditions"), ConditionEnvironment.ASSETS);
    }

    @Override
    public boolean getBoolean(Entity entity) {
        if (entity instanceof LivingEntity living) {
            for (Condition condition : this.conditions) {
                if (!condition.active(ConditionContext.forEntity(living))) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
