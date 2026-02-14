package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.client.gui.screen.power.PowerUiScreen;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.PalladiumCodecs;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class PowerNameUiComponent extends AbstractStringUiComponent {

    public static final MapCodec<PowerNameUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(PalladiumRegistryKeys.POWER).optionalFieldOf("power").forGetter(c -> Optional.ofNullable(c.power)),
            PalladiumCodecs.COLOR_INT_CODEC.optionalFieldOf("color", RenderUtil.DEFAULT_GRAY).forGetter(PowerNameUiComponent::getColor),
            Codec.BOOL.optionalFieldOf("shadow", false).forGetter(PowerNameUiComponent::hasShadow),
            TEXT_ALIGNMENT_CODEC.optionalFieldOf("alignment", TextAlignment.LEFT).forGetter(PowerNameUiComponent::getTextAlignment),
            TEXT_OVERFLOW_CODEC.optionalFieldOf("overflow", StringWidget.TextOverflow.CLAMPED).forGetter(PowerNameUiComponent::getTextOverflow),
            propertiesCodec()
    ).apply(instance, (p, c, s, a, o, props) -> new PowerNameUiComponent(p.orElse(null), c, s, a, o, props)));

    @Nullable
    private final ResourceKey<Power> power;

    public PowerNameUiComponent(ResourceKey<Power> power, int color, boolean shadow, TextAlignment alignment, StringWidget.TextOverflow textOverflow, UiComponentProperties properties) {
        super(color, shadow, alignment, textOverflow, properties);
        this.power = power;
    }

    public PowerNameUiComponent(@Nullable ResourceKey<Power> power, UiComponentProperties properties) {
        super(RenderUtil.DEFAULT_GRAY, false, TextAlignment.LEFT, StringWidget.TextOverflow.CLAMPED, properties);
        this.power = power;
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.POWER_NAME;
    }

    @Override
    public Component getText(UiScreen screen) {
        if (this.power != null) {
            return Objects.requireNonNull(screen.getMinecraft().level).registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).get(this.power).map(holder -> holder.value().getName()).orElse(Component.empty());
        } else if (screen instanceof PowerUiScreen powerUiScreen) {
            return powerUiScreen.getPowerInstance().getPower().value().getName();
        } else {
            return Component.empty();
        }
    }

    public static class Serializer extends AbstractStringUiComponentSerializer<PowerNameUiComponent> {

        @Override
        public MapCodec<PowerNameUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, PowerNameUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Power Name")
                    .setDescription("Renders the name of a power.")
                    .addOptional("power", TYPE_POWER, "The power that will be displayed. If none is specified, it will use the from the current power screen (if this component is used in one).");
        }
    }
}
