package net.threetag.palladium.power.ability;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Items;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.TagKeyListProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DamageImmunityAbility extends Ability {

    public static final PalladiumProperty<List<TagKey<DamageType>>> DAMAGE_SOURCES = new TagKeyListProperty<>("damage_sources", Registries.DAMAGE_TYPE).configurable("Determines which damage sources have no effect on the entity based on a tag. Minecraft's builtin damage source tags: " + Arrays.toString(getExampleTags().stream().map(t -> t.location().toString()).toArray()));

    public DamageImmunityAbility() {
        this.withProperty(ICON, new ItemIcon(Items.POTION));
        this.withProperty(DAMAGE_SOURCES, Arrays.asList(DamageTypeTags.IS_LIGHTNING, DamageTypeTags.IS_FALL));
    }

    public static boolean isImmuneAgainst(AbilityEntry entry, DamageSource source) {
        if (!entry.isEnabled()) {
            return false;
        }

        for (TagKey<DamageType> tag : entry.getProperty(DAMAGE_SOURCES)) {
            if (source.is(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDocumentationDescription() {
        return "Makes the entity immune against certain damage source tags.";
    }

    public static List<TagKey<?>> getExampleTags() {
        List<TagKey<?>> list = new ArrayList<>();
        Field[] allFields = DamageTypeTags.class.getDeclaredFields();

        for (Field field : allFields) {
            try {
                if(field.get(null) instanceof TagKey<?> tag) {
                    list.add(tag);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}
