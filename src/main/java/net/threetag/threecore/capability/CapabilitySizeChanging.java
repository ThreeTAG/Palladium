package net.threetag.threecore.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.network.UpdateSizeData;
import net.threetag.threecore.sizechanging.SizeChangeType;
import net.threetag.threecore.util.threedata.*;

import javax.annotation.Nullable;

public class CapabilitySizeChanging implements ISizeChanging, IWrappedThreeDataHolder, INBTSerializable<CompoundNBT> {

    @CapabilityInject(ISizeChanging.class)
    public static Capability<ISizeChanging> SIZE_CHANGING;

    public static final float MIN_SIZE = 0.05F;
    public static final float MAX_SIZE = 16F;

    public static final ThreeData<Float> SCALE = new FloatThreeData("scale").setSyncType(EnumSync.NONE);
    public static final ThreeData<Float> ESTIMATED_SCALE = new FloatThreeData("estimated_scale");
    public static final ThreeData<Float> SCALE_PER_TICK = new FloatThreeData("scale_per_tick");
    public static final ThreeData<SizeChangeType> SIZE_CHANGE_TYPE = new SizeChangeTypeThreeData("size_change_type");

    public final ThreeDataManager dataManager = new ThreeDataManager().setListener(new ThreeDataManager.Listener() {
        @Override
        public <T> void dataChanged(ThreeData<T> data, T oldValue, T value) {
            update(data, value);
        }
    });
    public final Entity entity;
    private float prevWidth = 1F;
    private float prevHeight = 1F;
    private boolean dirty = false;

    public CapabilitySizeChanging(Entity entity) {
        this.entity = entity;
        this.dataManager.register(SCALE, 1F);
        this.dataManager.register(ESTIMATED_SCALE, 1F);
        this.dataManager.register(SCALE_PER_TICK, 0F);
        this.dataManager.register(SIZE_CHANGE_TYPE, SizeChangeType.DEFAULT_TYPE);
    }

    @Override
    public IThreeDataHolder getThreeDataHolder() {
        return this.dataManager;
    }

    @Override
    public void tick() {
        this.prevWidth = this.getWidth();
        this.prevHeight = this.getHeight();
        this.fixValues();

        if (this.dataManager.get(SCALE_PER_TICK) != 0F) {
            this.dataManager.set(SCALE, this.dataManager.get(SCALE) + this.dataManager.get(SCALE_PER_TICK));

            if (Math.abs(this.dataManager.get(SCALE) - this.dataManager.get(ESTIMATED_SCALE)) < Math.abs(this.dataManager.get(SCALE_PER_TICK))) {
                this.dataManager.set(SCALE_PER_TICK, 0F);
                this.dataManager.set(SCALE, this.dataManager.get(ESTIMATED_SCALE));
                this.getSizeChangeType().end(entity, this, this.dataManager.get(SCALE));
            }

            this.getSizeChangeType().onSizeChanged(entity, this, this.dataManager.get(SCALE));
        } else {
            this.dataManager.set(SCALE, this.dataManager.get(ESTIMATED_SCALE));
        }

        this.getSizeChangeType().onUpdate(entity, this, this.dataManager.get(SCALE));

        if (this.getWidth() != this.prevWidth || this.getHeight() != this.prevHeight || this.dirty) {
            this.updateBoundingBox();
            this.setDirty();
            this.dirty = false;
        }
    }

    @Override
    public void updateBoundingBox() {
        boolean b = entity.firstUpdate;
        entity.firstUpdate = true;
        entity.recalculateSize();
        double d0 = (double) entity.size.width / 2.0D;
        entity.setBoundingBox(new AxisAlignedBB(entity.getPosX() - d0, entity.getPosY(), entity.getPosZ() - d0, entity.getPosX() + d0, entity.getPosY() + (double) entity.size.height, entity.getPosZ() + d0));
        entity.firstUpdate = b;
    }

    public void fixValues() {
        if (this.dataManager.get(SCALE) <= 0F)
            this.dataManager.set(SCALE, 1F);
        if (this.dataManager.get(ESTIMATED_SCALE) <= 0F)
            this.dataManager.set(ESTIMATED_SCALE, 1F);
    }

    @Override
    public float getWidth() {
        float f = this.dataManager.get(SCALE);
//        if (this.entity instanceof LivingEntity)
//            f *= ((LivingEntity) this.entity).getAttribute(SizeManager.SIZE_WIDTH).getValue();
        return MathHelper.clamp(f, MIN_SIZE, MAX_SIZE);
    }

    @Override
    public float getHeight() {
        float f = this.dataManager.get(SCALE);
        if (this.entity instanceof PlayerEntity) {
            // TODO reenable size attributes
//            f *= ((LivingEntity) this.entity).getAttribute(SizeManager.SIZE_HEIGHT).getValue();
//            System.out.println(((LivingEntity) this.entity).getAttribute(SizeManager.SIZE_HEIGHT).getValue());
        }
        return MathHelper.clamp(f, MIN_SIZE, MAX_SIZE);
    }

    @Override
    public float getRenderWidth(float partialTicks) {
        return this.prevWidth + (this.getWidth() - this.prevWidth) * partialTicks;
    }

    @Override
    public float getRenderHeight(float partialTicks) {
        return this.prevHeight + (this.getHeight() - this.prevHeight) * partialTicks;
    }

    @Override
    public float getScale() {
        return this.dataManager.get(SCALE);
    }

    @Override
    public SizeChangeType getSizeChangeType() {
        return this.dataManager.get(SIZE_CHANGE_TYPE);
    }

    @Override
    public void changeSizeChangeType(SizeChangeType type) {
        if (type != null) {
            this.dataManager.set(SIZE_CHANGE_TYPE, type);
        }
    }

    @Override
    public boolean startSizeChange(@Nullable SizeChangeType type, float size) {
        if (size != this.dataManager.get(ESTIMATED_SCALE)) {
            if (type != null)
                this.dataManager.set(SIZE_CHANGE_TYPE, type);
            if (!getSizeChangeType().start(entity, this, this.dataManager.get(SCALE), size))
                return false;
            this.dataManager.set(ESTIMATED_SCALE, size);
            this.dataManager.set(SCALE_PER_TICK, (size - this.dataManager.get(SCALE)) / this.getSizeChangeType().getSizeChangingTime(entity, this, size));
            return true;
        }
        return false;
    }

    @Override
    public boolean setSizeDirectly(@Nullable SizeChangeType type, float size) {
        if (size != this.dataManager.get(SCALE)) {
            this.dataManager.set(SCALE, size);
            this.dataManager.set(ESTIMATED_SCALE, size);
            this.dataManager.set(SCALE_PER_TICK, 0F);
            if (type != null)
                this.dataManager.set(SIZE_CHANGE_TYPE, type);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSizeChanging() {
        return this.dataManager.get(SCALE_PER_TICK) != 0F;
    }

    public <T> void update(ThreeData<T> data, T value) {
        this.setDirty();

        if (entity.world.isRemote)
            return;

        CompoundNBT nbt = new CompoundNBT();
        data.writeToNBT(nbt, value);

        if (data.getSyncType() != EnumSync.NONE && entity instanceof ServerPlayerEntity) {
            ThreeCore.NETWORK_CHANNEL.sendTo(new UpdateSizeData(entity.getEntityId(), data.getKey(), nbt), ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
        if (data.getSyncType() == EnumSync.EVERYONE && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateSizeData(entity.getEntityId(), data.getKey(), nbt));
        }
    }

    public void setDirty() {
        this.dirty = true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.dataManager.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.dataManager.deserializeNBT(nbt);
    }
}
