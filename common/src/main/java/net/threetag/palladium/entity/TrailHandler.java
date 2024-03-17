package net.threetag.palladium.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.trail.CompoundTrailRenderer;
import net.threetag.palladium.client.renderer.trail.TrailRenderer;
import net.threetag.palladium.client.renderer.trail.TrailRendererManager;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.TrailAbility;

import java.util.*;
import java.util.stream.Collectors;

public class TrailHandler {

    private final Entity entity;
    private Map<TrailRenderer<?>, List<TrailSegmentEntity<?>>> trails = new HashMap<>();

    public TrailHandler(Entity entity) {
        this.entity = entity;
    }

    public void tick() {
        var active = getTrailRenderersFor(this.entity);

        for (TrailRenderer<?> renderer : active) {
            this.trails.putIfAbsent(renderer, new LinkedList<>());
        }

        Map<TrailRenderer<?>, List<TrailSegmentEntity<?>>> toChange = new HashMap<>(this.trails);

        for (Map.Entry<TrailRenderer<?>, List<TrailSegmentEntity<?>>> entry : this.trails.entrySet()) {
            var renderer = entry.getKey();
            List<TrailSegmentEntity<?>> trails = entry.getValue();

            if (!trails.isEmpty()) {
                if (active.contains(renderer)) {
                    var last = trails.get(trails.size() - 1);

                    if (last.position().distanceTo(this.entity.position()) >= entity.getBbWidth() * renderer.getSpacing()) {
                        trails.add(this.spawnEntity(renderer));
                    }
                }

                trails = trails.stream().filter(LivingEntity::isAlive).collect(Collectors.toList());
            } else if (active.contains(renderer) && this.isMoving()) {
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

    private boolean isMoving() {
        if (this.entity instanceof LivingEntity) {
            return this.entity.xo != this.entity.getX() || this.entity.yo != this.entity.getY() || this.entity.zo != this.entity.getZ();
        } else {
            return this.entity.getDeltaMovement().length() != 0F;
        }
    }

    public static List<TrailRenderer<?>> getTrailRenderersFor(Entity entity) {
        List<TrailRenderer<?>> renderers = new ArrayList<>();

        if (!entity.isAlive()) {
            return renderers;
        }

        if (entity instanceof LivingEntity living) {
            for (TrailRenderer<?> renderer : AbilityUtil.getEnabledEntries(living, Abilities.TRAIL.get()).stream().map(e -> TrailRendererManager.INSTANCE.getRenderer(e.getProperty(TrailAbility.TRAIL_RENDERER_ID))).filter(Objects::nonNull).distinct().toList()) {
                addTrailToList(renderer, renderers);
            }
        }

        if (entity instanceof CustomProjectile projectile) {
            for (CustomProjectile.Appearance appearance : projectile.appearances) {
                if (appearance instanceof CustomProjectile.TrailAppearance trailAppearance) {
                    for (ResourceLocation trailId : trailAppearance.trails) {
                        var trailRenderer = TrailRendererManager.INSTANCE.getRenderer(trailId);

                        if (trailRenderer != null && !renderers.contains(trailRenderer)) {
                            renderers.add(trailRenderer);
                        }
                    }
                }
            }
        }

        return renderers;
    }

    private static void addTrailToList(TrailRenderer<?> trailRenderer, List<TrailRenderer<?>> trailRendererList) {
        if (trailRenderer instanceof CompoundTrailRenderer compound) {
            for (TrailRenderer<?> renderer : compound.getTrailRenderers()) {
                addTrailToList(renderer, trailRendererList);
            }
        } else if (!trailRendererList.contains(trailRenderer)) {
            trailRendererList.add(trailRenderer);
        }
    }
}
