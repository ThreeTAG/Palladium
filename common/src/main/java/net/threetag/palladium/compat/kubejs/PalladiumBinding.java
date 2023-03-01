package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.core.EntityKJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;

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
}
