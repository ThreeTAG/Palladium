package net.threetag.palladium.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.PoseStackTransformation;

public record AccessorySlot(Component name, PoseStackTransformation preview) {

    public static final PoseStackTransformation DEFAULT_PREVIEW = new PoseStackTransformation(new Vec3(1, 1, 1), Vec3.ZERO, new Vec3(15, 40, 0));

    public static final Codec<AccessorySlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(AccessorySlot::name),
            PoseStackTransformation.CODEC.optionalFieldOf("preview", DEFAULT_PREVIEW).forGetter(AccessorySlot::preview)
    ).apply(instance, AccessorySlot::new));


}
