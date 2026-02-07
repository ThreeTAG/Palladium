package net.threetag.palladium.client.gui.ui.layout;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.List;
import java.util.function.BiConsumer;

public class MultiColumnLayout extends UiLayout {

    public static final MapCodec<MultiColumnLayout> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.CODEC.listOf(1, 10).fieldOf("layouts").forGetter(l -> l.layouts),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("gap", 0).forGetter(l -> l.gap)
    ).apply(instance, MultiColumnLayout::new));

    private final List<UiLayout> layouts;
    private final int gap;
    private final int width, height;

    public MultiColumnLayout(List<UiLayout> layouts, int gap) {
        this.layouts = layouts;
        this.gap = gap;
        int w = 0;
        int h = 0;

        for (UiLayout layout : layouts) {
            w += layout.getWidth();
            h = Math.max(h, layout.getHeight());
        }

        w += (layouts.size() - 1) * gap;

        this.width = w;
        this.height = h;
    }

    @Override
    public UiLayoutSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int x, int y) {
        for (UiLayout layout : this.layouts) {
            layout.renderBackground(guiGraphics, x, y);
            x += layout.getWidth() + this.gap;
        }
    }

    @Override
    public void addComponents(UiScreen screen, int x, int y, BiConsumer<UiComponent, AbstractWidget> consumer) {
        for (UiLayout layout : this.layouts) {
            layout.addComponents(screen, x, y, consumer);
            x += layout.getWidth() + this.gap;
        }
    }

    public static class Serializer extends UiLayoutSerializer<MultiColumnLayout> {

        @Override
        public MapCodec<MultiColumnLayout> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiLayout, MultiColumnLayout> builder, HolderLookup.Provider provider) {
            builder.setName("Multi Column Layout")
                    .setDescription("Used for using multiple layouts next to each other horizontally.")
                    .add("layouts", TYPE_UI_LAYOUTS, "List of UI layouts.")
                    .addOptional("gap", TYPE_NON_NEGATIVE_INT, "Gap between each layout.", 0);
        }
    }
}
