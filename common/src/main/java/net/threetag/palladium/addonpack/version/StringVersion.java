package net.threetag.palladium.addonpack.version;

public record StringVersion(String version) implements Version {

    @Override
    public String getFriendlyString() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringVersion) {
            return version.equals(((StringVersion) obj).version);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Version o) {
        if (o instanceof SemanticVersion) {
            return -1;
        }

        return getFriendlyString().compareTo(o.getFriendlyString());
    }

    @Override
    public String toString() {
        return version;
    }
}