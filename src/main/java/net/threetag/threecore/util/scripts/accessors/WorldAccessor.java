package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WorldAccessor {

    public final World world;

    public WorldAccessor(World world) {
        this.world = world;
    }

    public long getTime() {
        return this.world.getDayTime();
    }

    public void setTime(long time) {
        this.world.setDayTime(time);
    }

    public boolean isRaining() {
        return this.world.isRaining();
    }

    public boolean isThundering() {
        return this.world.isThundering();
    }

    public void setRainStrength(float strength) {
        this.world.setRainStrength(strength);
    }

    public void summonLightning(double x, double y, double z, boolean effectOnly) {
        if (this.world instanceof ServerWorld)
            ((ServerWorld) this.world).addLightningBolt(new LightningBoltEntity(world, x, y, z, effectOnly));
    }

    @Override
    public boolean equals(Object obj) {
        return this.world.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.world.hashCode();
    }

}
