package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.DataAttachmentLoader;
import net.threetag.palladium.attachment.PlatformAttachmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class SyncAttachmentTypePacket implements CustomPacketPayload {

    public static final Type<SyncAttachmentTypePacket> TYPE = new Type<>(Palladium.id("sync_attachment_type"));
    public static final int MAX_DATA_SIZE_IN_BYTES = ServerboundCustomPayloadPacket.MAX_PAYLOAD_SIZE - 265;

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAttachmentTypePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, p -> p.entityId,
            Change.STREAM_CODEC, p -> p.change,
            SyncAttachmentTypePacket::new
    );

    private final int entityId;
    private final Change change;

    public SyncAttachmentTypePacket(int entityId, PlatformAttachmentType<?> type, Object value, RegistryAccess registryAccess) {
        StreamCodec<? super RegistryFriendlyByteBuf, Object> codec = (StreamCodec<? super RegistryFriendlyByteBuf, Object>) type.getStreamCodec();
        Objects.requireNonNull(codec, "attachment packet codec cannot be null");
        Objects.requireNonNull(registryAccess, "dynamic registry manager cannot be null");
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(new FriendlyByteBuf(Unpooled.buffer()), registryAccess);
        if (value != null) {
            buf.writeBoolean(true);
            codec.encode(buf, value);
        } else {
            buf.writeBoolean(false);
        }

        byte[] encoded = buf.array();
        if (encoded.length > MAX_DATA_SIZE_IN_BYTES) {
            throw new IllegalArgumentException("Data for attachment '%s' was too big (%d bytes, over maximum %d)".formatted(DataAttachmentLoader.INSTANCE.getId(type), encoded.length, MAX_DATA_SIZE_IN_BYTES));
        } else {
            this.entityId = entityId;
            this.change = new Change(type, encoded);
        }
    }

    public SyncAttachmentTypePacket(int entityId, Change change) {
        this.entityId = entityId;
        this.change = change;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncAttachmentTypePacket packet, NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> handleClient(packet, context));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncAttachmentTypePacket packet, NetworkManager.PacketContext context) {
        var entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(packet.entityId);

        if (entity != null) {
            PlatformAttachmentType type = packet.change.type;
            Object value = packet.decodeValue(context.registryAccess());
            PlatformAttachmentType.set(entity, type, value);
        }
    }

    public @Nullable Object decodeValue(RegistryAccess registryAccess) {
        StreamCodec<? super RegistryFriendlyByteBuf, Object> codec = (StreamCodec<? super RegistryFriendlyByteBuf, Object>) this.change.type.getStreamCodec();
        Objects.requireNonNull(codec, "codec was null");
        Objects.requireNonNull(registryAccess, "dynamic registry manager cannot be null");
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.copiedBuffer(this.change.data), registryAccess);
        return !buf.readBoolean() ? null : codec.decode(buf);
    }

    public record Change(PlatformAttachmentType<?> type, byte[] data) {

        public static final StreamCodec<FriendlyByteBuf, Change> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC.map(
                        (id) -> Objects.requireNonNull(DataAttachmentLoader.INSTANCE.get(id)),
                        (type) -> Objects.requireNonNull(DataAttachmentLoader.INSTANCE.getId(type))
                ),
                Change::type,
                ByteBufCodecs.BYTE_ARRAY, Change::data,
                Change::new);
    }

}
