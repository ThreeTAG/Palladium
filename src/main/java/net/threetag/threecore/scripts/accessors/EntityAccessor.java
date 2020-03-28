package net.threetag.threecore.scripts.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.sizechanging.SizeChangeType;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.PlayerUtil;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;
import net.threetag.threecore.capability.CapabilityThreeData;

public class EntityAccessor extends ScriptAccessor<Entity> {

    public final WorldAccessor world;

    public EntityAccessor(Entity value) {
        super(value);
        this.world = (WorldAccessor) makeAccessor(value.world);
    }

    public String getName() {
        return this.value.getDisplayName().getFormattedText();
    }

    public void setName(@ScriptParameterName("name") String name) {
        this.value.setCustomName(new StringTextComponent(name));
    }

    public void setNameVisible(@ScriptParameterName("visible") boolean visible) {
        this.value.setCustomNameVisible(visible);
    }

    public WorldAccessor getWorld() {
        return this.world;
    }

    public String getUUID() {
        return this.value.getUniqueID().toString();
    }

    public Vec3dAccessor getPosition() {
        return new Vec3dAccessor(this.value.getPositionVector());
    }

    public double getPosX() {
        return this.value.getPosX();
    }

    public double getPosY() {
        return this.value.getPosY();
    }

    public double getPosZ() {
        return this.value.getPosZ();
    }

    public float getYaw() {
        return this.value.rotationYaw;
    }

    public float setYaw(@ScriptParameterName("yaw") float yaw) {
        return this.value.rotationYaw = yaw;
    }

    public float getPitch() {
        return this.value.rotationPitch;
    }

    public void setPitch(@ScriptParameterName("pitch") float pitch) {
        this.value.rotationPitch = pitch;
    }

    public void setPosition(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        this.value.setPositionAndUpdate(x, y, z);
    }

    public void setRotation(@ScriptParameterName("yaw") float yaw, @ScriptParameterName("pitch") float pitch) {
        setPositionAndRotation(getPosX(), getPosY(), getPosZ(), yaw, pitch);
    }

    public void setPositionAndRotation(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z, @ScriptParameterName("yaw") float yaw, @ScriptParameterName("pitch") float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.value.setPositionAndUpdate(x, y, z);
    }

    public Vec3dAccessor getLookVec() {
        return new Vec3dAccessor(this.value.getLookVec());
    }

    public Vec3dAccessor getMotion() {
        return new Vec3dAccessor(this.value.getMotion());
    }

    public void setMotion(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        this.value.setMotion(x, y, z);
    }

    public void addMotion(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        this.setMotion(this.value.getMotion().x + x, this.value.getMotion().y + y, this.value.getMotion().z + z);
    }

    public Direction getHorizontalFacing() {
        return this.value.getHorizontalFacing();
    }

    public float getWidth() {
        return this.value.getWidth();
    }

    public float getHeight() {
        return this.value.getHeight();
    }

    public float getEyeHeight() {
        return this.value.getEyeHeight();
    }

    public int getTicksExisted() {
        return this.value.ticksExisted;
    }

    public void sendMessage(@ScriptParameterName("message") String message) {
        this.value.sendMessage(new StringTextComponent(message));
    }

    public void sendTranslatedMessage(@ScriptParameterName("translationKey") String message, @ScriptParameterName("args") Object... args) {
        this.value.sendMessage(new TranslationTextComponent(message, args));
    }

    public boolean isCrouching() {
        return this.value.isCrouching();
    }

    public boolean isAlive() {
        return this.value.isAlive();
    }

    public boolean isPlayer() {
        return this.value instanceof PlayerEntity;
    }

    public boolean isLiving() {
        return this.value instanceof LivingEntity;
    }

    public void kill() {
        this.value.onKillCommand();
    }

    public boolean isInvisible() {
        return this.value.isInvisible();
    }

    public void setInvisible(@ScriptParameterName("invisible") boolean invisible) {
        this.value.setInvisible(invisible);
    }

    public boolean isOnGround() {
        return this.value.onGround;
    }

    public float getFallDistance() {
        return this.value.fallDistance;
    }

    public void setFallDistance(@ScriptParameterName("fallDistance") float fallDistance) {
        this.value.fallDistance = fallDistance;
    }

    public boolean noClip() {
        return this.value.noClip;
    }

    public void setNoClip(@ScriptParameterName("noClip") boolean noClip) {
        this.value.noClip = noClip;
    }

    public boolean hasNoGravity() {
        return this.value.hasNoGravity();
    }

    public void setNoGravity(@ScriptParameterName("noGravity") boolean noGravity) {
        this.value.setNoGravity(noGravity);
    }

    public int executeCommand(@ScriptParameterName("command") String command) {
        if (this.value.world instanceof ServerWorld) {
            return ((ServerWorld) this.value.world).getServer().getCommandManager().handleCommand(this.value.getCommandSource(), command);
        }

        return 0;
    }

    public boolean startRiding(@ScriptParameterName("entity") EntityAccessor entity, @ScriptParameterName("force") boolean force) {
        return this.value.startRiding(entity.value, force);
    }

    public void removePassengers() {
        this.value.removePassengers();
    }

    public void stopRiding() {
        this.value.stopRiding();
    }

    public EntityAccessor[] getPassengers() {
        EntityAccessor[] passengers = new EntityAccessor[this.value.getPassengers().size()];
        for (int i = 0; i < passengers.length; i++) {
            passengers[i] = new EntityAccessor(this.value.getPassengers().get(i));
        }
        return passengers;
    }

    public boolean isPassenger(@ScriptParameterName("entity") EntityAccessor entity) {
        return this.value.isPassenger(entity.value);
    }

    public void setOnFire(@ScriptParameterName("seconds") int seconds) {
        this.value.setFire(seconds);
    }

    public boolean isImmuneToFire() {
        return this.value.isImmuneToFire();
    }

    public void extinguish() {
        this.value.extinguish();
    }

    public CompoundNBTAccessor getPersistentData() {
        return new CompoundNBTAccessor(this.value.getPersistentData());
    }

    public void playSoundAt(@ScriptParameterName("id") String id, @ScriptParameterName("volume") float volume, @ScriptParameterName("pitch") float pitch) {
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id));

        if (soundEvent != null) {
            PlayerUtil.playSoundToAll(this.value.world, this.getPosX(), this.getPosY() + this.getHeight() / 2D, this.getPosZ(), 50, soundEvent, this.value.getSoundCategory(), volume, pitch);
        }
    }

    public void startSizeChange(@ScriptParameterName("size") float size, @ScriptParameterName("sizeChangeType") String sizeChangeType) {
        this.value.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            SizeChangeType type = SizeChangeType.REGISTRY.getValue(new ResourceLocation(sizeChangeType));
            sizeChanging.startSizeChange(type, size);
        });
    }

    public void setSizeDirectly(@ScriptParameterName("size") float size, @ScriptParameterName("sizeChangeType") String sizeChangeType) {
        this.value.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            SizeChangeType type = SizeChangeType.REGISTRY.getValue(new ResourceLocation(sizeChangeType));
            sizeChanging.setSizeDirectly(type, size);
        });
    }

    public Object getThreeData(@ScriptParameterName("key") String key) {
        IThreeDataHolder threeData = this.value.getCapability(CapabilityThreeData.THREE_DATA).orElse(null);
        if (threeData == null)
            return null;
        ThreeData data = threeData.getDataByName(key);
        return data == null ? null : threeData.get(data);
    }

    public boolean setThreeData(@ScriptParameterName("key") String key, @ScriptParameterName("value") Object value) {
        IThreeDataHolder threeData = this.value.getCapability(CapabilityThreeData.THREE_DATA).orElse(null);
        if (threeData == null)
            return false;
        ThreeData data = threeData.getDataByName(key);
        if (data == null)
            return false;

        // ugly fix since JavaScript numbers are apparently always doubles?
        if (data instanceof IntegerThreeData) {
            if (value instanceof Double)
                value = ((Double) value).intValue();
            else if (value instanceof Float)
                value = ((Float) value).intValue();
        }

        threeData.set(data, value);
        return true;
    }

}
