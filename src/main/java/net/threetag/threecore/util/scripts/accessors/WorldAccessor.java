package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.util.scripts.ScriptParameterName;

public class WorldAccessor extends ScriptAccessor<World> {

    public WorldAccessor(World value) {
        super(value);
    }

    public long getTime() {
        return this.value.getDayTime();
    }

    public void setTime(@ScriptParameterName("time") long time) {
        this.value.setDayTime(time);
    }

    public boolean isRaining() {
        return this.value.isRaining();
    }

    public boolean isThundering() {
        return this.value.isThundering();
    }

    public void setRainStrength(@ScriptParameterName("strength") float strength) {
        this.value.setRainStrength(strength);
    }

    public void summonLightning(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z, @ScriptParameterName("effectOnly") boolean effectOnly) {
        if (this.value instanceof ServerWorld)
            ((ServerWorld) this.value).addLightningBolt(new LightningBoltEntity(this.value, x, y, z, effectOnly));
    }

    public void setBlockState(@ScriptParameterName("block") Object block, @ScriptParameterName("x") int x, @ScriptParameterName("y") int y, @ScriptParameterName("z") int z) {
        BlockState b = block instanceof BlockStateAccessor ? ((BlockStateAccessor) block).value :
                (block instanceof String && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation((String) block)) ?
                        ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String) block)).getDefaultState() : null);
        if (block != null) {
            this.value.setBlockState(new BlockPos(x, y, z), b);
        }
    }

    public BlockStateAccessor getBlockState(@ScriptParameterName("x") int x, @ScriptParameterName("y") int y, @ScriptParameterName("z") int z) {
        return (BlockStateAccessor) ScriptAccessor.makeAccessor(this.value.getBlockState(new BlockPos(x, y, z)));
    }

}
