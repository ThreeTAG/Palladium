package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;

public final class IconUiComponent extends RenderableUiComponent {

    public static final MapCodec<IconUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Icon.CODEC.fieldOf("icon").forGetter(IconUiComponent::icon),
            propertiesCodec16X16()
    ).apply(instance, IconUiComponent::new));

    private final Icon icon;

    public IconUiComponent(Icon icon, UiComponentProperties properties) {
        super(properties);
        this.icon = icon;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment) {
        IconRenderer.drawIcon(this.icon, minecraft, gui, DataContext.forEntity(minecraft.player), x, y);
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.ICON;
    }

    public Icon icon() {
        return icon;
    }

    public static class Serializer extends UiComponentSerializer<IconUiComponent> {

        @Override
        public MapCodec<IconUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, IconUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Icon")
                    .setDescription("Renders an icon")
                    .add("icon", TYPE_ICON, "The icon to be rendered");
        }
    }
}
