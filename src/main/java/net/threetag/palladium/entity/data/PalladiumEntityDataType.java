package net.threetag.palladium.entity.data;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public record PalladiumEntityDataType<T extends PalladiumEntityData<? extends Entity, ?>>(Codec<T> codec,
                                                                                          Predicate<Entity> predicate) {

    public static final Predicate<Entity> FILTER_ALL = (en) -> true;
    public static final Predicate<Entity> FILTER_LIVING = (en) -> en instanceof LivingEntity;
    public static final Predicate<Entity> FILTER_PLAYER = (en) -> en instanceof Player;
    public static final Predicate<Entity> FILTER_AVATAR = (en) -> en instanceof Avatar;

    public PalladiumEntityDataType(Codec<T> codec) {
        this(codec, FILTER_ALL);
    }
}
