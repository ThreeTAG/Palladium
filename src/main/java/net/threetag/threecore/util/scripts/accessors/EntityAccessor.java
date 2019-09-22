package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.sizechanging.SizeChangeType;
import net.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.player.PlayerHelper;

import java.util.UUID;

public class EntityAccessor {

    public final Entity entity;
    public final WorldAccessor world;

    public EntityAccessor(Entity entity) {
        this.entity = entity;
        this.world = new WorldAccessor(entity.world);
    }

    public String getName() {
        return this.entity.getDisplayName().getFormattedText();
    }

    public void setName(String name) {
        this.entity.setCustomName(new StringTextComponent(name));
    }

    public void setNameVisible(boolean visible) {
        this.entity.setCustomNameVisible(visible);
    }

    public WorldAccessor getWorld() {
        return this.world;
    }

    public UUID getUUID() {
        return this.entity.getUniqueID();
    }

    public double getPosX() {
        return this.entity.posX;
    }

    public double getPosY() {
        return this.entity.posY;
    }

    public double getPosZ() {
        return this.entity.posZ;
    }

    public float getYaw() {
        return this.entity.rotationYaw;
    }

    public float setYaw(float yaw) {
        return this.entity.rotationYaw = yaw;
    }

    public float getPitch() {
        return this.entity.rotationPitch;
    }

    public void setPitch(float pitch) {
        this.entity.rotationPitch = pitch;
    }

    public void setPosition(double x, double y, double z) {
        this.entity.setPositionAndUpdate(x, y, z);
    }

    public void setRotation(float yaw, float pitch) {
        setPositionAndRotation(getPosX(), getPosY(), getPosZ(), yaw, pitch);
    }

    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.entity.setPositionAndUpdate(x, y, z);
    }

    public Vec3d getMotion() {
        return this.entity.getMotion();
    }

    public void setMotion(double x, double y, double z) {
        this.entity.setMotion(x, y, z);
    }

    public void addMotion(double x, double y, double z) {
        this.setMotion(this.entity.getMotion().x + x, this.entity.getMotion().y + y, this.entity.getMotion().z + z);
    }

    public Direction getHorizontalFacing() {
        return this.entity.getHorizontalFacing();
    }

    public float getWidth() {
        return this.entity.getWidth();
    }

    public float getHeight() {
        return this.entity.getHeight();
    }

    public float getEyeHeight() {
        return this.entity.getEyeHeight();
    }

    public int getTicksExisted() {
        return this.entity.ticksExisted;
    }

    public void sendMessage(String message) {
        this.entity.sendMessage(new StringTextComponent(message));
    }

    public void sendTranslatedMessage(String message, Object... args) {
        this.entity.sendMessage(new TranslationTextComponent(message, args));
    }

    public boolean isSneaking() {
        return this.entity.isSneaking();
    }

    public boolean isAlive() {
        return this.entity.isAlive();
    }

    public boolean isPlayer() {
        return this.entity instanceof PlayerEntity;
    }

    public boolean isLiving() {
        return this.entity instanceof LivingEntity;
    }

    public void kill() {
        this.entity.onKillCommand();
    }

    public boolean isInvisible() {
        return this.entity.isInvisible();
    }

    public void setInvisible(boolean invisible) {
        this.entity.setInvisible(invisible);
    }

    public boolean isOnGround() {
        return this.entity.onGround;
    }

    public float getFallDistance() {
        return this.entity.fallDistance;
    }

    public void setFallDistance(float fallDistance) {
        this.entity.fallDistance = fallDistance;
    }

    public boolean noClip() {
        return this.entity.noClip;
    }

    public void setNoClip(boolean noClip) {
        this.entity.noClip = noClip;
    }

    public boolean hasNoGravity() {
        return this.entity.hasNoGravity();
    }

    public void setNoGravity(boolean noGravity) {
        this.entity.setNoGravity(noGravity);
    }

    public int executeCommand(String command) {
        if (this.entity.world instanceof ServerWorld) {
            return ((ServerWorld) this.entity.world).getServer().getCommandManager().handleCommand(this.entity.getCommandSource(), command);
        }

        return 0;
    }

    public boolean startRiding(EntityAccessor entity, boolean force) {
        return this.entity.startRiding(entity.entity, force);
    }

    public void removePassengers() {
        this.entity.removePassengers();
    }

    public void stopRiding() {
        entity.stopRiding();
    }

    public EntityAccessor[] getPassengers() {
        EntityAccessor[] passengers = new EntityAccessor[this.entity.getPassengers().size()];
        for (int i = 0; i < passengers.length; i++) {
            passengers[i] = new EntityAccessor(this.entity.getPassengers().get(i));
        }
        return passengers;
    }

    public boolean isPassenger(EntityAccessor entity) {
        return this.entity.isPassenger(entity.entity);
    }

    public void setOnFire(int seconds) {
        this.entity.setFire(seconds);
    }

    public boolean isImmuneToFire() {
        return this.entity.isImmuneToFire();
    }

    public void extinguish() {
        this.entity.extinguish();
    }

    public void playSoundAt(String id, float volume, float pitch) {
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id));

        if (soundEvent != null) {
            PlayerHelper.playSoundToAll(this.entity.world, this.getPosX(), this.getPosY() + this.getHeight() / 2D, this.getPosZ(), 50, soundEvent, this.entity.getSoundCategory(), volume, pitch);
        }
    }

    public void startSizeChange(float size, String sizeChangeType) {
        this.entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            SizeChangeType type = SizeChangeType.REGISTRY.getValue(new ResourceLocation(sizeChangeType));
            sizeChanging.startSizeChange(type, size);
        });
    }

    public void setSizeDirectly(float size, String sizeChangeType) {
        this.entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            SizeChangeType type = SizeChangeType.REGISTRY.getValue(new ResourceLocation(sizeChangeType));
            sizeChanging.setSizeDirectly(type, size);
        });
    }

    @Override
    public boolean equals(Object obj) {
        return this.entity.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.entity.hashCode();
    }

}
