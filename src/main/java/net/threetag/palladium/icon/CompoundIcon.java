package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Arrays;
import java.util.List;

public record CompoundIcon(List<Icon> icons) implements Icon {

    public static final MapCodec<CompoundIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Icon.CODEC.listOf().fieldOf("icons").forGetter(CompoundIcon::icons))
            .apply(instance, CompoundIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompoundIcon> STREAM_CODEC = StreamCodec.composite(
            Icon.STREAM_CODEC.apply(ByteBufCodecs.list()), CompoundIcon::icons, CompoundIcon::new
    );

    @Override
    public IconSerializer<CompoundIcon> getSerializer() {
        return IconSerializers.COMPOUND.get();
    }

    public static class Serializer extends IconSerializer<CompoundIcon> {

        @Override
        public MapCodec<CompoundIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CompoundIcon> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Icon, CompoundIcon> builder, HolderLookup.Provider provider) {
            builder.setName("Compound").setDescription("Fits multiple icons into one")
                    .add("icons", TYPE_ICON_LIST, "Array of icons that will be displayed")
                    .addExampleObject(new CompoundIcon(Arrays.asList(new ItemIcon(Items.APPLE), new SpriteIcon(Identifier.fromNamespaceAndPath("example", "sprite")))));
        }
    }
}
