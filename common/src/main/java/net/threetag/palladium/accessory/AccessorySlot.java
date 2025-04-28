package net.threetag.palladium.accessory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.phys.Vec3;

public record AccessorySlot(Component name, Preview preview) {

    public static final Codec<AccessorySlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(AccessorySlot::name),
            Preview.CODEC.optionalFieldOf("preview", Preview.DEFAULT).forGetter(AccessorySlot::preview)
    ).apply(instance, (component, preview1) -> {
        System.out.println("HALLO ----");
        System.out.println(preview1.button.scale.toString());
        System.out.println(preview1.player.scale.toString());
        return new AccessorySlot(component, preview1);
    }));

    public record Preview(Orientation button, Orientation player) {

        public static final Preview DEFAULT = new Preview(Orientation.DEFAULT, Orientation.DEFAULT);

        private static final Codec<Preview> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Orientation.CODEC.optionalFieldOf("button", Orientation.DEFAULT).forGetter(Preview::button),
                Orientation.CODEC.optionalFieldOf("player", Orientation.DEFAULT).forGetter(Preview::player)
        ).apply(instance, Preview::new));

//        public static final Codec<Preview> CODEC = Codec.either(Orientation.CODEC, DIRECT_CODEC).xmap(
//                either -> either.map(o -> new Preview(o, o), preview -> preview),
//                preview -> preview.button.scale.equals(preview.player.scale)
//                        && preview.button.translation.equals(preview.player.translation)
//                        && preview.button.rotation.equals(preview.player.rotation) ?
//                        Either.left(preview.button) : Either.right(preview)
//        );

        public static final Codec<Preview> CODEC = Codec.withAlternative(DIRECT_CODEC, Orientation.CODEC.xmap(orientation -> new Preview(orientation, orientation), preview1 -> preview1.button));
    }

    public record Orientation(Vec3 scale, Vec3 translation, Vec3 rotation) {

        public static final Orientation DEFAULT = new Orientation(new Vec3(1, 1, 1), Vec3.ZERO, new Vec3(15, 40, 0));

        public static final Codec<Orientation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Vec3.CODEC.optionalFieldOf("scale", DEFAULT.scale).forGetter(Orientation::scale),
                Vec3.CODEC.optionalFieldOf("translation", Vec3.ZERO).forGetter(Orientation::translation),
                Vec3.CODEC.optionalFieldOf("rotation", DEFAULT.rotation).forGetter(Orientation::rotation)
        ).apply(instance, Orientation::new));

    }

    public static Preview preview(float scale, float translationX, float translationY, float translationZ, float rotationX, float rotationY, float rotationZ) {
        var orientation = new Orientation(new Vec3(scale, scale, scale), new Vec3(translationX, translationY, translationZ), new Vec3(rotationX, rotationY, rotationZ));
        return new Preview(orientation, orientation);
    }

    public static Orientation orientation(float scale, float translationX, float translationY, float translationZ, float rotationX, float rotationY, float rotationZ) {
        return new Orientation(new Vec3(scale, scale, scale), new Vec3(translationX, translationY, translationZ), new Vec3(rotationX, rotationY, rotationZ));
    }

}
