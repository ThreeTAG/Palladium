package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.EntityTypeProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class EntityTypeCondition extends Condition {

    private final EntityType<?> entityType;

    public EntityTypeCondition(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        return entity.getType() == this.entityType;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ENTITY_TYPE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<EntityType<?>> ENTITY_TYPE = new EntityTypeProperty("entity_type").configurable("The entity type the entity must be of for the condition to be active");

        public Serializer() {
            this.withProperty(ENTITY_TYPE, EntityType.PLAYER);
        }

        @Override
        public Condition make(JsonObject json) {
            return new EntityTypeCondition(this.getProperty(json, ENTITY_TYPE));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is of a specific entity type.";
        }
    }
}
