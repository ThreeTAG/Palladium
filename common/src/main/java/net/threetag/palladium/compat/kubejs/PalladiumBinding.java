package net.threetag.palladium.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

    public static Object getProperty(Entity entity, CharSequence key) {
        AtomicReference result = new AtomicReference();
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty property = handler.getPropertyByName(key.toString());

            if (property != null) {
                result.set(handler.get(property));
            }
        });

        return result.get();
    }

    public static boolean setProperty(Entity entity, CharSequence key, Object value) {
        AtomicBoolean result = new AtomicBoolean(false);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty property = handler.getPropertyByName(key.toString());

            if (property != null) {
                handler.set(property, PalladiumProperty.fixValues(property, value));
                result.set(true);
            }
        });

        return result.get();
    }

    public static boolean hasProperty(Entity entity, String key) {
        AtomicBoolean result = new AtomicBoolean(false);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            result.set(handler.getPropertyByName(key) != null);
        });
        return result.get();
    }

    public static void setItemInSlot(LivingEntity entity, @Nullable PlayerSlot slot, ItemStack stack) {
        if (slot != null) {
            slot.setItem(entity, stack);
        }
    }

    public static ItemStack getItemInSlot(LivingEntity entity, @Nullable PlayerSlot slot) {
        if (slot != null) {
            return slot.getItems(entity).stream().findFirst().orElse(ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }
}
