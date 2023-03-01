package net.threetag.palladium.entity;

import net.minecraft.core.Rotations;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladiumcore.network.NetworkManager;

public class SuitStand extends ArmorStand {

    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final EntityDataAccessor<Byte> DYE_COLOR = SynchedEntityData.defineId(SuitStand.class, EntityDataSerializers.BYTE);

    @SuppressWarnings("unchecked")
    public SuitStand(EntityType<?> entityType, Level level) {
        super((EntityType<? extends ArmorStand>) entityType, level);
        this.setLeftArmPose(DEFAULT_LEFT_ARM_POSE);
        this.setRightArmPose(DEFAULT_RIGHT_ARM_POSE);
        this.setLeftLegPose(DEFAULT_LEFT_LEG_POSE);
        this.setRightLegPose(DEFAULT_RIGHT_LEG_POSE);
    }

    public SuitStand(Level level, double posX, double posY, double posZ) {
        this(PalladiumEntityTypes.SUIT_STAND.get(), level);
        this.setPos(posX, posY, posZ);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DYE_COLOR, (byte) 0);
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 4, true));
    }

    private byte setBit(byte oldBit, int offset, boolean value) {
        if (value) {
            oldBit = (byte) (oldBit | offset);
        } else {
            oldBit = (byte) (oldBit & ~offset);
        }

        return oldBit;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setDyeColor(DyeColor.byId(nbt.getByte("Color")));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("Color", (byte) this.getDyeColor().getId());
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (!player.isCrouching()) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof DyeItem dyeItem) {
                this.setDyeColor(dyeItem.getDyeColor());
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactAt(player, vec, hand);
    }

    public void readSuitStandPose(CompoundTag compound) {
        ListTag listTag = compound.getList("Head", 5);
        this.setHeadPose(listTag.isEmpty() ? ArmorStand.DEFAULT_HEAD_POSE : new Rotations(listTag));
        ListTag listTag2 = compound.getList("Body", 5);
        this.setBodyPose(listTag2.isEmpty() ? ArmorStand.DEFAULT_BODY_POSE : new Rotations(listTag2));
        ListTag listTag3 = compound.getList("LeftArm", 5);
        this.setLeftArmPose(listTag3.isEmpty() ? DEFAULT_LEFT_ARM_POSE : new Rotations(listTag3));
        ListTag listTag4 = compound.getList("RightArm", 5);
        this.setRightArmPose(listTag4.isEmpty() ? DEFAULT_RIGHT_ARM_POSE : new Rotations(listTag4));
        ListTag listTag5 = compound.getList("LeftLeg", 5);
        this.setLeftLegPose(listTag5.isEmpty() ? DEFAULT_LEFT_LEG_POSE : new Rotations(listTag5));
        ListTag listTag6 = compound.getList("RightLeg", 5);
        this.setRightLegPose(listTag6.isEmpty() ? DEFAULT_RIGHT_LEG_POSE : new Rotations(listTag6));
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(PalladiumItems.SUIT_STAND.get());
    }

    public void suitStandBrokenByPlayer(DamageSource damageSource) {
        Block.popResource(this.level, this.blockPosition(), new ItemStack(PalladiumItems.SUIT_STAND.get()));
        this.brokenByAnything(damageSource);
    }

    public void suitStandShowBreakingParticles() {
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.QUARTZ_BLOCK.defaultBlockState()), this.getX(), this.getY(0.6666666666666666), this.getZ(), 10, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05);
        }
    }

    public DyeColor getDyeColor() {
        return DyeColor.byId(this.entityData.get(DYE_COLOR) & 15);
    }

    public void setDyeColor(DyeColor color) {
        byte b0 = this.entityData.get(DYE_COLOR);
        this.entityData.set(DYE_COLOR, (byte) (b0 & 240 | color.getId() & 15));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }
}
