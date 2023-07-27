package net.threetag.palladium.condition;

public enum ConditionEnvironment {

    DATA,
    ASSETS,
    ALL;

    public boolean forAbilities() {
        return this == DATA || this == ALL;
    }

    public boolean forRenderLayers() {
        return this == ASSETS || this == ALL;
    }

}
