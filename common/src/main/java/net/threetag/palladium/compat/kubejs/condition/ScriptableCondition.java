package net.threetag.palladium.compat.kubejs.condition;

import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.Nullable;

public class ScriptableCondition extends Condition {

    public final ConditionBuilder builder;
    public final ConditionSerializer serializer;

    public ScriptableCondition(ConditionBuilder builder, ConditionSerializer serializer) {
        this.builder = builder;
        this.serializer = serializer;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return this.builder.test != null && this.builder.test.test(UtilsJS.getLevel(entity.level).getLivingEntity(entity));
    }

    @Override
    public ConditionSerializer getSerializer() {
        return this.serializer;
    }

}
