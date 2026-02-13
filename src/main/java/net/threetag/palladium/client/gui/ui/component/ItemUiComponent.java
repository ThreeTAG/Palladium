package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PalladiumCodecs;

public class ItemUiComponent extends UiComponent {

    public static final MapCodec<ItemUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.SIMPLE_ITEM_STACK.fieldOf("item").forGetter(c -> c.itemStack),
            Codec.BOOL.optionalFieldOf("decorations", true).forGetter(c -> c.decorations),
            Codec.BOOL.optionalFieldOf("tooltip", true).forGetter(c -> c.tooltip),
            propertiesCodec16X16()
    ).apply(instance, ItemUiComponent::new));

    private final ItemStack itemStack;
    private final boolean decorations;
    private final boolean tooltip;

    public ItemUiComponent(ItemStack itemStack, boolean decorations, boolean tooltip, UiComponentProperties properties) {
        super(properties);
        this.itemStack = itemStack;
        this.decorations = decorations;
        this.tooltip = tooltip;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.ITEM;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle) {
        var widget = new ItemDisplayWidget(
                screen.getMinecraft(),
                0, 0,
                this.getWidth(), this.getHeight(),
                this.itemStack.getHoverName(),
                this.itemStack,
                this.decorations,
                this.tooltip
        );

        widget.setPosition(this.getX(rectangle), this.getY(rectangle));

        if (!this.tooltip) {
            this.getProperties().tooltip().ifPresent(component -> widget.setTooltip(Tooltip.create(component)));
        }

        return widget;
    }

    public static class Serializer extends UiComponentSerializer<ItemUiComponent> {

        @Override
        public MapCodec<ItemUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, ItemUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Item")
                    .setDescription("Renders an item")
                    .add("item", TYPE_ITEM_STACK, "The item stack to be rendered")
                    .addOptional("decorations", TYPE_BOOLEAN, "Whether or not item stack \"decorations\" should be drawn (count, damage, etc.).", true)
                    .addOptional("tooltip", TYPE_BOOLEAN, "Whether or not the default tooltip for the item information should be rendered. If enabled, this will override the tooltip of the properties.", true);
        }
    }
}
