package net.threetag.palladium.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public interface IAddonItem {

    void setTooltip(List<Component> lines);

    void addAttributeModifier(@Nullable EquipmentSlot slot, Attribute attribute, AttributeModifier modifier);

}
