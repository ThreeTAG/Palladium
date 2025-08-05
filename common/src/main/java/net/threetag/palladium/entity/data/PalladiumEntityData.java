package net.threetag.palladium.entity.data;

import com.mojang.serialization.MapCodec;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.threetag.palladium.core.event.PalladiumEntityEvents;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Optional;

public abstract class PalladiumEntityData<T extends Entity, R extends PalladiumEntityData<T, ?>> {

    private T entity;

    @SuppressWarnings("unchecked")
    public void setEntity(Entity entity) {
        this.entity = (T) entity;
    }

    public abstract MapCodec<R> codec();

    public void tick() {
    }

    public void onReload() {
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerEvents() {
        PalladiumEntityEvents.TICK_POST.register(entity -> {
            for (PalladiumEntityDataType type : entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.ENTITY_DATA_TYPE)) {
                var data = get(entity, type);

                if (data != null) {
                    data.tick();
                }
            }
        });

        PalladiumLifecycleEvents.DATA_PACK_SYNC.register((playerList, pl) -> {
            if (pl == null) {
                for (PalladiumEntityDataType type : PalladiumRegistries.ENTITY_DATA_TYPE) {
                    for (ServerPlayer player : playerList.getPlayers()) {
                        var data = PalladiumEntityData.get(player, type);

                        if (data != null) {
                            data.onReload();
                        }
                    }
                }
            }
        });

        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            for (PalladiumEntityDataType type : PalladiumRegistries.ENTITY_DATA_TYPE) {
                PalladiumEntityData oldData = get(oldPlayer, type);

                if (oldData != null && (wonGame || oldData.copyOnDeath())) {
                    PalladiumEntityData newData = get(newPlayer, type);

                    if (newData != null) {
                        newData.copyFrom(oldData);
                    }
                }
            }
        });
    }

}
