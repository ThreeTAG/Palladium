package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class EntityTypeTagCondition extends Condition {

    private final TagKey<EntityType<?>> tag;

    public EntityTypeTagCondition(TagKey<EntityType<?>> tag) {
        this.tag = tag;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

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
            return new EntityTypeTagCondition(TagKey.create(Registries.ENTITY_TYPE, this.getProperty(json, ENTITY_TYPE)));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is of a certain tag.";
        }
    }
}
