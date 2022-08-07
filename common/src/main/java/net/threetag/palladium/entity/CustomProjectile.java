package net.threetag.palladium.entity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.extensions.network.EntitySpawnExtension;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;

public class CustomProjectile extends ThrowableProjectile implements EntitySpawnExtension {

    public static final Map<String, Function<CompoundTag, Appearance>> APPEARANCE_REGISTRY = new HashMap<>();
    public float damage = 3F;
    public float gravity = 0.03F;
    public boolean dieOnBlockHit = true;
    public boolean dieOnEntityHit = true;
    public EntityDimensions dimensions = new EntityDimensions(0.1F, 0.1F, false);
    public List<Appearance> appearances = new ArrayList<>();

    static {
        APPEARANCE_REGISTRY.put("item", ItemAppearance::new);
        APPEARANCE_REGISTRY.put("particles", ParticleAppearance::new);
        APPEARANCE_REGISTRY.put("laser", LaserAppearance::new);
    }

    public CustomProjectile(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.dimensions;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected float getGravity() {
        return this.gravity;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        entity.hurt(DamageSource.thrown(this, this.getOwner()), this.damage);

        if (this.dieOnEntityHit && !this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.dieOnBlockHit && !this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte state) {
        if (state == 3) {
            for (Appearance appearance : this.appearances) {
                appearance.spawnParticlesOnHit(this);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        for (Appearance appearance : this.appearances) {
            appearance.onTick(this);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Damage", this.damage);
        compound.putFloat("Gravity", this.gravity);
        compound.putBoolean("DieOnEntityHit", this.dieOnEntityHit);
        compound.putBoolean("DieOnBlockHit", this.dieOnBlockHit);
        compound.putFloat("Size", this.dimensions.width);

        ListTag appearanceList = new ListTag();
        for (Appearance appearance : this.appearances) {
            CompoundTag aTag = new CompoundTag();
            aTag.putString("Type", appearance.getId());
            appearance.toNBT(aTag);
            appearanceList.add(aTag);
        }
        compound.put("Appearances", appearanceList);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Damage", 99))
            this.damage = compound.getFloat("Damage");
        if (compound.contains("Gravity", 99))
            this.gravity = compound.getFloat("Gravity");
        if (compound.contains("DieOnEntityHit"))
            this.dieOnEntityHit = compound.getBoolean("DieOnEntityHit");
        if (compound.contains("DieOnBlockHit"))
            this.dieOnBlockHit = compound.getBoolean("DieOnBlockHit");
        if (compound.contains("Size", 99))
            this.dimensions = new EntityDimensions(compound.getFloat("Size"), compound.getFloat("Size"), false);

        if (compound.contains("Appearances")) {
            this.appearances = new ArrayList<>();
            ListTag listTag = compound.getList("Appearances", 10);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag aTag = listTag.getCompound(i);
                var type = APPEARANCE_REGISTRY.get(aTag.getString("Type"));

                if (type != null) {
                    this.appearances.add(type.apply(aTag));
                }
            }
        }
    }

    @Override
    public void saveAdditionalSpawnData(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        this.addAdditionalSaveData(tag);
        buf.writeNbt(tag);
        buf.writeFloat(this.getXRot());
        buf.writeFloat(this.getYRot());
    }

    @Override
    public void loadAdditionalSpawnData(FriendlyByteBuf buf) {
        this.readAdditionalSaveData(Objects.requireNonNull(buf.readNbt()));
        this.setXRot(buf.readFloat());
        this.setYRot(buf.readFloat());
    }

    public static abstract class Appearance {

        public Appearance(CompoundTag tag) {
        }

        public abstract String getId();

        public void onTick(CustomProjectile projectile) {

        }

        public void spawnParticlesOnHit(CustomProjectile projectile) {

        }

        public abstract void toNBT(CompoundTag nbt);

    }

    public static class ParticleAppearance extends Appearance {

        public final ParticleType type;
        public final float spread;
        public final String options;

        public ParticleAppearance(CompoundTag tag) {
            super(tag);
            this.type = Registry.PARTICLE_TYPE.get(new ResourceLocation(tag.getString("ParticleType")));
            this.spread = tag.getFloat("Spread");
            this.options = tag.getString("Options");
        }

        @Override
        public String getId() {
            return "particles";
        }

        @Override
        public void toNBT(CompoundTag nbt) {
            nbt.putString("ParticleType", Registry.PARTICLE_TYPE.getKey(this.type).toString());
            nbt.putFloat("Spread", this.spread);
            nbt.putString("Options", this.options);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onTick(CustomProjectile projectile) {
            Random random = new Random();
            float sX = (random.nextFloat() - 0.5F) * this.spread;
            float sY = (random.nextFloat() - 0.5F) * this.spread;
            float sZ = (random.nextFloat() - 0.5F) * this.spread;

            try {
                projectile.level.addParticle(this.type.getDeserializer().fromCommand(this.type, new StringReader(this.options)), projectile.getX(), projectile.getY(), projectile.getZ(), sX, sY, sZ);
            } catch (CommandSyntaxException e) {
            }
        }

        @Override
        public void spawnParticlesOnHit(CustomProjectile projectile) {
            Random random = new Random();
            float sX = (random.nextFloat() - 0.5F) * this.spread * 2F;
            float sY = (random.nextFloat() - 0.5F) * this.spread * 2F;
            float sZ = (random.nextFloat() - 0.5F) * this.spread * 2F;

            try {
                projectile.level.addParticle(this.type.getDeserializer().fromCommand(this.type, new StringReader(this.options)), projectile.getX(), projectile.getY(), projectile.getZ(), sX, sY, sZ);
            } catch (CommandSyntaxException e) {
            }
        }
    }

    public static class ItemAppearance extends Appearance {

        public final ItemStack item;

        public ItemAppearance(CompoundTag tag) {
            super(tag);
            var itemTag = tag.get("Item");

            if (itemTag instanceof CompoundTag compoundTag) {
                this.item = ItemStack.of(tag.getCompound("Item"));
            } else if (itemTag instanceof StringTag stringTag) {
                this.item = new ItemStack(Registry.ITEM.get(new ResourceLocation(stringTag.getAsString())));
            } else {
                this.item = ItemStack.EMPTY;
            }
        }

        @Override
        public String getId() {
            return "item";
        }

        @Override
        public void toNBT(CompoundTag nbt) {
            nbt.put("Item", this.item.save(new CompoundTag()));
        }

        @Override
        public void spawnParticlesOnHit(CustomProjectile projectile) {
            var data = new ItemParticleOption(ParticleTypes.ITEM, this.item);

            for (int i = 0; i < 8; ++i) {
                projectile.level.addParticle(data, projectile.getX(), projectile.getY(), projectile.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public static class LaserAppearance extends Appearance {

        public final float thickness;
        public final Color color;

        public LaserAppearance(CompoundTag tag) {
            super(tag);
            this.thickness = tag.getFloat("Thickness");
            var colorTag = tag.get("Color");

            if (colorTag instanceof StringTag stringTag) {
                this.color = Color.decode(stringTag.getAsString());
            } else if (colorTag instanceof CompoundTag compoundTag) {
                this.color = new Color(compoundTag.getInt("Red"), compoundTag.getInt("Green"), compoundTag.getInt("Blue"));
            } else {
                this.color = Color.RED;
            }
        }

        @Override
        public String getId() {
            return "laser";
        }

        @Override
        public void toNBT(CompoundTag nbt) {
            nbt.putFloat("Thickness", this.thickness);
            CompoundTag colorTag = new CompoundTag();
            colorTag.putInt("Red", this.color.getRed());
            colorTag.putInt("Green", this.color.getGreen());
            colorTag.putInt("Blue", this.color.getBlue());
            nbt.put("Color", colorTag);
        }
    }

}
