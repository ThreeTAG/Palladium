package net.threetag.palladium.compat.kubejs.condition;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.compat.kubejs.PalladiumJSEvents;
import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionContextType;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ScriptableCondition extends Condition {

    public Function<LivingEntityJS, Boolean> function;

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return this.function != null ? this.function.apply(UtilsJS.getLevel(entity.level).getLivingEntity(entity)) : false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return PalladiumKubeJSPlugin.SCRIPTABLE_CONDITION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> KEY = new ResourceLocationProperty("key").configurable("The unique key for your KubeJS code. You'll need to use this key when defining the handler code in the \"" + PalladiumJSEvents.SCRIPTABLE_CONDITIONS + "\" event.");

        public Serializer() {
            this.withProperty(KEY, new ResourceLocation("example:key"));
        }

        @Override
        public Condition make(JsonObject json) {
            return null;
        }

        @Override
        public Condition make(JsonObject json, ConditionContextType type) {
            var condition = new ScriptableCondition();
            new ScriptableConditionEventJS(this.getProperty(json, KEY), condition).post(type == ConditionContextType.ABILITIES ? ScriptType.SERVER : ScriptType.CLIENT, PalladiumJSEvents.SCRIPTABLE_CONDITIONS);
            return condition;
        }
    }
}
