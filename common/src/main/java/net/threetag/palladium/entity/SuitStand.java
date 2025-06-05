package net.threetag.palladium.entity;

import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.item.PalladiumItems;

public class SuitStand extends ArmorStand {

    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final EntityDataAccessor<Byte> DYE_COLOR = SynchedEntityData.defineId(SuitStand.class, EntityDataSerializers.BYTE);

    public SuitStand(EntityType<? extends SuitStand> entityType, Level level) {
        super(entityType, level);
        this.setLeftArmPose(DEFAULT_LEFT_ARM_POSE);
        this.setRightArmPose(DEFAULT_RIGHT_ARM_POSE);
        this.setLeftLegPose(DEFAULT_LEFT_LEG_POSE);
        this.setRightLegPose(DEFAULT_RIGHT_LEG_POSE);
        this.setShowArms(true);
    }

    public SuitStand(Level level, double x, double y, double z) {
        this(PalladiumEntityTypes.SUIT_STAND.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DYE_COLOR, (byte) 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setDyeColor(DyeColor.byId(nbt.getByte("color")));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("color", (byte) this.getDyeColor().getId());
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

    public DyeColor getDyeColor() {
        return DyeColor.byId(this.entityData.get(DYE_COLOR) & 15);
    }

    public void setDyeColor(DyeColor color) {
        byte b0 = this.entityData.get(DYE_COLOR);
        this.entityData.set(DYE_COLOR, (byte) (b0 & 240 | color.getId() & 15));
    }

    @Override
    public ItemStack getPickResult() {
        return PalladiumItems.SUIT_STAND.get().getDefaultInstance();
    }
}
