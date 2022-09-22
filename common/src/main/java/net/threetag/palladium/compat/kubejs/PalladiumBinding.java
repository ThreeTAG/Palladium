package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.core.EntityKJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PalladiumBinding {

    public static void swingArm(LivingEntity entity, InteractionHand hand) {
        entity.swing(hand, true);
    }

    public static IIcon createItemIcon(ItemStack itemStack) {
        return new ItemIcon(itemStack);
    }

    public static IIcon createTextureIcon(ResourceLocation path) {
        return new TexturedIcon(path);
    }

    public static Object getProperty(EntityKJS entity, CharSequence key) {
        var handler = EntityPropertyHandler.getHandler(entity.kjs$self());
        PalladiumProperty property = handler.getPropertyByName(key.toString());

        if (property != null) {
            return handler.get(property);
        } else {
            return null;
        }
    }

    public static boolean setProperty(Entity entity, CharSequence key, Object value) {
        var handler = EntityPropertyHandler.getHandler(entity);
        PalladiumProperty property = handler.getPropertyByName(key.toString());

        if (property != null) {
            handler.set(property, PalladiumKubeJSPlugin.fixValues(property, value));
            return true;
        }

        return false;
    }

    public static boolean hasProperty(Entity entity, String key) {
        return EntityPropertyHandler.getHandler(entity).getPropertyByName(key) != null;
    }

    public static AbilityEntryJS getAbilityEntry(LivingEntity entity, ResourceLocation powerId, String abilityId) {
        var entry = Ability.getEntry(entity, powerId, abilityId);
        return entry != null ? new AbilityEntryJS(entry) : null;
    }

    public static List<AbilityEntryJS> getAbilityEntries(LivingEntity entity) {
        return Ability.getEntries(entity).stream().map(AbilityEntryJS::new).collect(Collectors.toList());
    }

    public static List<AbilityEntryJS> getAbilityEntries(LivingEntity entity, ResourceLocation abilityId) {
        Ability ability = Ability.REGISTRY.get(abilityId);
        return ability != null ? Ability.getEntries(entity, ability).stream().map(AbilityEntryJS::new).collect(Collectors.toList()) : new ArrayList<>();
    }

    public static Collection<AbilityEntryJS> getEnabledAbilityEntries(LivingEntity entity, ResourceLocation abilityId) {
        Ability ability = Ability.REGISTRY.get(abilityId);
        return ability != null ? Ability.getEnabledEntries(entity, ability).stream().map(AbilityEntryJS::new).collect(Collectors.toList()) : new ArrayList<>();
    }
}
