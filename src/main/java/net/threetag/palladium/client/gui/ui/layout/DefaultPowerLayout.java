package net.threetag.palladium.client.gui.ui.layout;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.screen.ModalScreen;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.gui.ui.background.RepeatingTextureBackground;
import net.threetag.palladium.client.gui.ui.background.SpriteBackground;
import net.threetag.palladium.client.gui.ui.background.UiBackground;
import net.threetag.palladium.client.gui.ui.component.PowerNameUiComponent;
import net.threetag.palladium.client.gui.ui.component.PowerTreeUiComponent;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.client.gui.ui.component.UiComponentProperties;
import net.threetag.palladium.client.gui.ui.screen.UiPadding;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.condition.TrueCondition;

import java.util.Optional;
import java.util.function.BiConsumer;

public class DefaultPowerLayout extends UiLayout {

    public static final MapCodec<DefaultPowerLayout> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("width", 256).forGetter(DefaultPowerLayout::getWidth),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("height", 196).forGetter(DefaultPowerLayout::getHeight),
            UiBackground.Codecs.CODEC.optionalFieldOf("background", RepeatingTextureBackground.RED_WOOL).forGetter(l -> l.treeBackground)
    ).apply(instance, DefaultPowerLayout::new));

    private final int width;
    private final int height;
    private final UiBackground background;
    private final UiBackground treeBackground;

    public DefaultPowerLayout(int width, int height, UiBackground treeBackground) {
        this.width = width;
        this.height = height;
        this.background = new SpriteBackground(ModalScreen.BACKGROUND_MODAL_HEADER);
        this.treeBackground = treeBackground;
    }

    @Override
    public UiLayoutSerializer<?> getSerializer() {
        return UiLayoutSerializers.POWER_DEFAULT;
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
        this.background.render(guiGraphics, x, y, this.width, this.height);
    }

    @Override
    public void addComponents(UiScreen screen, int x, int y, BiConsumer<UiComponent, AbstractWidget> consumer) {
        var padding = UiPadding.SEVEN;
        var rectangle = new ScreenRectangle(
                x + padding.left(),
                y + padding.top(),
                this.getWidth() - padding.horizontal(),
                this.getHeight() - padding.vertical());

        var title = new PowerNameUiComponent(null, UiComponentProperties.withSize(rectangle.width(), 10));
        consumer.accept(title, title.buildWidget(screen, rectangle));

        var tree = new PowerTreeUiComponent(null, this.treeBackground, new UiComponentProperties(
                UiAlignment.BOTTOM_CENTER, 0, 0, this.width - padding.vertical(), 196 - 20 - padding.bottom(), Optional.empty(), Optional.empty(), TrueCondition.INSTANCE
        ));
        consumer.accept(tree, tree.buildWidget(screen, rectangle));
    }

    public static class Serializer extends UiLayoutSerializer<DefaultPowerLayout> {

        @Override
        public MapCodec<DefaultPowerLayout> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiLayout, DefaultPowerLayout> builder, HolderLookup.Provider provider) {
            builder.setName("Default Power Layout")
                    .setDescription("The default layout for power screens")
                    .addOptional("width", TYPE_POSITIVE_INT, "Width of this layout", 256)
                    .addOptional("height", TYPE_POSITIVE_INT, "Height of this layout", 196)
                    .addOptional("background", TYPE_UI_BACKGROUND, "The background that is drawn for the power tree.", RepeatingTextureBackground.RED_WOOL);
        }
    }
}
