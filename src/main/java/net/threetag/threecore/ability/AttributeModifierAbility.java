package net.threetag.threecore.ability;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityPropertiesPacket;
import net.threetag.threecore.entity.attributes.AttributeRegistry;
import net.threetag.threecore.util.icon.IIcon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.threetag.threecore.util.threedata.*;

import java.util.*;

public class AttributeModifierAbility extends Ability {

    public static final Map<String, IAttribute> ATTRIBUTES = Maps.newHashMap();
    public static final ThreeData<IAttribute> ATTRIBUTE = new AttributeThreeData("attribute").setSyncType(EnumSync.SELF).enableSetting("attribute", "Determines which attribute should be modified. Possible attribute: " + getAttributeList());
    public static final ThreeData<Float> AMOUNT = new FloatThreeData("amount").setSyncType(EnumSync.SELF).enableSetting("amount", "The amount for the giving attribute modifier");
    public static final ThreeData<AttributeModifier.Operation> OPERATION = new AttributeOperationThreeData("operation").setSyncType(EnumSync.SELF).enableSetting("operation", "The operation for the giving attribute modifier (More: https://minecraft.gamepedia.com/Attribute#Operations)");
    public static final ThreeData<UUID> UUID = new UUIDThreeData("uuid").setSyncType(EnumSync.SELF).enableSetting("uuid", "Sets the unique identifier for this attribute modifier. If not specified it will generate a random one");

    public AttributeModifierAbility() {
        super(AbilityType.ATTRIBUTE_MODIFIER);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ATTRIBUTE, AttributeRegistry.REGISTRY.getRandom(new Random()).getAttribute());
        this.dataManager.register(AMOUNT, 1F);
        this.dataManager.register(OPERATION, AttributeModifier.Operation.ADDITION);
        this.dataManager.register(UUID, java.util.UUID.fromString("498be4fb-af04-42f2-8948-e6ccdc0d99e1"));
    }

    @Override
    public void readFromJson(JsonObject jsonObject) {
        super.readFromJson(jsonObject);
        IIcon icon = AttributeRegistry.getEntry(this.dataManager.get(ATTRIBUTE)).makeIcon();
        if (icon != null)
            this.dataManager.register(ICON, icon);
    }

    @Override
    public void action(LivingEntity entity) {
        IAttribute attribute = this.dataManager.get(ATTRIBUTE);

        if (entity.getAttributes().getAttributeInstance(attribute) == null || entity.world.isRemote) {
            return;
        }

        UUID uuid = this.dataManager.get(UUID);

        if (entity.getAttributes().getAllAttributes().stream().noneMatch(iAttributeInstance -> iAttributeInstance.getAttribute() == attribute)) {
            entity.getAttributes().registerAttribute(attribute).setBaseValue(attribute.getDefaultValue());
        }

        if (entity.getAttributes().getAttributeInstance(attribute).getModifier(uuid) != null && (
                entity.getAttribute(attribute).getModifier(uuid).getAmount() != this.dataManager.get(AMOUNT)
                        || entity.getAttribute(attribute).getModifier(uuid).getOperation() != this.dataManager.get(OPERATION))) {
            entity.getAttributes().getAttributeInstance(attribute).removeModifier(uuid);
        }

        if (entity.getAttributes().getAttributeInstance(attribute).getModifier(uuid) == null) {
            AttributeModifier modifier = new AttributeModifier(uuid, this.dataManager.get(TITLE).getFormattedText(), this.dataManager.get(AMOUNT), this.dataManager.get(OPERATION)).setSaved(false);
            entity.getAttributes().getAttributeInstance(attribute).applyModifier(modifier);
        }

        if (entity.ticksExisted < 20 && entity instanceof ServerPlayerEntity) {
            Collection<IAttributeInstance> set = entity.getAttributes().getAllAttributes();
            if (!set.isEmpty()) {
                ((ServerPlayerEntity) entity).connection.sendPacket(new SEntityPropertiesPacket(entity.getEntityId(), set));
            }
        }
    }

    @Override
    public void lastTick(LivingEntity entity) {
        if (entity.getAttributes().getAttributeInstance(this.dataManager.get(ATTRIBUTE)) != null && entity.getAttributes().getAttributeInstance(this.dataManager.get(ATTRIBUTE)).getModifier(this.dataManager.get(UUID)) != null) {
            entity.getAttributes().getAttributeInstance(this.dataManager.get(ATTRIBUTE)).removeModifier(this.dataManager.get(UUID));
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
