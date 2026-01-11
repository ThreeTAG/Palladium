package net.threetag.palladium.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

import java.util.Collections;
import java.util.Optional;

public class CreativeModeTabCodec {

    public static final Codec<CreativeModeTab> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(CreativeModeTab::getDisplayName),
            Identifier.CODEC.fieldOf("icon").forGetter(tab -> BuiltInRegistries.ITEM.getKey(tab.getIconItem().getItem())),
            Identifier.CODEC.optionalFieldOf("background_texture").forGetter(tab -> Optional.of(tab.getBackgroundTexture())),
            Identifier.CODEC.listOf().optionalFieldOf("items", Collections.emptyList()).forGetter(tab -> tab.getDisplayItems().stream().map(item -> BuiltInRegistries.ITEM.getKey(item.getItem())).toList())
    ).apply(instance, (name, icon, bg, items) -> {
        var builder = CreativeModeTab.builder()
                .title(name)
                .icon(() -> BuiltInRegistries.ITEM.getValue(icon).getDefaultInstance());

        bg.ifPresent(builder::backgroundTexture);
        builder.displayItems((parameters, output) -> items.stream().map(BuiltInRegistries.ITEM::getValue).forEach(output::accept));

        return builder.build();
    }));

}
