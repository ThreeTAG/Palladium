package net.threetag.palladium.item.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;
import net.threetag.palladium.compat.trinkets.fabric.TrinketsCompat;
import net.threetag.palladium.item.CurioTrinket;
import net.threetag.palladiumcore.util.Platform;

public class CurioTrinketRegistryImpl {

    public static void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        if (Platform.isModLoaded("trinkets")) {
            TrinketsCompat.registerCurioTrinket(item, curioTrinket);
        }
    }

    public static boolean equipItem(Player user, ItemStack stack) {
        if (Platform.isModLoaded("trinkets")) {
            return TrinketsCompat.equipItem(user, stack);
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        if (Platform.isModLoaded("trinkets")) {
            TrinketsCompat.registerRenderer(item, renderer);
        }
    }

}
