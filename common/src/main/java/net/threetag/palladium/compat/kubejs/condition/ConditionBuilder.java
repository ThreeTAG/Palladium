package net.threetag.palladium.compat.kubejs.condition;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.compat.kubejs.PalladiumKubeJSPlugin;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;

public class ConditionBuilder extends BuilderBase<ConditionSerializer> {

    public transient TestFunction test;

    public ConditionBuilder(ResourceLocation id) {
        super(id);
        this.test = null;
    }

    @Override
    public RegistryObjectBuilderTypes<? super ConditionSerializer> getRegistryType() {
        return PalladiumKubeJSPlugin.CONDITION;
    }

    @Override
    public ConditionSerializer createObject() {
        return new Serializer(this);
    }

    public ConditionBuilder test(TestFunction testFunction) {
        this.test = testFunction;
        return this;
    }

    @FunctionalInterface
    public interface TestFunction {
        boolean test(LivingEntityJS entity);
    }

    public static class Serializer extends ConditionSerializer {

        public Serializer(ConditionBuilder builder) {
            this.builder = builder;
        }

        public final ConditionBuilder builder;

        @Override
        public Condition make(JsonObject json) {
            return new ScriptableCondition(this.builder, this);
        }
    }

}
