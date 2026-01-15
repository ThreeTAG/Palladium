package net.threetag.palladium.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class EffectEntity extends Entity implements IEntityWithComplexSpawn {

    private static final EntityDataAccessor<Boolean> IS_DONE_PLAYING = SynchedEntityData.defineId(EffectEntity.class, EntityDataSerializers.BOOLEAN);

    public int anchorId;
    public Entity anchor;
    public EntityEffect entityEffect;
    public CompoundTag customData;

    public EffectEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public EffectEntity(Level worldIn, Entity anchor, EntityEffect entityEffect) {
        this(PalladiumEntityTypes.EFFECT.get(), worldIn);
        this.anchorId = anchor.getId();
        this.entityEffect = entityEffect;
        this.customData = new CompoundTag();
        this.snapTo(anchor.position(), anchor.getYRot(), anchor.getXRot());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(IS_DONE_PLAYING, false);
    }

    public Entity getAnchorEntity() {
        if (this.anchor == null) {
            this.anchor = this.level().getEntity(this.anchorId);
        }
        return this.anchor;
    }

    @Override
    public void tick() {
        Entity anchor = getAnchorEntity();
        if (anchor != null && this.entityEffect != null) {
            if (!anchor.isAlive() || this.isDonePlaying()) {
                this.discard();
            } else {
                this.entityEffect.tick(this, anchor);
                this.setOldPosAndRot(anchor.position(), anchor.getYRot(), anchor.getXRot());
            }
        } else {
            this.discard();
        }
    }

    public boolean isDonePlaying() {
        return this.entityData.get(IS_DONE_PLAYING);
    }

    public void setDonePlaying(boolean donePlaying) {
        this.entityData.set(IS_DONE_PLAYING, donePlaying);
    }

    public CompoundTag getExtraData() {
        return this.customData;
    }

    public void setExtraData(CompoundTag data) {
        this.customData = data;
    }

    public void changeExtraData(Consumer<CompoundTag> consumer) {
        CompoundTag data = this.getExtraData();
        consumer.accept(data);
        this.setExtraData(data);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
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
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityEffect = PalladiumRegistries.ENTITY_EFFECT.getValue(Identifier.parse(input.getStringOr("entity_effect", "")));
        this.anchorId = input.getIntOr("anchor_id", -1);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putString("entity_effect", Objects.requireNonNull(PalladiumRegistries.ENTITY_EFFECT.getKey(this.entityEffect)).toString());
        output.putInt("anchor_id", this.anchorId);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeIdentifier(Objects.requireNonNull(PalladiumRegistries.ENTITY_EFFECT.getKey(this.entityEffect)));
        buffer.writeInt(this.anchorId);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.entityEffect = PalladiumRegistries.ENTITY_EFFECT.getValue(additionalData.readIdentifier());
        this.anchorId = additionalData.readInt();
    }
}
