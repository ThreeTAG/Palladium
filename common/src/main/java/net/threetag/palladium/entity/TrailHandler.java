package net.threetag.palladium.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.trail.TrailRenderer;
import net.threetag.palladium.client.renderer.trail.TrailRendererManager;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.TrailAbility;

import java.util.*;
import java.util.stream.Collectors;

public class TrailHandler {

    private final LivingEntity entity;
    private Map<TrailRenderer<?>, List<TrailSegmentEntity<?>>> trails = new HashMap<>();

    public TrailHandler(LivingEntity entity) {
        this.entity = entity;
    }

    public void tick() {
        var active = AbilityUtil.getEnabledEntries(this.entity, Abilities.TRAIL.get()).stream().map(e -> TrailRendererManager.INSTANCE.getRenderer(e.getProperty(TrailAbility.TRAIL_RENDERER_ID))).filter(Objects::nonNull).distinct().toList();
        Map<TrailRenderer<?>, List<TrailSegmentEntity<?>>> toChange = new HashMap<>(this.trails);

        for (TrailRenderer<?> renderer : active) {
            this.trails.putIfAbsent(renderer, new LinkedList<>());
        }

        for (Map.Entry<TrailRenderer<?>, List<TrailSegmentEntity<?>>> entry : this.trails.entrySet()) {
            var renderer = entry.getKey();
            List<TrailSegmentEntity<?>> trails = entry.getValue();

            if (!trails.isEmpty()) {
                var last = trails.get(trails.size() - 1);

                if (last.position().distanceTo(this.entity.position()) >= entity.getBbWidth() * renderer.getSpacing()) {
                    trails.add(this.spawnEntity(renderer));
                }

                trails = trails.stream().filter(LivingEntity::isAlive).collect(Collectors.toList());
            } else if (this.entity.xo != this.entity.getX() || this.entity.yo != this.entity.getY() || this.entity.zo != this.entity.getZ()) {
                trails.add(this.spawnEntity(renderer));
            }

            if (!active.contains(renderer) && trails.isEmpty()) {
                toChange.remove(renderer);
            } else {
                toChange.put(renderer, trails);
            }
        }

        this.trails = toChange;
    }

    private TrailSegmentEntity<?> spawnEntity(TrailRenderer<?> trailRenderer) {
        var entity = new TrailSegmentEntity<>(this.entity, trailRenderer);
        Objects.requireNonNull(Minecraft.getInstance().level).putNonPlayerEntity(0, entity);
        return entity;
    }

    public Map<TrailRenderer<?>, List<TrailSegmentEntity<?>>> getTrails() {
        return this.trails;
    }
}
