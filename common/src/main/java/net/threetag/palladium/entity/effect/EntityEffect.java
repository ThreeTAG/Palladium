package net.threetag.palladium.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.function.Predicate;

public abstract class EntityEffect extends RegistryEntry<EntityEffect> {

    public static final ResourceKey<Registry<EntityEffect>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "entity_effects"));
    public static final Registrar<EntityEffect> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new EntityEffect[0]).build();

    public static final PalladiumProperty<Boolean> IS_DONE_PLAYING = new BooleanProperty("is_done_playing");

    public void registerProperties(PropertyManager manager) {
        manager.register(IS_DONE_PLAYING, false);
    }

    @Environment(EnvType.CLIENT)
    public abstract void render(EffectEntity entity, Entity anchor, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTicks);

    public abstract void tick(EffectEntity entity, Entity anchor);

    public boolean isInRangeToRenderDist(EffectEntity effectEntity, Entity anchor, double distance) {
        return anchor.shouldRenderAtSqrDistance(distance);
    }

    public <T> T get(EffectEntity entity, PalladiumProperty<T> property) {
        return EntityPropertyHandler.getHandler(entity).get(property);
    }

    public <T> void set(EffectEntity entity, PalladiumProperty<T> property, T value) {
        EntityPropertyHandler.getHandler(entity).set(property, value);
    }

    public static void start(Entity anchor, EntityEffect entityEffect) {
        if (!anchor.level.isClientSide) {
            EffectEntity effectEntity = new EffectEntity(anchor.level, anchor, entityEffect);
            anchor.level.addFreshEntity(effectEntity);
        }
    }

    public static void stop(Entity anchor, Predicate<EntityEffect> predicate) {
        if (!anchor.level.isClientSide) {
            anchor.level.getEntities(anchor, anchor.getBoundingBox().inflate(2), entity -> entity instanceof EffectEntity && ((EffectEntity) entity).getAnchorEntity() == anchor && predicate.test(((EffectEntity) entity).entityEffect)).forEach(Entity::discard);
        }
    }

    public static void stop(Entity anchor, EntityEffect entityEffectType) {
        stop(anchor, effect -> effect == entityEffectType);
    }

}
