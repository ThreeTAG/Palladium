package net.threetag.threecore.scripts.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.common.extensions.IForgeWorldServer;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.scripts.ScriptCommandSource;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.PlayerUtil;

import java.util.List;
import java.util.Random;

public class WorldAccessor extends ScriptAccessor<World> {

    public WorldAccessor(World value) {
        super(value);
    }

    public long getTime() {
        return this.value.getDayTime();
    }

    public void setTime(@ScriptParameterName("time") long time) {
        if (this.value instanceof ServerWorld)
            ((ServerWorld) this.value).setDayTime(time);
    }

    public String getDimension() {
        return this.value.getDimensionKey().toString();
    }

    public void loadStructure(@ScriptParameterName("x") int x, @ScriptParameterName("y") int y, @ScriptParameterName("z") int z, @ScriptParameterName("name") String name) {
        if(this.value instanceof ServerWorld) {
            BlockPos pos = new BlockPos(x, y, z);
            ServerWorld worldserver = (ServerWorld) this.value;
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            ResourceLocation loc = new ResourceLocation(name);
            Template template = templatemanager.getTemplate(loc);
            if (template != null) {
                BlockState state = worldserver.getBlockState(pos);
                worldserver.notifyBlockUpdate(pos, state, state, 3);
                PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
                        .setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk(null);
                template.func_237144_a_(worldserver, pos.add(0, 1, 0), placementsettings, new Random());
            }
        }
    }

    public boolean isRaining() {
        return this.value.isRaining();
    }

    public boolean isThundering() {
        return this.value.isThundering();
    }

    public boolean isRemote() {
        return this.value.isRemote;
    }

    public void setRainStrength(@ScriptParameterName("strength") float strength) {
        this.value.setRainStrength(strength);
    }

    public void playSound(@ScriptParameterName("id") String id, @ScriptParameterName("id") String soundCategory, @ScriptParameterName("posX") double posX, @ScriptParameterName("posY") double posY, @ScriptParameterName("posZ") double posZ, @ScriptParameterName("volume") float volume, @ScriptParameterName("pitch") float pitch) {
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id));
        SoundCategory category = null;

        for (SoundCategory category1 : SoundCategory.values()) {
            if (category1.getName().equalsIgnoreCase(soundCategory)) {
                category = category1;
                break;
            }
        }

        if (soundEvent != null && category != null) {
            PlayerUtil.playSoundToAll(this.value, posX, posY, posZ, 50, soundEvent, category, volume, pitch);
        }
    }

    public void summonLightning(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z, @ScriptParameterName("effectOnly") boolean effectOnly) {
        if (this.value instanceof ServerWorld) {
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.value);
            lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(new BlockPos(x, y, z)));
            lightningboltentity.setEffectOnly(effectOnly);
            this.value.addEntity(lightningboltentity);
        }
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
            CommandSource commandSource = new CommandSource(new ScriptCommandSource(), Vector3d.copy(((ServerWorld) this.value).getSpawnPoint()), Vector2f.ZERO, (ServerWorld) value, 4, "Script", new StringTextComponent("Script"), this.value.getServer(), (Entity) null);
            this.value.getServer().getCommandManager().handleCommand(commandSource, command);
        }
    }

    public EntityAccessor[] getEntitiesInBox(@ScriptParameterName("x1") double x1, @ScriptParameterName("y1") double y1, @ScriptParameterName("z1") double z1, @ScriptParameterName("x2") double x2, @ScriptParameterName("y2") double y2, @ScriptParameterName("z2") double z2) {
        List<Entity> list = this.value.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x1, y1, z1, x2, y2, z2), t -> true);
        EntityAccessor[] array = new EntityAccessor[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = new EntityAccessor(list.get(i));
        return array;
    }

    public LivingEntityAccessor[] getLivingEntitiesInBox(@ScriptParameterName("x1") double x1, @ScriptParameterName("y1") double y1, @ScriptParameterName("z1") double z1, @ScriptParameterName("x2") double x2, @ScriptParameterName("y2") double y2, @ScriptParameterName("z2") double z2) {
        List<Entity> list = this.value.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(x1, y1, z1, x2, y2, z2), t -> true);
        LivingEntityAccessor[] array = new LivingEntityAccessor[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = new LivingEntityAccessor((LivingEntity) list.get(i));
        return array;
    }
}
