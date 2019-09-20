package net.threetag.threecore.abilities.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.abilities.AbilityMap;
import net.threetag.threecore.abilities.IAbilityContainer;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.TexturedIcon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityAbilityContainer implements IAbilityContainer, INBTSerializable<CompoundNBT> {

    @CapabilityInject(IAbilityContainer.class)
    public static Capability<IAbilityContainer> ABILITY_CONTAINER;
    public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "entity");
    public static final IIcon STEVE_HEAD_ICON = new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 112, 16, 16, 16);

    protected AbilityMap abilityMap;

    public CapabilityAbilityContainer() {
        this.abilityMap = new AbilityMap();
    }

    @Override
    public AbilityMap getAbilityMap() {
        return this.abilityMap;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return AbilityHelper.saveToNBT(this.getAbilityMap());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.abilityMap.clear();
        AbilityHelper.loadFromNBT(nbt, this.abilityMap);
    }

    public CompoundNBT getUpdateTag() {
        return AbilityHelper.saveToNBT(this.getAbilityMap(), true);
    }

    public void readUpdateTag(CompoundNBT nbt) {
        this.abilityMap.clear();
        AbilityHelper.loadFromNBT(nbt, this.abilityMap, true);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ITextComponent getTitle() {
        return new TranslationTextComponent("ability_container.threecore.player");
    }

    @Override
    public IIcon getIcon() {
        return STEVE_HEAD_ICON;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull LivingEntity entity, @Nonnull Capability<T> cap, @Nullable Direction side) {
        return entity.getCapability(cap, side);
    }
}
