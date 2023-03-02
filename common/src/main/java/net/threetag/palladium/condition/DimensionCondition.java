package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import org.jetbrains.annotations.Nullable;

public class DimensionCondition extends Condition {

    private final ResourceKey<Level> dimension;

    public DimensionCondition(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return entity.level.dimension().equals(this.dimension);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.DIMENSION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> DIMENSION = new ResourceLocationProperty("dimension").configurable("ID of the dimension the player must be in. Example values: minecraft:overworld, minecraft:nether, minecraft:the_end");

        public Serializer() {
            this.withProperty(DIMENSION, new ResourceLocation("minecraft:overworld"));
        }

        @Override
        public Condition make(JsonObject json) {
            return new DimensionCondition(ResourceKey.create(Registry.DIMENSION_REGISTRY, this.getProperty(json, DIMENSION)));
        }

        @Override
        public String getDescription() {
            return "Checks if the player is in a specific dimension.";
        }
    }
}
