package net.threetag.palladium.client.trail;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.util.CollectionDelta;

import java.util.*;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class EntityTrailHandler extends PalladiumEntityData<Entity, EntityTrailHandler> {

    public static final MapCodec<EntityTrailHandler> CODEC = MapCodec.unit(EntityTrailHandler::new);

    private Map<Trail, TrailInstance> trailHandlerMap = new HashMap<>();

    @Override
    public void tick() {
        new CollectionDelta<>(new ArrayList<>(this.trailHandlerMap.keySet()), Trail.getAvailableTrailsForEntity(this.getEntity()))
                .onAdd(t -> {
                    if (this.trailHandlerMap.containsKey(t)) {
                        this.trailHandlerMap.get(t).disabled = false;
                    } else {
                        this.trailHandlerMap.put(t, new TrailInstance(t));
                    }
                })
                .onRemove(t -> this.trailHandlerMap.getOrDefault(t, new TrailInstance(t)).disabled = true)
                .apply();

        this.trailHandlerMap.values().forEach(trailInstance -> trailInstance.tick(this.getEntity()));

        for (Map.Entry<Trail, TrailInstance> e : this.trailHandlerMap.entrySet()) {
            if (e.getValue().segments.isEmpty() && e.getValue().disabled) {
                this.trailHandlerMap.remove(e.getKey());
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SubscribeEvent
    static void onRenderLiving(RenderLivingEvent.Post<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> e) {
        var renderState = e.getRenderState();

        for (TrailInstance instance : renderState.getRenderDataOrDefault(PalladiumRenderStateKeys.TRAILS, Collections.emptyMap()).values()) {
            for (TrailSegment segment : instance.segments) {
                e.getPoseStack().pushPose();
                var offset = segment.getPosition().subtract(renderState.x, renderState.y, renderState.z);
                e.getPoseStack().translate(offset);
                instance.trail.trailRenderer().submit(e.getPoseStack(), e.getSubmitNodeCollector(), segment,
                        renderState.lightCoords, renderState.partialTick);
                e.getPoseStack().popPose();
            }
        }
    }

    @Override
    public MapCodec<EntityTrailHandler> codec() {
        return CODEC;
    }

    public static EntityTrailHandler get(LivingEntity entity) {
        return PalladiumEntityData.get(entity, PalladiumEntityDataTypes.TRAIL_HANDLER.get());
    }

    public Map<Trail, TrailInstance> getTrails() {
        return this.trailHandlerMap;
    }

    public static class TrailInstance {

        private final Trail trail;
        private List<TrailSegment<?>> segments;
        private boolean disabled = false;

        public TrailInstance(Trail trail) {
            this.trail = trail;
            this.segments = new LinkedList<>();
        }

        public void tick(Entity entity) {
            if (!this.disabled) {
                if (!this.segments.isEmpty()) {
                    var last = this.segments.getLast();

                    if (last.getPosition().distanceTo(entity.position()) >= entity.getBbWidth() * this.trail.spacing()) {
                        this.addSegment(entity);
                    }
                } else if (isMoving(entity)) {
                    this.addSegment(entity);
                }
            }

            for (TrailSegment<?> segment : Set.copyOf(this.segments)) {
                if (segment.isObsolete()) {
                    this.segments.remove(segment);
                } else {
                    segment.tick();
                }
            }
        }

        private void addSegment(Entity entity) {
            var segment = new TrailSegment<>(entity, this.trail, this.trail.trailRenderer());
            this.segments.add(segment);
        }
    }

    private static boolean isMoving(Entity entity) {
        if (entity instanceof LivingEntity) {
            return entity.xo != entity.getX() || entity.yo != entity.getY() || entity.zo != entity.getZ();
        } else {
            return entity.getDeltaMovement().length() != 0F;
        }
    }
}
