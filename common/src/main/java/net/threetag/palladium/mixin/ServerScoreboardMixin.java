package net.threetag.palladium.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.threetag.palladium.world.TrackedScoresManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    public abstract List<Packet<?>> getStopTrackingPackets(Objective objective);

    @Inject(method = "setDisplayObjective", at = @At("HEAD"))
    public void setDisplayObjective(int objectiveSlot, @Nullable Objective objective, CallbackInfo ci) {
        var scoreboard = (Scoreboard) (Object) this;
        Objective previousObj = scoreboard.getDisplayObjective(objectiveSlot);
        if (previousObj != null && previousObj != objective && TrackedScoresManager.INSTANCE.isTracked(previousObj.getName())) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(objectiveSlot, null));
        }
    }

    @Inject(method = "stopTrackingObjective", at = @At("HEAD"), cancellable = true)
    public void stopTrackingObjective(Objective objective, CallbackInfo ci) {
        if (TrackedScoresManager.INSTANCE.isTracked(objective.getName())) {
            ci.cancel();

            for (Packet<?> packet : getStopTrackingPackets(objective)) {
                this.server.getPlayerList().broadcastAll(packet);
            }
        }
    }

    @Inject(method = "getStopTrackingPackets", at = @At("RETURN"), cancellable = true)
    public void getStopTrackingPackets(Objective objective, CallbackInfoReturnable<List<Packet<?>>> cir) {
        if (TrackedScoresManager.INSTANCE.isTracked(objective.getName())) {
            List<Packet<?>> packets = new ArrayList<>();
            var scoreboard = (Scoreboard) (Object) this;

            for (int i = 0; i < 19; ++i) {
                if (scoreboard.getDisplayObjective(i) == objective) {
                    packets.add(new ClientboundSetDisplayObjectivePacket(i, null));
                }
            }

            cir.setReturnValue(packets);
        }
    }

}
