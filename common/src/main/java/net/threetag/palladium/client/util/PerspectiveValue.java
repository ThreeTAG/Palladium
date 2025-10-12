package net.threetag.palladium.client.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record PerspectiveValue<T>(T firstPerson, T thirdPerson) {

    public PerspectiveValue(T value) {
        this(value, value);
    }

    public T get(boolean firstPerson) {
        return firstPerson ? this.firstPerson() : this.thirdPerson();
    }

    public T get(CameraType cameraType) {
        return this.get(cameraType == CameraType.FIRST_PERSON);
    }

    public T get() {
        return this.get(Minecraft.getInstance().options.getCameraType());
    }

    public T getForPlayer(Player player) {
        if (player == Minecraft.getInstance().player) {
            return this.get();
        } else {
            return this.get(false);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerspectiveValue<?>(Object fPerson, Object sPerson))) return false;
        return Objects.equals(firstPerson, fPerson) && Objects.equals(thirdPerson, sPerson);
    }

    @Override
    public @NotNull String toString() {
        return "PerspectiveValue{" +
                "firstPerson=" + firstPerson +
                ", thirdPerson=" + thirdPerson +
                '}';
    }

    public static <T> Codec<PerspectiveValue<T>> codec(Codec<T> typeCodec) {
        Codec<PerspectiveValue<T>> recordCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        typeCodec.fieldOf("first_person").forGetter(PerspectiveValue::firstPerson),
                        typeCodec.fieldOf("third_person").forGetter(PerspectiveValue::thirdPerson)
                ).apply(instance, PerspectiveValue::new));

        return Codec.either(recordCodec, typeCodec)
                .xmap(
                        either -> either.map(
                                left -> left,
                                PerspectiveValue::new
                        ),
                        value -> value.firstPerson == value.thirdPerson ? Either.right(value.firstPerson) : Either.left(value)
                );
    }

}
