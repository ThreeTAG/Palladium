package net.threetag.palladium.util.property;

public enum SyncType {

    NONE, SELF, EVERYONE;

    public SyncType add(SyncType newSync) {
        return newSync.ordinal() > this.ordinal() ? newSync : this;
    }

}
