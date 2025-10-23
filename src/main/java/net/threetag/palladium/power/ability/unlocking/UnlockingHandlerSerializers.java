package net.threetag.palladium.power.ability.unlocking;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class UnlockingHandlerSerializers {

    public static final DeferredRegister<UnlockingHandlerSerializer<?>> UNLOCKING_HANDLERS = DeferredRegister.create(PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<UnlockingHandlerSerializer<?>, ConditionalUnlockingHandler.Serializer> CONDITIONAL = UNLOCKING_HANDLERS.register("conditional", ConditionalUnlockingHandler.Serializer::new);
    public static final DeferredHolder<UnlockingHandlerSerializer<?>, ExperienceLevelBuyableUnlockingHandler.Serializer> XP_BUYABLE = UNLOCKING_HANDLERS.register("experience_level_buyable", ExperienceLevelBuyableUnlockingHandler.Serializer::new);
    public static final DeferredHolder<UnlockingHandlerSerializer<?>, ItemBuyableUnlockingHandler.Serializer> ITEM_BUYABLE = UNLOCKING_HANDLERS.register("item_buyable", ItemBuyableUnlockingHandler.Serializer::new);
    public static final DeferredHolder<UnlockingHandlerSerializer<?>, ScoreBuyableUnlockingHandler.Serializer> SCORE_BUYABLE = UNLOCKING_HANDLERS.register("score_buyable", ScoreBuyableUnlockingHandler.Serializer::new);

}
