package net.threetag.palladium.entity.data;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
@EventBusSubscriber(modid = Palladium.MOD_ID)
public abstract class PalladiumEntityData<T extends Entity, R extends PalladiumEntityData<T, ?>> {

    private T entity;

    @SuppressWarnings("unchecked")
    public void setEntity(Entity entity) {
        this.entity = (T) entity;
    }

    public abstract MapCodec<R> codec();

    public void tick() {
    }

    public T getEntity() {
        return this.entity;
    }

    public RegistryAccess registryAccess() {
        return this.entity.registryAccess();
    }

    public boolean copyOnDeath() {
        return true;
    }

    public void copyFrom(PalladiumEntityData<T, R> source) {

    }

    @SuppressWarnings("unchecked")
    public static <T extends PalladiumEntityData<?, T>> T get(Entity entity, PalladiumEntityDataType<T> type) {
        return (T) ((PalladiumEntityExtension) entity).palladium$getDataMap().get(type);
    }

    public static <T extends PalladiumEntityData<?, T>> Optional<T> opt(Entity entity, PalladiumEntityDataType<T> type) {
        var data = get(entity, type);
        return Optional.ofNullable(data);
    }

    @SubscribeEvent
    static void tickPost(EntityTickEvent.Post e) {
        for (PalladiumEntityDataType type : e.getEntity().registryAccess().lookupOrThrow(PalladiumRegistryKeys.ENTITY_DATA_TYPE)) {
            var data = get(e.getEntity(), type);

            if (data != null) {
                data.tick();
            }
        }
    }

    @SubscribeEvent
    static void playerClone(net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone e) {
        for (PalladiumEntityDataType type : PalladiumRegistries.ENTITY_DATA_TYPE) {
            PalladiumEntityData oldData = get(e.getOriginal(), type);

            if (oldData != null && (!e.isWasDeath() || oldData.copyOnDeath())) {
                PalladiumEntityData newData = get(e.getEntity(), type);

                if (newData != null) {
                    newData.copyFrom(oldData);
                }
            }
        }
    }
}
