package net.threetag.threecore.scripts.accessors;

import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.scripts.ScriptCommandSource;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.PlayerUtil;

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

    public void playSound(@ScriptParameterName("id") String id, @ScriptParameterName("id") String soundCategory, @ScriptParameterName("posX") double posX, @ScriptParameterName("posY") double posY, @ScriptParameterName("posZ") double posZ, @ScriptParameterName("volume") float volume, @ScriptParameterName("pitch") float pitch) {
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id));
        SoundCategory category = null;
        try {
            category = SoundCategory.valueOf(soundCategory);
        } catch (Exception ignored) {

        }

        if (soundEvent != null && category != null) {
            PlayerUtil.playSoundToAll(this.value, posX, posY, posZ, 50, soundEvent, category, volume, pitch);
        }
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

    public void executeCommand(@ScriptParameterName("command") String command) {
        if (this.value instanceof ServerWorld) {
            CommandSource commandSource = new CommandSource(new ScriptCommandSource(), new Vec3d(value.getSpawnPoint()), Vec2f.ZERO, (ServerWorld) value, 4, "Script", new StringTextComponent("Script"), this.value.getServer(), (Entity) null);
            this.value.getServer().getCommandManager().handleCommand(commandSource, command);
        }
    }

}
