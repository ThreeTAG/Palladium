package net.threetag.palladium.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TrailSegmentEntity extends LivingEntity {

    public LivingEntity parent;
    public Object renderer;
    public Object model;
    public ResourceLocation texture;
    public float partialTick = -1;
    public int lifetime = -1;

    public boolean fetchedAnimationValues = false;
    public float limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, renderYawOffset;
    public Map<EquipmentSlot, ItemStack> items = new HashMap<>();
    public HumanoidArm mainArm = HumanoidArm.RIGHT;

    public TrailSegmentEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public TrailSegmentEntity(LivingEntity parent, int lifetime) {
        this(PalladiumEntityTypes.TRAIL_SEGMENT.get(), parent.level());
        this.parent = parent;
        this.lifetime = lifetime;
        this.setPos(parent.position());
        this.setXRot(parent.getXRot());
        this.setYRot(parent.getYRot());
        this.setYBodyRot(parent.yBodyRot);
        this.setYHeadRot(parent.getYHeadRot());
        this.xo = parent.xo;
        this.yo = parent.yo;
        this.zo = parent.zo;
        this.xRotO = parent.getXRot();
        this.yRotO = parent.getYRot();
        this.mainArm = parent.getMainArm();
        this.walkAnimation.speed = parent.walkAnimation.speed;
        this.walkAnimation.speedOld = parent.walkAnimation.speedOld;
        this.walkAnimation.position = parent.walkAnimation.position;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.items.put(slot, parent.getItemBySlot(slot).copy());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= this.lifetime) {
            this.discard();
        }
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return this.items.keySet().stream().filter(EquipmentSlot::isArmor).map(s -> this.items.get(s)).collect(Collectors.toList());
    }

    @Override
    public @NotNull ItemStack getItemBySlot(EquipmentSlot slot) {
        return this.items.get(slot);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return this.mainArm;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    protected void pushEntities() {

    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
}
