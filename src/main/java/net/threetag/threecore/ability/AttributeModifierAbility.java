package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityPropertiesPacket;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.util.threedata.*;

import java.util.Collection;
import java.util.UUID;

public class AttributeModifierAbility extends Ability {

    public static final ThreeData<Attribute> ATTRIBUTE = new AttributeThreeData("attribute").setSyncType(EnumSync.SELF).enableSetting("attribute", "Determines which attribute should be modified. Possible attribute: " + getAttributeList());
    public static final ThreeData<Float> AMOUNT = new FloatThreeData("amount").setSyncType(EnumSync.SELF).enableSetting("amount", "The amount for the giving attribute modifier");
    public static final ThreeData<AttributeModifier.Operation> OPERATION = new AttributeOperationThreeData("operation").setSyncType(EnumSync.SELF).enableSetting("operation", "The operation for the giving attribute modifier (More: https://minecraft.gamepedia.com/Attribute#Operations)");
    public static final ThreeData<UUID> UUID = new UUIDThreeData("uuid").setSyncType(EnumSync.SELF).enableSetting("uuid", "Sets the unique identifier for this attribute modifier. If not specified it will generate a random one");

    public AttributeModifierAbility() {
        super(AbilityType.ATTRIBUTE_MODIFIER);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ATTRIBUTE, Attributes.field_233818_a_);
        this.dataManager.register(AMOUNT, 1F);
        this.dataManager.register(OPERATION, AttributeModifier.Operation.ADDITION);
        this.dataManager.register(UUID, java.util.UUID.fromString("498be4fb-af04-42f2-8948-e6ccdc0d99e1"));
    }

    @Override
    public void action(LivingEntity entity) {
        Attribute attribute = this.dataManager.get(ATTRIBUTE);

        if (entity.getAttribute(attribute) == null || entity.world.isRemote) {
            return;
        }

        UUID uuid = this.dataManager.get(UUID);

        if (entity.func_233645_dx_().func_233778_a_().stream().noneMatch(iAttributeInstance -> iAttributeInstance.getAttribute() == attribute)) {
            entity.func_233645_dx_().func_233779_a_(attribute).setBaseValue(attribute.getDefaultValue());
        }

        if (entity.func_233645_dx_().func_233779_a_(attribute).getModifier(uuid) != null && (
                entity.getAttribute(attribute).getModifier(uuid).getAmount() != this.dataManager.get(AMOUNT)
                        || entity.getAttribute(attribute).getModifier(uuid).getOperation() != this.dataManager.get(OPERATION))) {
            entity.getAttribute(attribute).removeModifier(uuid);
        }

        // TODO TEST THIS
        if (entity.getAttribute(attribute).getModifier(uuid) == null) {
            AttributeModifier modifier = new AttributeModifier(uuid, this.dataManager.get(TITLE).getString(), this.dataManager.get(AMOUNT), this.dataManager.get(OPERATION));
            entity.func_233645_dx_().func_233779_a_(attribute).func_233767_b_(modifier);
        }

        if (entity.ticksExisted < 20 && entity instanceof ServerPlayerEntity) {
            Collection<ModifiableAttributeInstance> set = entity.func_233645_dx_().func_233778_a_();
            if (!set.isEmpty()) {
                ((ServerPlayerEntity) entity).connection.sendPacket(new SEntityPropertiesPacket(entity.getEntityId(), set));
            }
        }
    }

    @Override
    public void lastTick(LivingEntity entity) {
        if (entity.getAttribute(this.dataManager.get(ATTRIBUTE)) != null && entity.getAttribute(this.dataManager.get(ATTRIBUTE)).getModifier(this.dataManager.get(UUID)) != null) {
            entity.getAttribute(this.dataManager.get(ATTRIBUTE)).removeModifier(this.dataManager.get(UUID));
        }
    }

    private static String getAttributeList() {
        StringBuilder stringBuilder = new StringBuilder();
        ForgeRegistries.ATTRIBUTES.forEach(attribute -> {
            stringBuilder.append(", \"").append(ForgeRegistries.ATTRIBUTES.getKey(attribute).toString()).append("\"");
        });

        return stringBuilder.toString().substring(2);
    }

}
