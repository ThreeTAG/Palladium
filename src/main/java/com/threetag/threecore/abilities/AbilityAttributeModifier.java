package com.threetag.threecore.abilities;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.threetag.threecore.abilities.data.*;
import com.threetag.threecore.util.attributes.AttributeRegistry;
import com.threetag.threecore.util.render.IIcon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AbilityAttributeModifier extends AbilityConstant {

    public static final Map<String, IAttribute> ATTRIBUTES = Maps.newHashMap();
    public static final ThreeData<IAttribute> ATTRIBUTE = new ThreeDataAttribute("attribute").setSyncType(EnumSync.SELF).enableSetting("attribute", "Determines which attribute should be modified. Possible attribute: " + getAttributeList());
    public static final ThreeData<Float> AMOUNT = new ThreeDataFloat("amount").setSyncType(EnumSync.SELF).enableSetting("amount", "The amount for the giving attribute modifier");
    public static final ThreeData<Integer> OPERATION = new ThreeDataInteger("operation").setSyncType(EnumSync.SELF).enableSetting("operation", "The operation for the giving attribute modifier (More: https://minecraft.gamepedia.com/Attribute#Operations)");
    public static final ThreeData<UUID> UUID = new ThreeDataUUID("uuid").setSyncType(EnumSync.SELF).enableSetting("uuid", "Sets the unique identifier for this attribute modifier. If not specified it will generate a random one");

    public AbilityAttributeModifier() {
        super(AbilityType.ATTRIBUTE_MODIFIER);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ATTRIBUTE, AttributeRegistry.REGISTRY.getRandom(new Random()).getAttribute());
        this.dataManager.register(AMOUNT, 1F);
        this.dataManager.register(OPERATION, 0);
        this.dataManager.register(UUID, java.util.UUID.randomUUID());
    }

    @Override
    public void readFromJson(JsonObject jsonObject) {
        super.readFromJson(jsonObject);
        IIcon icon = AttributeRegistry.getEntry(this.dataManager.get(ATTRIBUTE)).makeIcon();
        if (icon != null)
            this.dataManager.register(ICON, icon);
    }

    @Override
    public void updateTick(EntityLivingBase entity) {
        IAttribute attribute = this.dataManager.get(ATTRIBUTE);
        UUID uuid = this.dataManager.get(UUID);

        if (entity.getAttributeMap().getAllAttributes().stream().noneMatch(iAttributeInstance -> iAttributeInstance.getAttribute() == attribute)) {
            entity.getAttributeMap().registerAttribute(attribute).setBaseValue(attribute.getDefaultValue());
        }

        if (entity.getAttributeMap().getAttributeInstance(attribute).getModifier(uuid) != null && (
                entity.getAttribute(attribute).getModifier(uuid).getAmount() != this.dataManager.get(AMOUNT)
                        || entity.getAttribute(attribute).getModifier(uuid).getOperation() != this.dataManager.get(OPERATION))) {
            entity.getAttributeMap().getAttributeInstance(attribute).removeModifier(uuid);
        }

        if (entity.getAttributeMap().getAttributeInstance(attribute).getModifier(uuid) == null) {
            AttributeModifier modifier = new AttributeModifier(uuid, this.dataManager.get(TITLE).getFormattedText(), this.dataManager.get(AMOUNT), this.dataManager.get(OPERATION)).setSaved(false);
            entity.getAttributeMap().getAttributeInstance(attribute).applyModifier(modifier);
        }
    }

    @Override
    public void lastTick(EntityLivingBase entity) {
        if (entity.getAttributeMap().getAttributeInstance(this.dataManager.get(ATTRIBUTE)).getModifier(this.dataManager.get(UUID)) != null) {
            entity.getAttributeMap().getAttributeInstance(this.dataManager.get(ATTRIBUTE)).removeModifier(this.dataManager.get(UUID));
        }
    }

    private static String getAttributeList() {
        String s = "";
        Iterator<AttributeRegistry.AttributeEntry> iterator = AttributeRegistry.REGISTRY.iterator();

        while (iterator.hasNext()) {
            s += ", \"" + AttributeRegistry.REGISTRY.getKey(iterator.next()).toString() + "\"";
        }

        return s.substring(2);
    }

}
