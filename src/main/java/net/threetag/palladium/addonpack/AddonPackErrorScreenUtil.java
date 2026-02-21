package net.threetag.palladium.addonpack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class AddonPackErrorScreenUtil {

    public static Map<ResourceKey<?>, Exception> ERRORS = null;

    /**
     * Original: ThePotatoArchivist/DatapackErrorViewer, 2025, MIT-License <br>
     * <a href="https://github.com/ThePotatoArchivist/DatapackErrorViewer/blob/main/src/main/java/archives/tater/datapackerrors/DatapackErrorViewer.java">Repository</a>
     */
    public static FittingMultiLineTextWidget getErrorsWidget(Screen screen, Font font) {
        if (ERRORS == null) {
            return null;
        }

        return new FittingMultiLineTextWidget(screen.width / 2 - 180,
                screen.height / 2 - 60,
                360,
                140,
                ComponentUtils.formatList(ERRORS.entrySet().stream()
                                .map(entry ->
                                        Component.literal(entry.getKey().registry() + "/" + entry.getKey().identifier() + "\n")
                                                .withStyle(ChatFormatting.WHITE)
                                                .append(ComponentUtils.formatList(
                                                        iterateUntilRepeat(entry.getValue(), Throwable::getCause)
                                                                .map(throwable -> Component.literal(throwable.getMessage())
                                                                        .withStyle(ChatFormatting.GRAY))
                                                                .toList(),
                                                        Component.literal("\n")
                                                ))
                                ).toList(),
                        Component.literal("\n\n")),
                font
        );
    }

    public static <T> Stream<T> iterateUntilRepeat(T initial, UnaryOperator<T> next) {
        return Stream.iterate(initial, value -> value != null && next.apply(value) != value, next);
    }

}
