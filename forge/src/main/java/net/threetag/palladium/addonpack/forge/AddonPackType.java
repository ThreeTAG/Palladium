package net.threetag.palladium.addonpack.forge;

import net.minecraft.server.packs.PackType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AddonPackType {

    public static PackType ADDON;

    public static void init() {
        if(ADDON == null) {
            try {
                Method M_CREATE = PackType.class.getDeclaredMethod("create", String.class, String.class, com.mojang.bridge.game.PackType.class);
                M_CREATE.setAccessible(true);
                ADDON = (PackType) M_CREATE.invoke(null, "ADDON", "addon", com.mojang.bridge.game.PackType.DATA);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Error calling private method", e);
            }
        }
    }

}
