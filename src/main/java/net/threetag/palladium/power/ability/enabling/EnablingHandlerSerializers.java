package net.threetag.palladium.power.ability.enabling;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class EnablingHandlerSerializers {

    public static final DeferredRegister<EnablingHandlerSerializer<?>> ENABLING_HANDLERS = DeferredRegister.create(PalladiumRegistryKeys.ABILITY_ENABLING_HANDLER_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<EnablingHandlerSerializer<?>, ConditionalEnablingHandler.Serializer> CONDITIONAL = ENABLING_HANDLERS.register("conditional", ConditionalEnablingHandler.Serializer::new);
    public static final DeferredHolder<EnablingHandlerSerializer<?>, KeyBindEnablingHandler.Serializer> KEY_BIND = ENABLING_HANDLERS.register("key_bind", KeyBindEnablingHandler.Serializer::new);

}
