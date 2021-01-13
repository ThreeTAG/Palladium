package net.threetag.threecore.ability.container;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.IconSerializer;
import net.threetag.threecore.util.threedata.EnumSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultAbilityContainer implements IAbilityContainer, INBTSerializable<CompoundNBT> {

    protected final AbilityMap abilityMap;
    protected ResourceLocation id;
    protected ITextComponent title;
    protected IIcon icon;
    private int lifetime = 0;
    private int maxLifetime = -1;

    public DefaultAbilityContainer(ResourceLocation id, ITextComponent title, IIcon icon, int lifetime) {
        this.abilityMap = new AbilityMap();
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.maxLifetime = lifetime;
    }

    public DefaultAbilityContainer(CompoundNBT nbt, boolean network) {
        this.abilityMap = new AbilityMap();
        if (network) {
            this.readUpdateTag(nbt);
        } else {
            this.deserializeNBT(nbt);
        }
    }

    @Override
    public void tick(LivingEntity entity) {
        getAbilityMap().forEach((s, a) -> {
            a.container = this;
            a.tick(entity);
            if (a.sync != EnumSync.NONE) {
                onUpdated(entity, a, a.sync);
                a.sync = EnumSync.NONE;
            }
        });
        this.lifetime++;
    }

    @Override
    public AbilityMap getAbilityMap() {
        return this.abilityMap;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ITextComponent getTitle() {
        return this.title;
    }

    @Override
    public ITextComponent getSubtitle() {
        if (this.maxLifetime >= 0) {
            return new StringTextComponent(StringUtils.ticksToElapsedTime(this.lifetime));
        } else {
            return null;
        }
    }

    @Override
    public IIcon getIcon() {
        return this.icon;
    }

    @Override
    public CompoundNBT getNbtTag(@Nonnull LivingEntity entity) {
        return entity.getPersistentData();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull LivingEntity entity, @Nonnull Capability<T> cap, @Nullable Direction side) {
        return entity.getCapability(cap, side);
    }

    @Override
    public boolean isObsolete() {
        return this.maxLifetime >= 0 && this.lifetime >= this.maxLifetime;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Type", AbilityContainerTypes.getTypeId(this.getClass()).toString());
        nbt.putString("ID", this.id.toString());
        nbt.putString("Title", ITextComponent.Serializer.toJson(this.title));
        nbt.put("Icon", this.icon.getSerializer().serializeExt(this.icon));
        nbt.put("Abilities", AbilityHelper.saveToNBT(this.getAbilityMap()));
        nbt.putInt("Lifetime", this.lifetime);
        nbt.putInt("MaxLifetime", this.maxLifetime);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("ID"));
        this.title = ITextComponent.Serializer.getComponentFromJson(nbt.getString("Title"));
        this.icon = IconSerializer.deserialize(nbt.getCompound("Icon"));
        this.abilityMap.clear();
        AbilityHelper.loadFromNBT(nbt.getCompound("Abilities"), this.abilityMap);
        this.lifetime = nbt.getInt("Lifetime");
        this.maxLifetime = nbt.getInt("MaxLifetime");
    }

    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("ID", this.id.toString());
        nbt.putString("Title", ITextComponent.Serializer.toJson(this.title));
        nbt.put("Icon", this.icon.getSerializer().serializeExt(this.icon));
        nbt.put("Abilities", AbilityHelper.saveToNBT(this.getAbilityMap(), true));
        nbt.putInt("Lifetime", this.lifetime);
        nbt.putInt("MaxLifetime", this.maxLifetime);
        return nbt;
    }

    public void readUpdateTag(CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("ID"));
        this.title = ITextComponent.Serializer.getComponentFromJson(nbt.getString("Title"));
        this.icon = IconSerializer.deserialize(nbt.getCompound("Icon"));
        this.abilityMap.clear();
        AbilityHelper.loadFromNBT(nbt.getCompound("Abilities"), this.abilityMap, true);
        this.lifetime = nbt.getInt("Lifetime");
        this.maxLifetime = nbt.getInt("MaxLifetime");
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }
}
