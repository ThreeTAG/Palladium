package net.threetag.threecore.entity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class ProjectileEntity extends ThrowableEntity implements IRendersAsItem, IEntityAdditionalSpawnData {

    public float damage = 3F;
    public float gravityVelocity = 0.03F;
    public boolean dieOnBlockHit = true;
    public boolean dieOnEntityHit = true;
    public boolean particles = true;
    public ProjectileRenderInfo renderInfo = new ProjectileRenderInfo(new ItemStack(Blocks.STONE));

    public ProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ProjectileEntity(World world, double x, double y, double z) {
        super(TCEntityTypes.PROJECTILE.get(), x, y, z, world);
    }

    public ProjectileEntity(World world, LivingEntity livingEntity) {
        super(TCEntityTypes.PROJECTILE.get(), livingEntity, world);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected float getGravityVelocity() {
        return this.gravityVelocity;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.damage);

            if (this.dieOnEntityHit && !this.world.isRemote) {
                if (this.particles)
                    this.world.setEntityState(this, (byte) 3);
                this.remove();
            }
        }

        if (result.getType() == RayTraceResult.Type.BLOCK && this.dieOnBlockHit && !this.world.isRemote) {
            if (this.particles)
                this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack stack = this.getRenderedItem();
        return stack.isEmpty() ? ParticleTypes.CLOUD : new ItemParticleData(ParticleTypes.ITEM, stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte state) {
        if (state == 3) {
            IParticleData particle = this.makeParticle();

            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(particle, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.isRemote && this.renderInfo.isParticles()) {
            Random random = new Random();
            float sX = (random.nextFloat() - 0.5F) * this.renderInfo.getParticleSpread();
            float sY = (random.nextFloat() - 0.5F) * this.renderInfo.getParticleSpread();
            float sZ = (random.nextFloat() - 0.5F) * this.renderInfo.getParticleSpread();
            try {
                this.world.addParticle(this.renderInfo.getParticleType().getDeserializer().deserialize(this.renderInfo.getParticleType(), new StringReader(this.renderInfo.particleOptions)), this.getPosX(), this.getPosY(), this.getPosZ(), sX, sY, sZ);
            } catch (CommandSyntaxException e) {
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("Damage", this.damage);
        compound.putFloat("GravityVelocity", this.gravityVelocity);
        compound.putBoolean("DieOnEntityHit", this.dieOnEntityHit);
        compound.putBoolean("DieOnBlockHit", this.dieOnBlockHit);
        compound.putBoolean("Particles", this.particles);
        compound.put("RenderInfo", this.renderInfo.serializeNBT());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Damage", Constants.NBT.TAG_ANY_NUMERIC))
            this.damage = compound.getInt("Damage");
        if (compound.contains("GravityVelocity", Constants.NBT.TAG_ANY_NUMERIC))
            this.gravityVelocity = compound.getInt("gravityVelocity");
        if (compound.contains("DieOnEntityHit"))
            this.dieOnEntityHit = compound.getBoolean("DieOnEntityHit");
        if (compound.contains("DieOnBlockHit"))
            this.dieOnBlockHit = compound.getBoolean("DieOnBlockHit");
        if (compound.contains("Particles"))
            this.particles = compound.getBoolean("Particles");
        this.renderInfo = new ProjectileRenderInfo(compound.getCompound("RenderInfo"));
    }

    public ItemStack getRenderedItem() {
        return this.renderInfo.isItem() ? this.renderInfo.getStack() : ItemStack.EMPTY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getItem() {
        return this.getRenderedItem();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeCompoundTag(this.renderInfo.serializeNBT());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        CompoundNBT nbt = additionalData.readCompoundTag();
        this.renderInfo = new ProjectileRenderInfo(nbt);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static class ProjectileRenderInfo implements INBTSerializable<CompoundNBT> {

        /**
         * 0 = nothing
         * 1 = item
         * 2 = model layer
         * 3 = energy
         * 4 = particles
         */
        private int type;
        private ItemStack stack;
        private ResourceLocation modelLayer;
        private Color color;
        private ParticleType<?> particleType;
        private float particleSpread;
        private String particleOptions;

        public ProjectileRenderInfo(ItemStack stack) {
            this.type = 1;
            this.stack = stack;
        }

        public ProjectileRenderInfo(ResourceLocation modelLayer) {
            this.type = 2;
            this.modelLayer = modelLayer;
        }

        public ProjectileRenderInfo(Color energyColor) {
            this.type = 3;
            this.color = energyColor;
        }

        public ProjectileRenderInfo(ParticleType particleType, float particleSpread) {
            this.type = 4;
            this.particleType = particleType;
            this.particleSpread = particleSpread;
        }

        public ProjectileRenderInfo(CompoundNBT nbt) {
            this.deserializeNBT(nbt);
        }

        public boolean isItem() {
            return this.type == 1 && this.stack != null;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public boolean isModelLayer() {
            return this.type == 2 && this.modelLayer != null;
        }

        public ResourceLocation getModelLayer() {
            return this.modelLayer;
        }

        public boolean isEnergy() {
            return this.type == 3;
        }

        public Color getColor() {
            return this.color;
        }

        public boolean isParticles() {
            return this.type == 4 && this.particleType != null;
        }

        public ParticleType getParticleType() {
            return particleType;
        }

        public float getParticleSpread() {
            return particleSpread;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("Type", this.type);
            if (this.isItem()) {
                nbt.put("Item", this.stack.serializeNBT());
            } else if (this.getModelLayer() != null) {
                nbt.putString("ModelLayer", this.modelLayer.toString());
            } else if (this.isEnergy()) {
                nbt.putIntArray("EnergyColor", new int[]{this.color.getRed(), this.color.getGreen(), this.getColor().getBlue()});
            } else if (this.isParticles()) {
                nbt.putString("ParticleType", ForgeRegistries.PARTICLE_TYPES.getKey(this.particleType).toString());
                nbt.putFloat("ParticleSpread", this.particleSpread);
                nbt.putString("ParticleOptions", this.particleOptions);
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if (nbt.contains("Type")) {
                this.type = nbt.getInt("Type");

                if (type == 1) {
                    this.stack = nbt.get("Item") instanceof CompoundNBT ? ItemStack.read(nbt.getCompound("Item")) : new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("Item"))));
                } else if (type == 2) {
                    this.modelLayer = new ResourceLocation(nbt.getString("ModelLayer"));
                } else if (type == 3) {
                    if (nbt.keySet().contains("EnergyColor") && nbt.getIntArray("EnergyColor").length == 3)
                        this.color = new Color(nbt.getIntArray("EnergyColor")[0], nbt.getIntArray("EnergyColor")[1], nbt.getIntArray("EnergyColor")[2]);
                    else
                        this.color = Color.RED;
                } else if (type == 4) {
                    this.particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(nbt.getString("ParticleType")));
                    this.particleSpread = nbt.getFloat("ParticleSpread");
                    this.particleOptions = nbt.getString("ParticleOptions");
                }
            } else {
                if (nbt.contains("Item")) {
                    this.type = 1;
                    this.stack = nbt.get("Item") instanceof CompoundNBT ? ItemStack.read(nbt.getCompound("Item")) : new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("Item"))));
                } else if (nbt.contains("ModelLayer")) {
                    this.type = 2;
                    this.modelLayer = new ResourceLocation(nbt.getString("ModelLayer"));
                } else if (nbt.contains("EnergyColor")) {
                    this.type = 3;
                    if (nbt.keySet().contains("EnergyColor") && nbt.getIntArray("EnergyColor").length == 3)
                        this.color = new Color(nbt.getIntArray("EnergyColor")[0], nbt.getIntArray("EnergyColor")[1], nbt.getIntArray("EnergyColor")[2]);
                    else
                        this.color = Color.RED;
                } else if (nbt.contains("ParticleType")) {
                    this.type = 4;
                    this.particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(nbt.getString("ParticleType")));
                    this.particleSpread = nbt.getFloat("ParticleSpread");
                    this.particleOptions = nbt.getString("ParticleOptions");
                }
            }
        }
    }
}
