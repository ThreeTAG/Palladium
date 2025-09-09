package net.threetag.palladium.entity.data;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.layer.EntityRenderLayers;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.PalladiumHubData;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.superpower.EntitySuperpowerHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PalladiumEntityDataTypes {

    public static final DeferredRegister<PalladiumEntityDataType<?>> DATA_TYPES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ENTITY_DATA_TYPE);

    public static final RegistryHolder<PalladiumEntityDataType<PalladiumHubData>> PALLADIUM_HUB =
            DATA_TYPES.register("palladium_hub_data", () -> new PalladiumEntityDataType<>(PalladiumHubData.CODEC, PalladiumEntityDataType.FILTER_PLAYER));

    public static final RegistryHolder<PalladiumEntityDataType<EntityPowerHandler>> POWER_HANDLER =
            DATA_TYPES.register("powers", () -> new PalladiumEntityDataType<>(EntityPowerHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final RegistryHolder<PalladiumEntityDataType<EntitySuperpowerHandler>> SUPERPOWER_HANDLER =
            DATA_TYPES.register("superpowers", () -> new PalladiumEntityDataType<>(EntitySuperpowerHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final RegistryHolder<PalladiumEntityDataType<EntityRenderLayers>> RENDER_LAYERS =
            DATA_TYPES.register("render_layers", () -> new PalladiumEntityDataType<>(EntityRenderLayers.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final RegistryHolder<PalladiumEntityDataType<EntityCustomizationHandler>> CUSTOMIZATION_HANDLER =
            DATA_TYPES.register("customizations", () -> new PalladiumEntityDataType<>(EntityCustomizationHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

    public static final RegistryHolder<PalladiumEntityDataType<EntityFlightHandler>> FLIGHT =
            DATA_TYPES.register("flight", () -> new PalladiumEntityDataType<>(EntityFlightHandler.CODEC, PalladiumEntityDataType.FILTER_LIVING));

}
