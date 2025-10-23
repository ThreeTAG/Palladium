package net.threetag.palladium.power.ability.keybind;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class KeyBindTypeSerializers {

    public static final DeferredRegister<KeyBindTypeSerializer<?>> KEY_BIND_TYPES = DeferredRegister.create(PalladiumRegistryKeys.KEY_BIND_TYPE_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<KeyBindTypeSerializer<?>, AbilityKeyBind.Serializer> ABILITY_KEY = KEY_BIND_TYPES.register("ability_key", AbilityKeyBind.Serializer::new);
    public static final DeferredHolder<KeyBindTypeSerializer<?>, JumpKeyBind.Serializer> JUMP = KEY_BIND_TYPES.register("jump", JumpKeyBind.Serializer::new);
    public static final DeferredHolder<KeyBindTypeSerializer<?>, MouseClickKeyBind.Serializer> MOUSE_CLICK = KEY_BIND_TYPES.register("mouse_click", MouseClickKeyBind.Serializer::new);

}
