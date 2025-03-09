package net.threetag.palladium.entity;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.util.PlayerUtil;

import java.util.Objects;

public class SkinTypedValue<T> {

    private final T wide;
    private final T slim;

    public SkinTypedValue(T value) {
        this.wide = this.slim = value;
    }

    public SkinTypedValue(T wide, T slim) {
        this.wide = wide;
        this.slim = slim;
    }

    public T getWide() {
        return this.wide;
    }

    public T getSlim() {
        return this.slim;
    }

    public T get(boolean slim) {
        return slim ? this.getSlim() : this.getWide();
    }

    public T get(DataContext context) {
        return this.get(context.getEntity());
    }

    public T get(Entity entity) {
        return this.get(entity instanceof Player player && PlayerUtil.hasSmallArms(player));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkinTypedValue<?> that)) return false;
        return Objects.equals(wide, that.wide) && Objects.equals(slim, that.slim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wide, slim);
    }

    @Override
    public String toString() {
        return "SkinTypedValue{" +
                "wide=" + wide +
                ", slim=" + slim +
                '}';
    }

    public static <T> Codec<SkinTypedValue<T>> codec(Codec<T> typeCodec) {
        Codec<SkinTypedValue<T>> recordCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        typeCodec.fieldOf("wide").forGetter(SkinTypedValue::getWide),
                        typeCodec.fieldOf("slim").forGetter(SkinTypedValue::getSlim)
                ).apply(instance, SkinTypedValue::new));

        return Codec.either(recordCodec, typeCodec)
                .xmap(
                        either -> either.map(
                                left -> left,
                                SkinTypedValue::new
                        ),
                        value -> value.wide == value.slim ? Either.right(value.wide) : Either.left(value)
                );
    }

}
