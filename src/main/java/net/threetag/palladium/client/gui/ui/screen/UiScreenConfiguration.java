package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.ui.component.UiComponent;

import java.util.Collections;
import java.util.List;

public record UiScreenConfiguration(int width, int height, int padding, UiScreenBackground background, List<UiComponent> components) {

    public static final Codec<UiScreenConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(UiScreenConfiguration::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(UiScreenConfiguration::height),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("padding", 7).forGetter(UiScreenConfiguration::padding),
            UiScreenBackground.CODEC.optionalFieldOf("background", UiScreenBackground.DEFAULT).forGetter(UiScreenConfiguration::background),
            UiComponent.CODEC.listOf().optionalFieldOf("components", Collections.emptyList()).forGetter(UiScreenConfiguration::components)
    ).apply(instance, UiScreenConfiguration::new));

    public void open() {
        Minecraft.getInstance().setScreen(new UiScreen(this));
    }

}
