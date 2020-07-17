package net.threetag.threecore.scripts.accessors;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.capability.CapabilityThreeData;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.sizechanging.SizeChangeType;
import net.threetag.threecore.util.EntityUtil;
import net.threetag.threecore.util.PlayerUtil;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class EntityAccessor extends ScriptAccessor<Entity> {

    public final WorldAccessor world;

    public EntityAccessor(Entity value) {
        super(value);
        this.world = (WorldAccessor) makeAccessor(value.world);
    }

    public String getName() {
        return this.value.getDisplayName().getString();
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

    public CompoundNBTAccessor getNBTData(){ return new CompoundNBTAccessor(this.value.serializeNBT()); }

    public Vector3dAccessor getPosition() {
        return new Vector3dAccessor(this.value.getPositionVec());
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

    public Vector3dAccessor getLookVec() {
        return new Vector3dAccessor(this.value.getLookVec());
    }

    public Vector3dAccessor getMotion() {
        return new Vector3dAccessor(this.value.getMotion());
    }

    public void setMotion(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        this.value.setMotion(x, y, z);
    }

    public void addMotion(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        this.setMotion(this.value.getMotion().x + x, this.value.getMotion().y + y, this.value.getMotion().z + z);
    }

    public void setPlayerMotion(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        if(!this.world.value.isRemote && this.value instanceof ServerPlayerEntity)
        {
            this.value.setMotion(x, y, z);
            ((ServerPlayerEntity) this.value).connection.sendPacket(new SEntityVelocityPacket(this.value));
        }
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
        this.value.sendMessage(new StringTextComponent(message), this.value.getUniqueID());
    }

    public void sendTranslatedMessage(@ScriptParameterName("translationKey") String message, @ScriptParameterName("args") Object... args) {
        this.value.sendMessage(new TranslationTextComponent(message, args), this.value.getUniqueID());
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

    public LivingEntityAccessor getAsLiving() { return new LivingEntityAccessor((LivingEntity) this.value); }

    public String getType(){ return this.value.getType().getRegistryName().toString(); }

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
        return this.value.func_233570_aj_();
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
        return this.value.func_230279_az_();
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

        if (data instanceof FloatThreeData) {
            if (value instanceof Double)
                value = ((Double) value).floatValue();
            else if (value instanceof Integer)
                value = ((Integer) value).floatValue();
        }

        threeData.set(data, value);
        return true;
    }

    public Object rayTrace(@ScriptParameterName("distance") double distance, @ScriptParameterName("blockMode") String blockMode, @ScriptParameterName("fluidMode") String fluidMode){
       RayTraceContext.BlockMode b = RayTraceContext.BlockMode.valueOf(blockMode.toUpperCase());
       RayTraceContext.FluidMode f = RayTraceContext.FluidMode.valueOf(fluidMode.toUpperCase());
       return ScriptAccessor.makeAccessor(EntityUtil.rayTraceWithEntities(this.value, distance, b, f));
    }

    public void lookAt(@ScriptParameterName("target") Vector3dAccessor target){
        this.value.lookAt(EntityAnchorArgument.Type.EYES, target.value);
    }

}
