package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class EntityTypeTagCondition extends Condition {

    private final TagKey<EntityType<?>> tag;

    public EntityTypeTagCondition(TagKey<EntityType<?>> tag) {
        this.tag = tag;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        return entity.getType().is(this.tag);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ENTITY_TYPE_TAG.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> ENTITY_TYPE = new ResourceLocationProperty("entity_type_tag").configurable("The tag the type of the entity must be in for the condition to be active");

        public Serializer() {
            this.withProperty(ENTITY_TYPE, EntityTypeTags.SKELETONS.location());
        }

        @Override
        public Condition make(JsonObject json) {
            return new EntityTypeTagCondition(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, this.getProperty(json, ENTITY_TYPE)));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is of a certain tag";
        }
    }
}
