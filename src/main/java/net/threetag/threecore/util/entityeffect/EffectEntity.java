package net.threetag.threecore.util.entityeffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.threetag.threecore.entity.TCEntityTypes;
import net.threetag.threecore.util.EntityUtil;

import java.util.UUID;

public class EffectEntity extends Entity implements IEntityAdditionalSpawnData {

    public UUID anchorUUID;
    public EntityEffect entityEffect;

    public EffectEntity(EntityType<? extends EffectEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreFrustumCheck = true;
    }

    public EffectEntity(World worldIn, Entity anchor, EntityEffect entityEffect) {
        this(TCEntityTypes.EFFECT.get(), worldIn);
        this.anchorUUID = anchor.getUniqueID();
        this.entityEffect = entityEffect;
        this.entityEffect.effectEntity = this;
        this.setLocationAndAngles(anchor.getPosX(), anchor.getPosY() + anchor.getYOffset() + anchor.getEyeHeight(), anchor.getPosZ(), anchor.rotationYaw, anchor.rotationPitch);
    }

    @Override
    protected void registerData() {

    }

    public Entity getAnchorEntity() {
        return EntityUtil.getEntityByUUID(this.world, this.anchorUUID);
    }

    @Override
    public void tick() {
        Entity anchor = getAnchorEntity();
        if (anchor != null && this.entityEffect != null) {
            if (!anchor.isAlive() || this.entityEffect.isDonePlaying()) {
                if (!this.world.isRemote)
                    this.remove();
            } else {
                this.entityEffect.tick(this, anchor);
                this.setLocationAndAngles(anchor.getPosX(), anchor.getPosY() + anchor.getYOffset() + anchor.getEyeHeight(), anchor.getPosZ(), anchor.rotationYaw, anchor.rotationPitch);
            }
        } else if (!this.world.isRemote) {
            this.remove();
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.hasUniqueId("AnchorUUID")) {
            this.anchorUUID = compound.getUniqueId("AnchorUUID");
        }
        this.entityEffect = EntityEffectType.REGISTRY.getValue(new ResourceLocation(compound.getString("EffectType"))).create(compound.getCompound("EffectData"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putUniqueId("AnchorUUID", this.anchorUUID);
        compound.putString("EffectType", this.entityEffect.getEntityEffectType().getRegistryName().toString());
        compound.put("EffectData", this.entityEffect.serializeNBT());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return this.getAnchorEntity() != null && this.entityEffect.isInRangeToRenderDist(this, this.getAnchorEntity(), distance);
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT compound = new CompoundNBT();
        this.writeAdditional(compound);
        buffer.writeCompoundTag(compound);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.readAdditional(additionalData.readCompoundTag());
    }
}
