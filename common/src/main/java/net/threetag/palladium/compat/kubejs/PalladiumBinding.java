package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
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

    public static void swingArm(LivingEntityJS entity, InteractionHand hand) {
        entity.minecraftLivingEntity.swing(hand, true);
    }

    public static IIcon createItemIcon(ItemStackJS itemStackJS) {
        return new ItemIcon(itemStackJS.getItemStack());
    }

    public static IIcon createTextureIcon(ResourceLocation path) {
        return new TexturedIcon(path);
    }

    public static Object getProperty(EntityJS entity, CharSequence key) {
        var handler = EntityPropertyHandler.getHandler(entity.minecraftEntity);
        PalladiumProperty property = handler.getPropertyByName(key.toString());

        if (property != null) {
            return handler.get(property);
        } else {
            return null;
        }
    }

    public static boolean setProperty(EntityJS entity, CharSequence key, Object value) {
        var handler = EntityPropertyHandler.getHandler(entity.minecraftEntity);
        PalladiumProperty property = handler.getPropertyByName(key.toString());

        if (property != null) {
            handler.set(property, PalladiumKubeJSPlugin.fixValues(property, value));
            return true;
        }

        return false;
    }

    public static boolean hasProperty(EntityJS entity, String key) {
        return EntityPropertyHandler.getHandler(entity.minecraftEntity).getPropertyByName(key) != null;
    }

    public static AbilityEntryJS getAbilityEntry(LivingEntityJS entity, ResourceLocation powerId, String abilityId) {
        var entry = Ability.getEntry(entity.minecraftLivingEntity, powerId, abilityId);
        return entry != null ? new AbilityEntryJS(entry) : null;
    }

    public static List<AbilityEntryJS> getAbilityEntries(LivingEntityJS entity) {
        return Ability.getEntries(entity.minecraftLivingEntity).stream().map(AbilityEntryJS::new).collect(Collectors.toList());
    }

    public static List<AbilityEntryJS> getAbilityEntries(LivingEntityJS entity, ResourceLocation abilityId) {
        Ability ability = Ability.REGISTRY.get(abilityId);
        return ability != null ? Ability.getEntries(entity.minecraftLivingEntity, ability).stream().map(AbilityEntryJS::new).collect(Collectors.toList()) : new ArrayList<>();
    }

    public static Collection<AbilityEntryJS> getEnabledAbilityEntries(LivingEntityJS entity, ResourceLocation abilityId) {
        Ability ability = Ability.REGISTRY.get(abilityId);
        return ability != null ? Ability.getEnabledEntries(entity.minecraftLivingEntity, ability).stream().map(AbilityEntryJS::new).collect(Collectors.toList()) : new ArrayList<>();
    }
}
