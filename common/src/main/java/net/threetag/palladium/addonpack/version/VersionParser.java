package net.threetag.palladium.addonpack.version;

public final class VersionParser {

    public static Version parse(String s, boolean storeX) throws VersionParsingException {
        if (s == null || s.isEmpty()) {
            throw new VersionParsingException("Version must be a non-empty string!");
        }

        Version version;

        try {
            version = new SemanticVersionImpl(s, storeX);
        } catch (VersionParsingException e) {
            version = new StringVersion(s);
        }

        return version;
    }

    public static SemanticVersion parseSemantic(String s) throws VersionParsingException {
        if (s == null || s.isEmpty()) {
            throw new VersionParsingException("Version must be a non-empty string!");
        }

        return new SemanticVersionImpl(s, false);
    }
}
