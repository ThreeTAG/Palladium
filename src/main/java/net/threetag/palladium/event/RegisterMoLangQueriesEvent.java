package net.threetag.palladium.event;

import net.neoforged.bus.api.Event;
import net.threetag.palladium.logic.molang.EntityContext;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;
import team.unnamed.mocha.runtime.value.ObjectValue;

import java.util.List;
import java.util.function.Function;

public class RegisterMoLangQueriesEvent extends Event {

    private final List<MoLangQueryRegistry.QueryFactory<?>> factories;

    public RegisterMoLangQueriesEvent(List<MoLangQueryRegistry.QueryFactory<?>> factories) {
        this.factories = factories;
    }

    public <T extends ObjectValue> void register(String namespace, Class<T> clazz, Function<EntityContext, T> factory) {
        this.factories.add(new MoLangQueryRegistry.QueryFactory<>(namespace, clazz, factory));
    }

}
