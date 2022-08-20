package net.threetag.palladium.addonpack.log;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.client.screen.AddonPackLogScreen;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;

import java.util.ArrayList;
import java.util.List;

public class AddonPackLog {

    private static final List<AddonPackLogEntry> ENTRIES = new ArrayList<>();

    public static List<AddonPackLogEntry> getEntries() {
        return ENTRIES;
    }

    public static void info(String message, Object... params) {
        Palladium.LOGGER.info(message, params);
        final Message msg = new FormattedMessage(message, params);
        ENTRIES.add(new AddonPackLogEntry(AddonPackLogEntry.Type.INFO, msg));
    }

    public static void error(String message, Object... params) {
        Palladium.LOGGER.error(message, params);
        final Message msg = new FormattedMessage(message, params);
        ENTRIES.add(new AddonPackLogEntry(AddonPackLogEntry.Type.ERROR, msg));
    }

    public static void error(Exception exception, String message, Object... params) {
        Palladium.LOGGER.error(message, params);
        final Message msg = new FormattedMessage(message + "\n" + exception.getMessage(), params);
        ENTRIES.add(new AddonPackLogEntry(AddonPackLogEntry.Type.ERROR, msg, exception.getStackTrace()));
    }

    public static void warning(String message, Object... params) {
        Palladium.LOGGER.warn(message, params);
        final Message msg = new FormattedMessage(message, params);
        ENTRIES.add(new AddonPackLogEntry(AddonPackLogEntry.Type.WARNING, msg));
    }

    public static void warning(Exception exception, String message, Object... params) {
        Palladium.LOGGER.warn(message, params);
        final Message msg = new FormattedMessage(message + "\n" + exception.getMessage(), params);
        ENTRIES.add(new AddonPackLogEntry(AddonPackLogEntry.Type.WARNING, msg, exception.getStackTrace()));
    }

    public static void setupButton() {
        ClientGuiEvent.INIT_POST.register((screen, access) -> {
            if (PalladiumConfig.addonPackDevMode() && screen instanceof TitleScreen) {
                access.addRenderableWidget(new Button(10, 10, 200, 20, new TranslatableComponent("gui.palladium.addon_pack_log"), (p_213079_1_) -> {
                    Minecraft.getInstance().setScreen(new AddonPackLogScreen(AddonPackLog.getEntries(), screen));
                }));
            }
        });
    }
}
