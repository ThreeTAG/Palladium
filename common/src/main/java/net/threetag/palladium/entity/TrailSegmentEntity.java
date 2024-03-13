package net.threetag.palladium.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.client.renderer.trail.AfterImageTrailRenderer;
import net.threetag.palladium.client.renderer.trail.TrailRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TrailSegmentEntity<T extends TrailRenderer.SegmentCache> extends LivingEntity {

    public LivingEntity parent;
    public EntityDimensions dimensions;
    public boolean mimicPlayer;
    public TrailRenderer<T> trailRenderer;
    public T cache;
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

    @SuppressWarnings("unchecked")
    public TrailSegmentEntity(LivingEntity parent, TrailRenderer<T> trailRenderer) {
        this(PalladiumEntityTypes.TRAIL_SEGMENT.get(), parent.level());
        this.parent = parent;
        this.lifetime = trailRenderer.getLifetime();
        this.dimensions = EntityDimensions.fixed(parent.getBbWidth(), parent.getBbHeight());
        this.refreshDimensions();
        this.setPos(parent.getPosition(Minecraft.getInstance().getFrameTime()));
        this.setXRot(parent.getXRot());
        this.setYRot(parent.getYRot());
        this.setYBodyRot(parent.yBodyRot);
        this.yBodyRotO = parent.yBodyRotO;
        this.setYHeadRot(parent.getYHeadRot());
        this.yHeadRotO = parent.yHeadRotO;
        this.xo = parent.xo;
        this.yo = parent.yo;
        this.zo = parent.zo;
        this.xRotO = parent.getXRot();
        this.yRotO = parent.getYRot();
        this.mainArm = parent.getMainArm();
        this.walkAnimation.speed = parent.walkAnimation.speed;
        this.walkAnimation.speedOld = parent.walkAnimation.speedOld;
        this.walkAnimation.position = parent.walkAnimation.position;
        this.trailRenderer = trailRenderer;
        this.mimicPlayer = trailRenderer instanceof AfterImageTrailRenderer ai && ai.mimicPlayer;
        this.cache = (T) this.trailRenderer.createCache();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.items.put(slot, mimicPlayer ? parent.getItemBySlot(slot).copy() : ItemStack.EMPTY);
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
    public EntityDimensions getDimensions(Pose pose) {
        return this.dimensions;
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
