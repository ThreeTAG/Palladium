package net.threetag.threecore.util.entityeffect;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.ThreeData;
import net.threetag.threecore.util.threedata.ThreeDataManager;
import net.threetag.threecore.util.threedata.IWrappedThreeDataHolder;

public abstract class EntityEffect implements INBTSerializable<CompoundNBT>, IWrappedThreeDataHolder {

    private final EntityEffectType entityEffectType;
    EffectEntity effectEntity;
    protected ThreeDataManager dataManager = new ThreeDataManager().setListener(new ThreeDataManager.Listener() {
        @Override
        public <T> void dataChanged(ThreeData<T> data, T oldValue, T value) {
            if (effectEntity != null) {
                CompoundNBT nbt = new CompoundNBT();
                data.writeToNBT(nbt, value);
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> effectEntity), new EntityEffectUpdateMessage(effectEntity.getEntityId(), data.getKey(), nbt));
            }
        }
    });

    public static final ThreeData<Boolean> IS_DONE_PLAYING = new BooleanThreeData("is_done_playing");

    public EntityEffect(EntityEffectType entityEffectType) {
        this.entityEffectType = entityEffectType;
        this.registerData();
    }

    public void registerData() {
        this.register(IS_DONE_PLAYING, false);
    }

    public final EntityEffectType getEntityEffectType() {
        return entityEffectType;
    }

    @Override
    public IThreeDataHolder getThreeDataHolder() {
        return this.dataManager;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(EffectEntity entity, Entity anchor, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, boolean isFirstPerson, float partialTicks);

    public abstract void tick(EffectEntity entity, Entity anchor);

    public boolean isDonePlaying() {
        return this.get(IS_DONE_PLAYING);
    }

    public boolean isInRangeToRenderDist(EffectEntity effectEntity, Entity anchor, double distance) {
        return anchor.isInRangeToRenderDist(distance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("EntityEffectType", this.entityEffectType.getRegistryName().toString());
        nbt.put("Data", this.dataManager.serializeNBT());
        nbt.put("IsDonePlaying", this.dataManager.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.dataManager.deserializeNBT(nbt.getCompound("Data"));
    }
}
