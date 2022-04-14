package net.threetag.palladium.addonpack.version;

public class VersionParsingException extends Exception {

    public VersionParsingException() {
        super();
    }

    public VersionParsingException(Throwable t) {
        super(t);
    }

    public VersionParsingException(String s) {
        super(s);
    }

    public VersionParsingException(String s, Throwable t) {
        super(s, t);
    }
}
