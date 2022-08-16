package net.threetag.palladium.compat.kubejs;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerDataJS;
import dev.latvian.mods.kubejs.script.AttachDataEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.compat.kubejs.ability.ScriptableAbility;
import net.threetag.palladium.compat.kubejs.condition.ScriptableCondition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.property.*;

public class PalladiumKubeJSPlugin extends KubeJSPlugin {

    public static final DeferredRegister<ConditionSerializer> CONDITION_SERIALIZERS = DeferredRegister.create(KubeJS.MOD_ID, ConditionSerializer.RESOURCE_KEY);
    public static final RegistrySupplier<ConditionSerializer> SCRIPTABLE_CONDITION = CONDITION_SERIALIZERS.register("scriptable", ScriptableCondition.Serializer::new);

    public static final DeferredRegister<Ability> ABILITY_SERIALIZERS = DeferredRegister.create(KubeJS.MOD_ID, Ability.RESOURCE_KEY);
    public static final RegistrySupplier<Ability> SCRIPTABLE_ABILITY = ABILITY_SERIALIZERS.register("scriptable", ScriptableAbility::new);

    @Override
    public void init() {
        CONDITION_SERIALIZERS.register();
        ABILITY_SERIALIZERS.register();

        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            new RegisterPalladiumPropertyEventJS(handler.getEntity(), handler).post(ScriptType.CLIENT, PalladiumJSEvents.REGISTER_PROPERTIES);
            new RegisterPalladiumPropertyEventJS(handler.getEntity(), handler).post(ScriptType.SERVER, PalladiumJSEvents.REGISTER_PROPERTIES);
        });
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("palladium", PalladiumBinding.class);
    }

    @Override
    public void attachPlayerData(AttachDataEvent<PlayerDataJS> event) {
        event.add("powers", new PowerHandlerJS(event.parent()));
        event.add("properties", new PalladiumPropertyHandlerJS(event.parent()));
    }

    @Override
    public void attachLevelData(AttachDataEvent<LevelJS> event) {
        event.add("powers", new PowerManagerJS(event.parent()));
    }

    public static Object fixValues(PalladiumProperty<?> property, Object value) {
        if (property instanceof IntegerProperty && value instanceof Number number) {
            value = number.intValue();
        } else if (property instanceof FloatProperty && value instanceof Number number) {
            value = number.floatValue();
        } else if (property instanceof DoubleProperty && value instanceof Number number) {
            value = number.doubleValue();
        } else if (property instanceof ResourceLocationProperty && value instanceof String string) {
            value = new ResourceLocation(string);
        }

        return value;
    }

}
