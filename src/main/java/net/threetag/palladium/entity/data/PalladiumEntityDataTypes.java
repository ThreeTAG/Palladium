package net.threetag.palladium.entity.data;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.layer.pack.EntityRenderLayers;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.PalladiumHubData;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.superpower.EntitySuperpowerHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PalladiumEntityDataTypes {

    public static final DeferredRegister<PalladiumEntityDataType<?>> DATA_TYPES = DeferredRegister.create(PalladiumRegistryKeys.ENTITY_DATA_TYPE, Palladium.MOD_ID);

    public static final DeferredHolder<PalladiumEntityDataType<?>, PalladiumEntityDataType<PalladiumHubData>> PALLADIUM_HUB =
            DATA_TYPES.register("palladium_hub_data", () -> new PalladiumEntityDataType<>(PalladiumHubData.CODEC, PalladiumEntityDataType.FILTER_PLAYER));

    public static final DeferredHolder<PalladiumEntityDataType<?>, PalladiumEntityDataType<EntityPowerHandler>> POWER_HANDLER =
            DATA_TYPES.register("powers", () -> new PalladiumEntityDataType<>(EntityPowerHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final DeferredHolder<PalladiumEntityDataType<?>, PalladiumEntityDataType<EntitySuperpowerHandler>> SUPERPOWER_HANDLER =
            DATA_TYPES.register("superpowers", () -> new PalladiumEntityDataType<>(EntitySuperpowerHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final DeferredHolder<PalladiumEntityDataType<?>, PalladiumEntityDataType<EntityRenderLayers>> RENDER_LAYERS =
            DATA_TYPES.register("render_layers", () -> new PalladiumEntityDataType<>(EntityRenderLayers.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final DeferredHolder<PalladiumEntityDataType<?>, PalladiumEntityDataType<EntityCustomizationHandler>> CUSTOMIZATION_HANDLER =
            DATA_TYPES.register("customizations", () -> new PalladiumEntityDataType<>(EntityCustomizationHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final DeferredHolder<PalladiumEntityDataType<?>, PalladiumEntityDataType<EntityFlightHandler>> FLIGHT =
            DATA_TYPES.register("flight", () -> new PalladiumEntityDataType<>(EntityFlightHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

}
