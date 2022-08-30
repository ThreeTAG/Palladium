package net.threetag.palladium.mixin.forge;

import dev.architectury.extensions.network.EntitySpawnExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.threetag.palladium.network.ExtendedEntitySpawnData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExtendedEntitySpawnData.class)
public interface ExtendedEntitySpawnDataMixin extends IEntityAdditionalSpawnData {

    @Override
    default void writeSpawnData(FriendlyByteBuf buf) {
        ((EntitySpawnExtension) this).saveAdditionalSpawnData(buf);
    }

    @Override
    default void readSpawnData(FriendlyByteBuf buf) {
        ((EntitySpawnExtension) this).loadAdditionalSpawnData(buf);
    }

}
