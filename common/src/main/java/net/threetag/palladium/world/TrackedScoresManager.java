package net.threetag.palladium.world;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Score;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.event.PlayerEvents;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import net.threetag.palladiumcore.util.Platform;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TrackedScoresManager extends SimplePreparableReloadListener<List<String>> implements PlayerEvents.Join {

    public static TrackedScoresManager INSTANCE;
    private final List<String> tracked = new ArrayList<>();

    public static void init() {
        INSTANCE = new TrackedScoresManager();
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("tracked_scores"), INSTANCE);
        PlayerEvents.JOIN.register(INSTANCE);
    }

    @Override
    public void playerJoin(Player player) {
        if (!player.level.isClientSide) {
            for (String tracked : this.tracked) {
                var scoreboard = player.getScoreboard();
                var objective = scoreboard.getObjective(tracked);

                if (objective != null && player instanceof ServerPlayer serverPlayer) {
                    for (Score score : scoreboard.getPlayerScores(objective)) {
                        serverPlayer.connection.send(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, objective.getName(), score.getOwner(), score.getScore()));
                    }
                }
            }
        }
    }

    @Override
    protected @NotNull List<String> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        List<String> objectives = new ArrayList<>();
        profiler.startTick();

        for (String namespace : resourceManager.getNamespaces()) {
            profiler.push(namespace);
            List<Resource> list = resourceManager.getResourceStack(new ResourceLocation(namespace, "tracked_scores.json"));

            for (Resource resource : list) {
                profiler.push(resource.sourcePackId());

                try {
                    Reader reader = resource.openAsReader();
                    profiler.push("parse");
                    JsonObject json = GsonHelper.parse(reader);

                    var array = GsonHelper.getAsJsonArray(json, "objectives");
                    for (JsonElement jsonElement : array) {
                        var obj = GsonHelper.convertToString(jsonElement, "objectives[]").trim();

                        if (!objectives.contains(obj)) {
                            objectives.add(obj);
                        }
                    }

                    profiler.popPush("register");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        profiler.endTick();
        return objectives;
    }

    @Override
    protected void apply(List<String> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.tracked.clear();
        this.tracked.addAll(object);

        if (!this.tracked.isEmpty()) {
            Palladium.LOGGER.info("Registered " + this.tracked.size() + " objectives to be tracked");
        }

        var server = Platform.getCurrentServer();

        if (server != null) {
            for (String tracked : this.tracked) {
                var scoreboard = server.getScoreboard();
                var objective = scoreboard.getObjective(tracked);

                if (objective != null) {
                    scoreboard.startTrackingObjective(objective);
                }
            }
        }
    }

    public boolean isTracked(String objective) {
        return this.tracked.contains(objective.trim());
    }

}
