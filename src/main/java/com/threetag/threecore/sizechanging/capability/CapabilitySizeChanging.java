package com.threetag.threecore.sizechanging.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.sizechanging.SizeChangeType;
import com.threetag.threecore.sizechanging.SizeManager;
import com.threetag.threecore.sizechanging.network.SyncSizeMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class CapabilitySizeChanging implements ISizeChanging, INBTSerializable<CompoundNBT> {

    @CapabilityInject(ISizeChanging.class)
    public static Capability<ISizeChanging> SIZE_CHANGING;

    public static final float MIN_SIZE = 0.05F;
    public static final float MAX_SIZE = 16F;

    protected float currentWidth = 1F;
    protected float currentHeight = 1F;
    protected float prevWidth = 1F;
    protected float prevHeight = 1F;
    protected float scale = 1F;
    protected float estimatedScale = 1F;
    protected float scalePerTick;
    protected SizeChangeType sizeChangeType;
    private boolean dirty = false;

    @Override
    public void tick(Entity entity) {
        if (this.currentWidth <= 0F || this.currentHeight <= 0F || this.scale <= 0F) {
            this.currentWidth = 1F;
            this.currentHeight = 1F;
            this.prevWidth = 1F;
            this.prevHeight = 1F;
            this.scale = 1F;
        }
        this.prevWidth = this.currentWidth;
        this.prevHeight = this.currentHeight;

        if (!entity.world.isRemote) {
            this.currentWidth = 1F;
            this.currentHeight = 1F;

            if (entity instanceof LivingEntity) {
                this.currentWidth *= ((LivingEntity) entity).getAttribute(SizeManager.SIZE_WIDTH).getValue();
                this.currentHeight *= ((LivingEntity) entity).getAttribute(SizeManager.SIZE_HEIGHT).getValue();
            }

            if (this.scalePerTick != 0F) {
                this.scale += this.scalePerTick;

                if (Math.abs(this.scale - this.estimatedScale) < Math.abs(this.scalePerTick)) {
                    this.scalePerTick = 0F;
                    this.scale = this.estimatedScale;
                    this.getSizeChangeType().end(entity, this, this.scale);
                }

                this.getSizeChangeType().onSizeChanged(entity, this, this.scale);
            }

            this.getSizeChangeType().onUpdate(entity, this, this.scale);

            this.currentWidth *= this.scale;
            this.currentHeight *= this.scale;
        }

        this.currentWidth = MathHelper.clamp(this.currentWidth, MIN_SIZE, MAX_SIZE);
        this.currentHeight = MathHelper.clamp(this.currentHeight, MIN_SIZE, MAX_SIZE);

        if (this.currentWidth != this.prevWidth || this.currentHeight != this.prevHeight || this.dirty) {
            updateBoundingBox(entity);
            this.sync(entity);
        }
    }

    @Override
    public void updateBoundingBox(Entity entity) {
        boolean b = entity.firstUpdate;
        entity.firstUpdate = true;
        entity.recalculateSize();
        double d0 = (double) entity.size.width / 2.0D;
        entity.setBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + (double) entity.size.height, entity.posZ + d0));
        entity.firstUpdate = b;
    }

    @Override
    public void sync(Entity entity) {
        if (!entity.world.isRemote) {
            SyncSizeMessage msg = new SyncSizeMessage(entity.getEntityId(), this.serializeNBT());
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);

            if (entity instanceof ServerPlayerEntity)
                ThreeCore.NETWORK_CHANNEL.sendTo(msg, ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    @Override
    public float getWidth() {
        return this.currentWidth;
    }

    @Override
    public float getHeight() {
        return this.currentHeight;
    }

    @Override
    public float getRenderWidth(float partialTicks) {
        return this.prevWidth + (this.currentWidth - this.prevWidth) * partialTicks;
    }

    @Override
    public float getRenderHeight(float partialTicks) {
        return this.prevHeight + (this.currentHeight - this.prevHeight) * partialTicks;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    @Override
    public SizeChangeType getSizeChangeType() {
        return this.sizeChangeType == null ? SizeChangeType.DEFAULT_TYPE : this.sizeChangeType;
    }

    @Override
    public void changeSizeChangeType(SizeChangeType type) {
        if (this.sizeChangeType != type && type != null) {
            this.sizeChangeType = type;
            this.dirty = true;
        }
    }

    @Override
    public boolean startSizeChange(Entity entity, @Nullable SizeChangeType type, float size) {
        if (size != this.estimatedScale) {
            type = type == null ? getSizeChangeType() : type;
            if (!type.start(entity, this, this.scale, size))
                return false;
            this.estimatedScale = size;
            this.scalePerTick = (float) (this.estimatedScale - this.scale) / this.getSizeChangeType().getSizeChangingTime(entity, this, this.estimatedScale);
            this.sizeChangeType = type;
            this.sync(entity);
            return true;
        }
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("Width", this.currentWidth);
        nbt.putFloat("Height", this.currentHeight);
        nbt.putFloat("Scale", this.scale);
        nbt.putFloat("EstimatedScale", this.estimatedScale);
        nbt.putFloat("ScalePerTick", this.scalePerTick);
        nbt.putString("SizeChangeType", this.getSizeChangeType().getRegistryName().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.currentWidth = nbt.getFloat("Width");
        this.currentHeight = nbt.getFloat("Height");
        this.scale = nbt.getFloat("Scale");
        this.estimatedScale = nbt.getFloat("EstimatedScale");
        this.scalePerTick = nbt.getFloat("ScalePerTick");
        this.sizeChangeType = SizeChangeType.REGISTRY.getValue(new ResourceLocation(nbt.getString("SizeChangeType")));
    }
}
