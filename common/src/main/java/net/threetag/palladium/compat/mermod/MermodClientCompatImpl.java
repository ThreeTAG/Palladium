package net.threetag.palladium.compat.mermod;

import io.github.thatpreston.mermod.MermodClient;
import net.minecraft.world.entity.player.Player;

public class MermodClientCompatImpl extends MermodClientCompat {

    public static void init() {
        MermodClientCompat.INSTANCE = new MermodClientCompatImpl();
    }

    @Override
    public boolean shouldRenderTail(Player player) {
        return MermodClient.shouldRenderTail(player);
    }
}
