package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class AbilityTypeUnlockedCondition extends Condition {

    private final ResourceLocation abilityId;

    public AbilityTypeUnlockedCondition(ResourceLocation abilityId) {
        this.abilityId = abilityId;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        return AbilityUtil.isTypeUnlocked(entity, this.abilityId);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_TYPE_UNLOCKED.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> ABILITY_TYPE = new ResourceLocationProperty("ability_type").configurable("ID of the ability type to look for. If one ability can be found which is unlocked, the condition will be true");

        public Serializer() {
            this.withProperty(ABILITY_TYPE, Palladium.id("dummy"));
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityTypeUnlockedCondition(this.getProperty(json, ABILITY_TYPE));
        }
    }
}