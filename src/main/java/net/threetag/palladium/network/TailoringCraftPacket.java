package net.threetag.palladium.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.menu.TailoringMenu;

public record TailoringCraftPacket(ResourceKey<Recipe<?>> recipeKey) implements CustomPacketPayload {

    public static final Type<TailoringCraftPacket> TYPE = new Type<>(Palladium.id("tailoring_craft"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TailoringCraftPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.RECIPE), TailoringCraftPacket::recipeKey,
            TailoringCraftPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TailoringCraftPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof TailoringMenu menu && context.player().level() instanceof ServerLevel level) {
                menu.craft(packet.recipeKey());
            }
        });
    }
}
