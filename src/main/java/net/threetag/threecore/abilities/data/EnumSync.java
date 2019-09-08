package net.threetag.threecore.abilities.data;

public enum EnumSync {

    NONE, SELF, EVERYONE;

    public EnumSync add(EnumSync newSync) {
        return newSync.ordinal() > this.ordinal() ? newSync : this;
    }

}
