package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import net.minecraft.world.InteractionHand;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PalladiumBinding {

    public static void swingArm(LivingEntityJS entity, InteractionHand hand) {
        entity.minecraftLivingEntity.swing(hand, true);
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

}
