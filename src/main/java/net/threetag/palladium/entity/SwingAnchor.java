package net.threetag.palladium.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.SwingingFlightType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SwingAnchor extends Entity {

    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(SwingAnchor.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> BEAM_RENDERER_ID = SynchedEntityData.defineId(SwingAnchor.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DESPAWN = SynchedEntityData.defineId(SwingAnchor.class, EntityDataSerializers.BOOLEAN);

    private LivingEntity owner;
    private int despawnTimer = 0;
    public Map<Object, Vec3> beamAnchorCache = new HashMap<>();

    public SwingAnchor(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public SwingAnchor(LivingEntity owner, BlockPos pos, Identifier beamRendererId) {
        this(PalladiumEntityTypes.SWING_ANCHOR.get(), owner.level());
        this.snapTo(pos.getCenter());
        this.setOwner(owner);
        this.setBeamRendererId(beamRendererId);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_ID, -1);
        builder.define(BEAM_RENDERER_ID, "");
        builder.define(DESPAWN, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            if (this.owner == null || !this.owner.isAlive()) {
                this.discard();
                return;
            }

            if (EntityFlightHandler.get(this.owner).getController() instanceof SwingingFlightType.Controller swinging) {
                var blockPos = this.blockPosition();

                if (!blockPos.equals(swinging.getAnchor())) {
                    this.markToDespawn();
                }
            } else {
                this.markToDespawn();
            }
        }

        if (this.isMarkedToDespawn()) {
            this.despawnTimer++;

            if (this.despawnTimer >= 20) {
                this.discard();
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    private void setOwner(LivingEntity entity) {
        this.owner = entity;
        this.entityData.set(OWNER_ID, entity.getId());
    }

    public LivingEntity getOwner() {
        if (this.owner == null && this.level().getEntity(this.entityData.get(OWNER_ID)) instanceof LivingEntity living) {
            this.owner = living;
        }

        return this.owner;
    }

    public void setBeamRendererId(Identifier id) {
        this.entityData.set(BEAM_RENDERER_ID, id.toString());
    }

    public Identifier getBeamRendererId() {
        var id = this.entityData.get(BEAM_RENDERER_ID);
        return id.isEmpty() ? null : Identifier.parse(id);
    }

    public void markToDespawn() {
        this.entityData.set(DESPAWN, true);
    }

    public boolean isMarkedToDespawn() {
        return this.entityData.get(DESPAWN);
    }

    public float getOpacity(float partialTicks) {
        if (this.isMarkedToDespawn()) {
            return 1.0F - Math.min((this.despawnTimer + partialTicks) / 20.0F, 1.0F);
        }
        return 1.0F;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
    }

    @Override
    protected boolean couldAcceptPassenger() {
        return false;
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

}
