package net.threetag.palladium.power.ability;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;

public class AbilityDescription {

    public static final AbilityDescription EMPTY = new AbilityDescription(Component.empty());

    private static final Codec<AbilityDescription> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("locked").forGetter(AbilityDescription::getLockedDescription),
                    ComponentSerialization.CODEC.fieldOf("unlocked").forGetter(AbilityDescription::getUnlockedDescription)
            ).apply(instance, AbilityDescription::new));

    public static final Codec<AbilityDescription> CODEC = Codec.either(
            DIRECT_CODEC,
            ComponentSerialization.CODEC
    ).xmap(
            either -> either.map(
                    left -> left,
                    AbilityDescription::new
            ),
            desc -> desc.isSimple() ? Either.right(desc.getLockedDescription()) : Either.left(desc)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityDescription> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, AbilityDescription::getLockedDescription,
            ComponentSerialization.STREAM_CODEC, AbilityDescription::getUnlockedDescription,
            AbilityDescription::new
    );

    private final Component lockedDescription;
    private final Component unlockedDescription;
    private final boolean simple;

    public AbilityDescription(Component lockedDescription, Component unlockedDescription) {
        this.lockedDescription = lockedDescription;
        this.unlockedDescription = unlockedDescription;
        this.simple = false;
    }

    public AbilityDescription(Component description) {
        this.lockedDescription = this.unlockedDescription = description;
        this.simple = true;
    }

    public Component getLockedDescription() {
        return this.lockedDescription;
    }

    public Component getUnlockedDescription() {
        return this.unlockedDescription;
    }

    public boolean isSimple() {
        return this.simple;
    }

    public Component get(boolean unlocked) {
        return unlocked ? this.unlockedDescription : this.lockedDescription;
    }

}
