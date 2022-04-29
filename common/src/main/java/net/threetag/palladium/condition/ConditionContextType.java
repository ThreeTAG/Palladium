package net.threetag.palladium.condition;

public enum ConditionContextType {

    ABILITIES,
    RENDER_LAYERS,
    ALL;

    public boolean forAbilities() {
        return this == ABILITIES || this == ALL;
    }

    public boolean forRenderLayers() {
        return this == RENDER_LAYERS || this == ALL;
    }

}
