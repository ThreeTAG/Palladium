package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntityGlowAbility extends Ability {

    public static final PalladiumProperty<Mode> MODE = new EntityGlowModeProperty("mode").configurable("Determines if this ability makes the entity itself glow, or lets you see other entities with their glow effect.");
    public static final PalladiumProperty<Color> COLOR = new ColorProperty("color").configurable("Determines the color of the glow effect. If left empty, it will use the default team color.");
    public static final PalladiumProperty<Double> DISTANCE = new DoubleProperty("distance").configurable("The distance at which the glow effect is visible. If left empty, it will behave normally.");
    public static final PalladiumProperty<String> PROPERTY_KEY = new StringProperty("property_key").configurable("If you want to make the glow dependent on a Palladium Property, this is the key/name of it which you can specify.");
    public static final PalladiumProperty<String> PROPERTY_VALUE = new StringProperty("property_value").configurable("If you want to make the glow dependent on a Palladium Property, this is value of it which you can specify.");

    public EntityGlowAbility() {
        this.withProperty(ICON, new ItemIcon(Items.GLOW_BERRIES))
                .withProperty(MODE, Mode.SELF)
                .withProperty(COLOR, null)
                .withProperty(DISTANCE, 0D)
                .withProperty(PROPERTY_KEY, null)
                .withProperty(PROPERTY_VALUE, null);
    }

    @Override
    public String getDocumentationDescription() {
        return "Makes the entity, the entities visible for you, glow.";
    }

    public static boolean shouldGlow(Entity renderedEntity, Entity cameraEntity) {
        if (renderedEntity instanceof LivingEntity living) {
            for (AbilityInstance ability : AbilityUtil.getEnabledInstances(living, Abilities.ENTITY_GLOW.get())) {
                if (ability.getProperty(EntityGlowAbility.MODE) == Mode.SELF && propertyCheck(renderedEntity, ability.getProperty(PROPERTY_KEY), ability.getProperty(PROPERTY_VALUE))) {
                    double maxDistance = ability.getProperty(EntityGlowAbility.DISTANCE);
                    return maxDistance <= 0D || renderedEntity == cameraEntity || renderedEntity.distanceToSqr(cameraEntity) <= maxDistance * maxDistance;
                }
            }
        }

        if (renderedEntity != cameraEntity && cameraEntity instanceof LivingEntity living) {
            for (AbilityInstance ability : AbilityUtil.getEnabledInstances(living, Abilities.ENTITY_GLOW.get())) {
                if (ability.getProperty(EntityGlowAbility.MODE) == Mode.OTHERS && propertyCheck(renderedEntity, ability.getProperty(PROPERTY_KEY), ability.getProperty(PROPERTY_VALUE))) {
                    double maxDistance = ability.getProperty(EntityGlowAbility.DISTANCE);
                    return maxDistance <= 0D || renderedEntity.distanceToSqr(cameraEntity) <= maxDistance * maxDistance;
                }
            }
        }

        return false;
    }

    private static boolean propertyCheck(Entity entity, String key, String value) {
        if (key == null || value == null) {
            return true;
        }

        var result = new AtomicBoolean(false);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            var prop = handler.getPropertyByName(key);

            if (prop != null) {
                Object val = handler.get(prop);

                if (val != null) {
                    result.set(val.toString().equals(value));
                }
            }
        });
        return result.get();
    }

    public enum Mode {

        SELF("self"),
        OTHERS("others");

        public final String name;

        Mode(String name) {
            this.name = name;
        }
    }
}
