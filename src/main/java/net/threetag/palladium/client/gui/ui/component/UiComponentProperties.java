package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.dialog.action.Action;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.TrueCondition;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record UiComponentProperties(UiAlignment alignment, int x, int y, int width, int height, Optional<Action> action,
                                    Optional<Component> tooltip, Condition visibility) {

    public static final UiComponentProperties DEFAULT = withSize(50, 18);
    public static final UiComponentProperties DEFAULT_16X16 = withSize(16, 16);

    public static final Codec<UiComponentProperties> CODEC = codecWithDefaultSize(50, 18);
    public static final Codec<UiComponentProperties> CODEC_16X16 = codecWithDefaultSize(16, 16);

    public static Codec<UiComponentProperties> codecWithDefaultSize(int width, int height) {
        return RecordCodecBuilder.create(instance -> instance.group(
                UiAlignment.CODEC.optionalFieldOf("alignment", UiAlignment.TOP_LEFT).forGetter(UiComponentProperties::alignment),
                Codec.INT.optionalFieldOf("x", 0).forGetter(UiComponentProperties::x),
                Codec.INT.optionalFieldOf("y", 0).forGetter(UiComponentProperties::y),
                Codec.INT.optionalFieldOf("width", width).forGetter(UiComponentProperties::width),
                Codec.INT.optionalFieldOf("height", height).forGetter(UiComponentProperties::height),
                Action.CODEC.optionalFieldOf("action").forGetter(UiComponentProperties::action),
                ComponentSerialization.CODEC.optionalFieldOf("tooltip").forGetter(UiComponentProperties::tooltip),
                Condition.CODEC.optionalFieldOf("visibility", TrueCondition.INSTANCE).forGetter(UiComponentProperties::visibility)
        ).apply(instance, UiComponentProperties::new));
    }

    public static UiComponentProperties alignWithSize(UiAlignment alignment, int width, int height) {
        return new UiComponentProperties(
                alignment, 0, 0, width, height, Optional.empty(), Optional.empty(), TrueCondition.INSTANCE
        );
    }

    public static UiComponentProperties withSize(int width, int height) {
        return new UiComponentProperties(
                UiAlignment.TOP_LEFT, 0, 0, width, height, Optional.empty(), Optional.empty(), TrueCondition.INSTANCE
        );
    }

    @Override
    public @NonNull String toString() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow().toString();
    }
}
