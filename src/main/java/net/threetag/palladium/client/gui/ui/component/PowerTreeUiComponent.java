package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.client.gui.widget.PowerTreeWidget;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.gui.ui.screen.UiScreenBackground;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import javax.annotation.Nullable;
import java.util.Optional;

public class PowerTreeUiComponent extends UiComponent {

    public static final MapCodec<PowerTreeUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(PalladiumRegistryKeys.POWER).optionalFieldOf("power").forGetter(c -> Optional.ofNullable(c.power)),
            UiScreen.BACKGROUND_CODEC.optionalFieldOf("background", UiScreenBackground.RepeatingTexture.RED_WOOL).forGetter(c -> c.background),
            propertiesCodec()
    ).apply(instance, (p, b, props) -> new PowerTreeUiComponent(p.orElse(null), b, props)));

    @Nullable
    private final ResourceKey<Power> power;
    private final UiScreenBackground background;

    public PowerTreeUiComponent(@org.jspecify.annotations.Nullable ResourceKey<Power> power, UiScreenBackground background, UiComponentProperties properties) {
        super(properties);
        this.power = power;
        this.background = background;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.POWER_TREE;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen) {
        PowerInstance powerInstance = this.power != null ? PowerUtil.getPowerHandler(screen.getMinecraft().player).getPowerInstance(this.power.identifier()) : null;
        // TODO from power screen

        return new PowerTreeWidget(
                screen,
                powerInstance,
                this.background,
                this.getX(screen.getInnerRectangle()),
                this.getY(screen.getInnerRectangle()),
                this.getWidth(),
                this.getHeight());
    }

    public static class Serializer extends UiComponentSerializer<PowerTreeUiComponent> {

        @Override
        public MapCodec<PowerTreeUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, PowerTreeUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Power Tree")
                    .setDescription("Renders an interactable tree for a power of a power")
                    .addOptional("power", TYPE_POWER, "The power that will be displayed. If none is specified, it will use the from the current power screen (if this component is used in one).");
        }
    }
}
