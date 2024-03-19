package net.threetag.palladium.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.trail.AfterImageTrailRenderer;
import net.threetag.palladium.client.renderer.trail.TrailRenderer;
import net.threetag.palladium.util.SizeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrailSegmentEntity<T extends TrailRenderer.SegmentCache> extends LivingEntity {

    public Entity parent;
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
    public float scaleHeight = 1F;
    public float scaleWidth = 1F;
    public float scale = 1F;
    public boolean snapshotsGathered = false;
    private final List<Object> renderLayerSnapshots = new ArrayList<>();

    public TrailSegmentEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @SuppressWarnings("unchecked")
    public TrailSegmentEntity(Entity parent, TrailRenderer<T> trailRenderer) {
        this(PalladiumEntityTypes.TRAIL_SEGMENT.get(), parent.level());

        float partialTick = Minecraft.getInstance().getFrameTime();
        this.parent = parent;
        this.lifetime = trailRenderer.getLifetime();
        this.dimensions = EntityDimensions.fixed(parent.getBbWidth(), parent.getBbHeight());
        this.refreshDimensions();
        this.setPos(parent.getPosition(partialTick));
        this.setXRot(parent.getXRot());
        this.setYRot(parent.getYRot());
        this.setYHeadRot(parent.getYHeadRot());
        this.xo = parent.xo;
        this.yo = parent.yo;
        this.zo = parent.zo;
        this.xRotO = parent.getXRot();
        this.yRotO = parent.getYRot();
        this.trailRenderer = trailRenderer;
        this.mimicPlayer = trailRenderer instanceof AfterImageTrailRenderer ai && ai.mimicPlayer;
        this.cache = (T) this.trailRenderer.createCache();
        this.scaleWidth = SizeUtil.getInstance().getWidthScale(parent, partialTick);
        this.scaleHeight = SizeUtil.getInstance().getHeightScale(parent, partialTick);
        this.scale = (this.scaleWidth + this.scaleHeight) / 2F;

        if (parent instanceof LivingEntity living) {
            this.setYBodyRot(living.yBodyRot);
            this.yBodyRotO = living.yBodyRotO;
            this.yHeadRotO = living.yHeadRotO;
            this.mainArm = living.getMainArm();
            this.walkAnimation.speed = living.walkAnimation.speed;
            this.walkAnimation.speedOld = living.walkAnimation.speedOld;
            this.walkAnimation.position = living.walkAnimation.position;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                this.items.put(slot, mimicPlayer ? living.getItemBySlot(slot).copy() : ItemStack.EMPTY);
            }
        } else {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                this.items.put(slot, ItemStack.EMPTY);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void addSnapshot(IPackRenderLayer.Snapshot snapshot) {
        this.renderLayerSnapshots.add(snapshot);
    }

    @Environment(EnvType.CLIENT)
    public List<Object> getRenderLayerSnapshots() {
        return this.renderLayerSnapshots;
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

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(50, 50, 50);
    }
}
