package net.threetag.palladium.network;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.threetag.palladium.Palladium;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumNetwork {

    public static void init() {
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            if (entity instanceof LivingEntity livingEntity) {
                consumer.accept(SyncEntityPowersPacket.create(livingEntity));
                consumer.accept(SyncEntityCustomizationsPacket.create(livingEntity));
            }
        });
    }

    @SubscribeEvent
    static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(AbilityKeyChangePacket.TYPE, AbilityKeyChangePacket.STREAM_CODEC, AbilityKeyChangePacket::handle);
        registrar.playToServer(AbilityClickedPacket.TYPE, AbilityClickedPacket.STREAM_CODEC, AbilityClickedPacket::handle);
        registrar.playToServer(BuyAbilityPacket.TYPE, BuyAbilityPacket.STREAM_CODEC, BuyAbilityPacket::handle);
        registrar.playToServer(SelectCustomizationPacket.TYPE, SelectCustomizationPacket.STREAM_CODEC, SelectCustomizationPacket::handle);
        registrar.playToServer(ToggleEntityFlightPacket.TYPE, ToggleEntityFlightPacket.STREAM_CODEC, ToggleEntityFlightPacket::handle);
        registrar.playToServer(TailoringCraftPacket.TYPE, TailoringCraftPacket.STREAM_CODEC, TailoringCraftPacket::handle);

        registrar.playToClient(SyncEntityPowersPacket.TYPE, SyncEntityPowersPacket.STREAM_CODEC, SyncEntityPowersPacket::handle);
        registrar.playToClient(SyncAbilityComponentPacket.TYPE, SyncAbilityComponentPacket.STREAM_CODEC, SyncAbilityComponentPacket::handle);
        registrar.playToClient(SyncEnergyBarPacket.TYPE, SyncEnergyBarPacket.STREAM_CODEC, SyncEnergyBarPacket::handle);
        registrar.playToClient(OpenAbilityBuyScreenPacket.TYPE, OpenAbilityBuyScreenPacket.STREAM_CODEC, OpenAbilityBuyScreenPacket::handle);
        registrar.playToClient(SyncEntityCustomizationsPacket.TYPE, SyncEntityCustomizationsPacket.STREAM_CODEC, SyncEntityCustomizationsPacket::handle);
        registrar.playToClient(SyncEntityCustomizationPacket.TYPE, SyncEntityCustomizationPacket.STREAM_CODEC, SyncEntityCustomizationPacket::handle);
        registrar.playToClient(SyncEntityUnselectCustomizationPacket.TYPE, SyncEntityUnselectCustomizationPacket.STREAM_CODEC, SyncEntityUnselectCustomizationPacket::handle);
        registrar.playToClient(SyncUnlockedCustomizationsPacket.TYPE, SyncUnlockedCustomizationsPacket.STREAM_CODEC, SyncUnlockedCustomizationsPacket::handle);
        registrar.playToClient(SyncSwingAnchorPacket.TYPE, SyncSwingAnchorPacket.STREAM_CODEC, SyncSwingAnchorPacket::handle);
        registrar.playToClient(SyncAvailableTailoringRecipesPacket.TYPE, SyncAvailableTailoringRecipesPacket.STREAM_CODEC, SyncAvailableTailoringRecipesPacket::handle);
    }
}
