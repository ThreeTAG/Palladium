package net.threetag.palladium.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class PerspectiveValue<T> {

    private final T firstPerson;
    private final T thirdPerson;

    public PerspectiveValue(T value) {
        this.firstPerson = this.thirdPerson = value;
    }

    public PerspectiveValue(T firstPerson, T thirdPerson) {
        this.firstPerson = firstPerson;
        this.thirdPerson = thirdPerson;
    }

    public T getFirstPerson() {
        return this.firstPerson;
    }

    public T getThirdPerson() {
        return this.thirdPerson;
    }

    public T get(boolean firstPerson) {
        return firstPerson ? this.getFirstPerson() : this.getThirdPerson();
    }

    @Environment(EnvType.CLIENT)
    public T get(CameraType cameraType) {
        return this.get(cameraType == CameraType.FIRST_PERSON);
    }

    @Environment(EnvType.CLIENT)
    public T get() {
        return this.get(Minecraft.getInstance().options.getCameraType());
    }

    @Environment(EnvType.CLIENT)
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
        if (!(o instanceof PerspectiveValue<?> that)) return false;
        return Objects.equals(firstPerson, that.firstPerson) && Objects.equals(thirdPerson, that.thirdPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPerson, thirdPerson);
    }

    @Override
    public String toString() {
        return "PerspectiveValue{" +
                "firstPerson=" + firstPerson +
                ", thirdPerson=" + thirdPerson +
                '}';
    }

    public static <T> Codec<PerspectiveValue<T>> codec(Codec<T> typeCodec) {
        Codec<PerspectiveValue<T>> recordCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        typeCodec.fieldOf("first_person").forGetter(PerspectiveValue::getFirstPerson),
                        typeCodec.fieldOf("third_person").forGetter(PerspectiveValue::getThirdPerson)
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
