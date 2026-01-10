package net.threetag.palladium.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.menu.ClientTailoringRecipeBook;

import java.util.List;

public record SyncAvailableTailoringRecipesPacket(List<ResourceKey<Recipe<?>>> recipes) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncAvailableTailoringRecipesPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_available_tailoring_recipes"));
    public static final StreamCodec<FriendlyByteBuf, SyncAvailableTailoringRecipesPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.RECIPE).apply(ByteBufCodecs.list()), SyncAvailableTailoringRecipesPacket::recipes,
            SyncAvailableTailoringRecipesPacket::new
    );

    public static void handle(SyncAvailableTailoringRecipesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientTailoringRecipeBook.setAvailableRecipes(packet.recipes));
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
