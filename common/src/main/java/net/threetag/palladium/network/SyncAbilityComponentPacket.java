package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.ability.AbilityReference;

public record SyncAbilityComponentPacket(int entityId, AbilityReference reference,
                                         DataComponentPatch patch) implements CustomPacketPayload {

    public static final Type<SyncAbilityComponentPacket> TYPE = new Type<>(Palladium.id("sync_ability_component"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAbilityComponentPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncAbilityComponentPacket::entityId,
            AbilityReference.STREAM_CODEC, SyncAbilityComponentPacket::reference,
            DataComponentPatch.STREAM_CODEC, SyncAbilityComponentPacket::patch,
            SyncAbilityComponentPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncAbilityComponentPacket packet, NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> handleClient(packet, context));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncAbilityComponentPacket packet, NetworkManager.PacketContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            packet.reference.optional(livingEntity, null).ifPresent(ability -> {
                boolean prevEnabled = ability.isEnabled();
                ability.applyPatch(packet.patch);
                var optional = packet.patch.get(PalladiumDataComponents.Abilities.ENABLED.get());

                if (optional != null && optional.isPresent()) {
                    optional.ifPresent(enabled -> {
                        if (enabled != prevEnabled) {
                            if (enabled) {
                                ability.getAbility().firstTick(livingEntity, ability);
                            } else if (ability.getLifetime() > 0) {
                                ability.getAbility().lastTick(livingEntity, ability);
                            }
                        }
                    });
                }

                if (livingEntity == Minecraft.getInstance().player) {
                    AbilityBar.INSTANCE.populate();

                    if (Minecraft.getInstance().screen instanceof PowersScreen powers && powers.selectedTab != null) {
                        powers.selectedTab.populate();
                    }
                }
            });
        }
    }

}
