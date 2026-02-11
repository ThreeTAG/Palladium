package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.client.gui.screen.power.PowerUiScreen;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.gui.ui.layout.UiBackground;
import net.threetag.palladium.client.gui.widget.PowerTreeWidget;
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
            UiScreen.BACKGROUND_CODEC.optionalFieldOf("background", UiBackground.RepeatingTexture.RED_WOOL).forGetter(c -> c.background),
            propertiesCodec()
    ).apply(instance, (p, b, props) -> new PowerTreeUiComponent(p.orElse(null), b, props)));

    @Nullable
    private final ResourceKey<Power> power;
    private final UiBackground background;

    public PowerTreeUiComponent(@Nullable ResourceKey<Power> power, UiBackground background, UiComponentProperties properties) {
        super(properties);
        this.power = power;
        this.background = background;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.POWER_TREE;
    }

    @Override
    public AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle) {
        PowerInstance powerInstance = null;

        if (this.power != null) {
            powerInstance = PowerUtil.getPowerHandler(screen.getMinecraft().player).getPowerInstance(this.power.identifier());
        } else if (screen instanceof PowerUiScreen powerUiScreen) {
            powerInstance = powerUiScreen.getPowerInstance();
        }

        return new PowerTreeWidget(
                screen,
                powerInstance,
                this.background,
                this.getX(rectangle),
                this.getY(rectangle),
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
                    .addOptional("power", TYPE_POWER, "The power that will be displayed. If none is specified, it will use the from the current power screen (if this component is used in one).")
                    .addOptional("background", TYPE_UI_BACKGROUND, "The background that is drawn for the power tree.", UiBackground.RepeatingTexture.RED_WOOL.toString());
        }
    }
}
