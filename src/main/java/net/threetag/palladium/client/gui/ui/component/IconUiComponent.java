package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;

public record IconUiComponent(Icon icon, UiComponentPosition position) implements RenderableUiComponent {

    public static final MapCodec<IconUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Icon.CODEC.fieldOf("icon").forGetter(IconUiComponent::icon),
            UiComponentPosition.CODEC.optionalFieldOf("position", UiComponentPosition.TOP_LEFT).forGetter(IconUiComponent::position)
    ).apply(instance, IconUiComponent::new));

    public IconUiComponent(Icon icon) {
        this(icon, UiComponentPosition.TOP_LEFT);
    }

    @Override
    public UiComponentPosition getPosition() {
        return this.position();
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, UiAlignment alignment) {
        IconRenderer.drawIcon(this.icon, minecraft, gui, DataContext.forEntity(minecraft.player), x, y);
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.ICON;
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
                    .add("icon", TYPE_ICON, "The icon to be rendered")
                    .addOptional("position", TYPE_UI_POSITION, "Position of this component", new int[] {0, 0});
        }
    }
}
