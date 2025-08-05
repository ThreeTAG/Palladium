package net.threetag.palladium.entity.data;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class PalladiumEntityDataType<T extends PalladiumEntityData<? extends Entity, ?>> {

    public static final Predicate<Entity> FILTER_ALL = (en) -> true;
    public static final Predicate<Entity> FILTER_LIVING = (en) -> en instanceof LivingEntity;
    public static final Predicate<Entity> FILTER_PLAYER = (en) -> en instanceof Player;

    private final MapCodec<T> codec;
    private final Predicate<Entity> predicate;

    public PalladiumEntityDataType(MapCodec<T> codec, Predicate<Entity> predicate) {
        this.codec = codec;
        this.predicate = predicate;
    }

    public PalladiumEntityDataType(MapCodec<T> codec) {
        this(codec, FILTER_ALL);
    }

    public MapCodec<T> codec() {
        return this.codec;
    }

    public Predicate<Entity> getPredicate() {
        return this.predicate;
    }
}
