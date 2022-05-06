package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;
import org.jetbrains.annotations.Nullable;

public class ModLoadedCondition extends Condition {

    public final boolean loaded;

    public ModLoadedCondition(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
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

    }
}
