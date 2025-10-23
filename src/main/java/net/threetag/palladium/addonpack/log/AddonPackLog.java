package net.threetag.palladium.addonpack.log;

import net.threetag.palladium.Palladium;
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

}
