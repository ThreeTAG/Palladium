package net.threetag.threecore.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkHooks;
import net.threetag.threecore.item.TCItems;

import javax.annotation.Nonnull;

public class SuitStandEntity extends ArmorStandEntity {

    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(SuitStandEntity.class, DataSerializers.BYTE);

    public SuitStandEntity(World world) {
        this(TCEntityTypes.SUIT_STAND.get(), world);
    }

    public SuitStandEntity(EntityType<? extends SuitStandEntity> entityType, World world) {
        super(entityType, world);
        this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
        this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
        this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
        this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
    }

    public SuitStandEntity(World worldIn, double posX, double posY, double posZ) {
        this(TCEntityTypes.SUIT_STAND.get(), worldIn);
        this.setPosition(posX, posY, posZ);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DYE_COLOR, (byte) 0);
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.setDyeColor(DyeColor.byId(nbt.getByte("Color")));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("Color", (byte) this.getDyeColor().getId());
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec3d, Hand hand) {
        if (!player.isSneaking()) {
            ItemStack stack = player.getHeldItem(hand);

            if (stack.getItem().isIn(Tags.Items.DYES)) {
                DyeColor color = DyeColor.getColor(stack);
                if (color != null) {
                    this.setDyeColor(color);
                    stack.shrink(1);
                    return ActionResultType.SUCCESS;
                } else {
                    return ActionResultType.PASS;
                }
            }
        }

        return super.applyPlayerInteraction(player, vec3d, hand);
    }

    @Override
    public void readPose(CompoundNBT tagCompound) {
        ListNBT listnbt = tagCompound.getList("Head", 5);
        this.setHeadRotation(listnbt.isEmpty() ? ArmorStandEntity.DEFAULT_HEAD_ROTATION : new Rotations(listnbt));
        ListNBT listnbt1 = tagCompound.getList("Body", 5);
        this.setBodyRotation(listnbt1.isEmpty() ? ArmorStandEntity.DEFAULT_BODY_ROTATION : new Rotations(listnbt1));
        ListNBT listnbt2 = tagCompound.getList("LeftArm", 5);
        this.setLeftArmRotation(listnbt2.isEmpty() ? DEFAULT_LEFTARM_ROTATION : new Rotations(listnbt2));
        ListNBT listnbt3 = tagCompound.getList("RightArm", 5);
        this.setRightArmRotation(listnbt3.isEmpty() ? DEFAULT_RIGHTARM_ROTATION : new Rotations(listnbt3));
        ListNBT listnbt4 = tagCompound.getList("LeftLeg", 5);
        this.setLeftLegRotation(listnbt4.isEmpty() ? DEFAULT_LEFTLEG_ROTATION : new Rotations(listnbt4));
        ListNBT listnbt5 = tagCompound.getList("RightLeg", 5);
        this.setRightLegRotation(listnbt5.isEmpty() ? DEFAULT_RIGHTLEG_ROTATION : new Rotations(listnbt5));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TCItems.SUIT_STAND.get());
    }

    @Override
    public void func_213815_f(DamageSource p_213815_1_) {
        Block.spawnAsEntity(this.world, new BlockPos(this), new ItemStack(TCItems.SUIT_STAND.get()));
        this.func_213816_g(p_213815_1_);
    }

    @Override
    public void playParticles() {
        if (this.world instanceof ServerWorld) {
            ((ServerWorld) this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.QUARTZ_BLOCK.getDefaultState()), this.posX, this.posY + (double) this.getHeight() / 1.5D, this.posZ, 10, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
        }
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public DyeColor getDyeColor() {
        return DyeColor.byId(this.dataManager.get(DYE_COLOR) & 15);
    }

    public void setDyeColor(DyeColor color) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte) (b0 & 240 | color.getId() & 15));
    }
}
