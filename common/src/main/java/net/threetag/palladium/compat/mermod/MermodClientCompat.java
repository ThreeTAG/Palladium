package net.threetag.palladium.compat.mermod;

import net.minecraft.world.entity.player.Player;

public class MermodClientCompat {

    public static MermodClientCompat INSTANCE = new MermodClientCompat();

    public boolean shouldRenderTail(Player player) {
        return false;
    }

}
