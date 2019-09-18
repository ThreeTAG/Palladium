package net.threetag.threecore.util.threedata;

public enum EnumSync {

    NONE, SELF, EVERYONE;

    public EnumSync add(EnumSync newSync) {
        return newSync.ordinal() > this.ordinal() ? newSync : this;
    }

}
