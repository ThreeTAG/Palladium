package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.RegistryObjectProperty;

public class HasEffectCondition extends Condition {

    public final MobEffect mobEffect;

    public HasEffectCondition(MobEffect mobEffect) {
        this.mobEffect = mobEffect;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        return entity != null && entity.hasEffect(this.mobEffect);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HAS_EFFECT.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<MobEffect> EFFECT = new RegistryObjectProperty<>("effect", Registry.MOB_EFFECT).configurable("ID of the (potion) effect that is being checked for.");

        public Serializer() {
            this.withProperty(EFFECT, MobEffects.POISON);
        }

        @Override
        public Condition make(JsonObject json) {
            return new HasEffectCondition(this.getProperty(json, EFFECT));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a (potion) effect.";
        }
    }
}
