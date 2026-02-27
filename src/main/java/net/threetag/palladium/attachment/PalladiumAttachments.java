package net.threetag.palladium.attachment;

import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.Palladium;

public class PalladiumAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Palladium.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> IS_CLIMBING = ATTACHMENT_TYPES.register("is_climbing", () -> AttachmentType.builder(() -> false).sync(ByteBufCodecs.BOOL).build());

}
