package net.threetag.palladium.addonpack.log;

import net.minecraft.ChatFormatting;
import org.apache.logging.log4j.message.Message;

import java.util.Arrays;

public class AddonPackLogEntry {

    private Type type;
    private Message msg;
    private String msgString;
    private String stacktrace;

    public AddonPackLogEntry(Type type, Message message) {
        this(type, message, null);
    }

    public AddonPackLogEntry(Type type, Message message, StackTraceElement[] stacktrace) {
        this.type = type;
        this.msg = message;
        this.stacktrace = Arrays.toString(stacktrace);
    }

    public AddonPackLogEntry(Type type, String msgString) {
        this(type, msgString, null);
    }

    public AddonPackLogEntry(Type type, String msgString, StackTraceElement[] stacktrace) {
        this.type = type;
        this.msgString = msgString;
        this.stacktrace = Arrays.toString(stacktrace);
    }

    public Type getType() {
        return this.type;
    }

    public String getText() {
        return this.msg != null ? this.msg.getFormattedMessage() : this.msgString;
    }

    public String getStacktrace() {
        return this.stacktrace;
    }

    @Override
    public String toString() {
        return "[" + this.type.toString() + "] " + (this.msg != null ? this.msg : this.msgString);
    }

    public enum Type {

        INFO(ChatFormatting.RESET),
        WARNING(ChatFormatting.YELLOW),
        ERROR(ChatFormatting.RED);

        private final ChatFormatting color;

        Type(ChatFormatting color) {
            this.color = color;
        }

        public ChatFormatting getColor() {
            return color;
        }
    }

}
