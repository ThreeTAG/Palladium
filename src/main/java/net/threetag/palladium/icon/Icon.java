package net.threetag.palladium.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.IdentifierException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public interface Icon {

    Codec<Icon> DIRECT_CODEC = PalladiumRegistries.ICON_SERIALIZER.byNameCodec().dispatch(Icon::getSerializer, IconSerializer::codec);

    Codec<Icon> CODEC = Codec.withAlternative(
            DIRECT_CODEC,
            Codec.STRING.comapFlatMap(Icon::read, Icon::toSimpleString).stable()
    );

    StreamCodec<RegistryFriendlyByteBuf, Icon> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.ICON_SERIALIZER).dispatch(Icon::getSerializer, IconSerializer::streamCodec);

    static Icon parse(String input) {
        if (input.endsWith(".png")) {
            return new TexturedIcon(Identifier.parse(input));
        } else if (input.startsWith("#")) {
            return new TexturedIcon(TextureReference.parse(input));
        } else {
            Identifier id = Identifier.parse(input);

            if (BuiltInRegistries.ITEM.containsKey(id)) {
                return new ItemIcon(BuiltInRegistries.ITEM.getValue(id));
            }

            return new SpriteIcon(id);
        }
    }

    static DataResult<Icon> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (IdentifierException e) {
            return DataResult.error(() -> "Not a valid texture reference: " + path + " " + e.getMessage());
        }
    }

    static String toSimpleString(Icon icon) {
        return switch (icon) {
            case ItemIcon itemIcon -> BuiltInRegistries.ITEM.getKey(itemIcon.stack().getItem()).toString();
            case TexturedIcon(TextureReference texture, java.awt.Color tint) when tint == null -> texture.toString();
            case SpriteIcon(Identifier sprite) -> sprite.toString();
            case null, default -> "invalid";
        };
    }

    IconSerializer<?> getSerializer();

}
