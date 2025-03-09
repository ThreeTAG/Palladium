package net.threetag.palladium.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.core.event.PalladiumEntityEvents;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Optional;

public abstract class PalladiumEntityData<T extends Entity> {

    private final T entity;

    protected PalladiumEntityData(T entity) {
        this.entity = entity;
    }

    public abstract void load(CompoundTag nbt, HolderLookup.Provider registryLookup);

    public abstract CompoundTag save(HolderLookup.Provider registryLookup);

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

    @SuppressWarnings("unchecked")
    public static <T extends PalladiumEntityData<?>> T get(Entity entity, PalladiumEntityDataType<T> type) {
        return (T) ((PalladiumEntityExtension) entity).palladium$getDataMap().get(type);
    }

    public static <T extends PalladiumEntityData<?>> Optional<T> opt(Entity entity, PalladiumEntityDataType<T> type) {
        var data = get(entity, type);
        return Optional.ofNullable(data);
    }

    public static void registerEvents() {
        PalladiumEntityEvents.TICK_POST.register(entity -> {
            for (PalladiumEntityDataType<?> type : entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.ENTITY_DATA_TYPE)) {
                var data = get(entity, type);

                if (data != null) {
                    data.tick();
                }
            }
        });

        PalladiumLifecycleEvents.DATA_PACK_SYNC.register((playerList, pl) -> {
            if (pl == null) {
                for (PalladiumEntityDataType<?> type : PalladiumRegistries.ENTITY_DATA_TYPE) {
                    for (ServerPlayer player : playerList.getPlayers()) {
                        var data = PalladiumEntityData.get(player, type);

                        if (data != null) {
                            data.onReload();
                        }
                    }
                }
            }
        });
    }

}
