package net.threetag.palladium.item.forge;

import dev.architectury.platform.Platform;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.palladium.compat.curios.forge.CuriosCompat;
import net.threetag.palladium.item.CurioTrinket;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;

public class CurioTrinketRegistryImpl {

    public static void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        if (Platform.isModLoaded("curios")) {
            CuriosCompat.registerCurioTrinket(item, curioTrinket);
        }
    }

    public static boolean equipItem(Player user, ItemStack stack) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        if (Platform.isModLoaded("trinkets")) {
            CuriosCompat.registerRenderer(item, renderer);
        }
    }

}
