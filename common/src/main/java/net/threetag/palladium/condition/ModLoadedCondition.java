package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;
import net.threetag.palladiumcore.util.Platform;

public class ModLoadedCondition extends Condition {

    public final boolean loaded;

    public ModLoadedCondition(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return this.loaded;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.MOD_LOADED.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> MOD_ID = new StringProperty("mod_id").configurable("ID of the mod that must be loaded");

        public Serializer() {
            this.withProperty(MOD_ID, "palladium");
        }

        @Override
        public Condition make(JsonObject json) {
            return new ModLoadedCondition(Platform.isModLoaded(this.getProperty(json, MOD_ID)));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if a mod is loaded.";
        }
    }
}
