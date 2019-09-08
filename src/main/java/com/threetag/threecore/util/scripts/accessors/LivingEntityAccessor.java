package com.threetag.threecore.util.scripts.accessors;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LivingEntityAccessor {

    public final LivingEntity entity;

    public LivingEntityAccessor(LivingEntity entity) {
        this.entity = entity;
    }

    public String getName() {
        return this.entity.getDisplayName().getFormattedText();
    }

    public void setName(String name) {
        this.entity.setCustomName(new StringTextComponent(name));
    }

    public void setNameVisible(boolean visible) {
        this.entity.setCustomNameVisible(visible);
    }

    public UUID getUUID() {
        return this.entity.getUniqueID();
    }

    public double getPosX() {
        return this.entity.posX;
    }

    public double getPosY() {
        return this.entity.posY;
    }

    public double getPosZ() {
        return this.entity.posZ;
    }

    public void setPosition(double x, double y, double z) {
        this.entity.setPositionAndUpdate(x, y, z);
    }

    public float getWidth() {
        return this.entity.getWidth();
    }

    public float getHeight() {
        return this.entity.getHeight();
    }

    public int getTicksExisted() {
        return this.entity.ticksExisted;
    }

    public void sendMessage(String message) {
        this.entity.sendMessage(new StringTextComponent(message));
    }

    public void sendTranslatedMessage(String message, Object... args) {
        this.entity.sendMessage(new TranslationTextComponent(message, args));
    }

    public boolean isSneaking() {
        return this.entity.isSneaking();
    }

    public AbilityAccessor[] getAbilities() {
        List<Ability> list = AbilityHelper.getAbilities(this.entity);
        AbilityAccessor[] array = new AbilityAccessor[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = new AbilityAccessor(list.get(i));
        }

        return array;
    }

    public AbilityAccessor[] getAbilities(String containerId) {
        IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(this.entity, new ResourceLocation(containerId));

        if (container != null)
            return new AbilityAccessor[0];

        Collection<AbilityAccessor> list = container.getAbilities().stream().map((a) -> new AbilityAccessor(a)).collect(Collectors.toList());
        AbilityAccessor[] array = new AbilityAccessor[list.size()];
        int i = 0;

        for (AbilityAccessor abilityAccessor : list) {
            array[i] = abilityAccessor;
            i++;
        }

        return array;
    }

}
