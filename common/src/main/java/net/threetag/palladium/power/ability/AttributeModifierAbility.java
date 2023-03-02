package net.threetag.palladium.power.ability;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

import java.util.Objects;
import java.util.UUID;

public class AttributeModifierAbility extends Ability {

    public static final PalladiumProperty<Attribute> ATTRIBUTE = new AttributeProperty("attribute").configurable("Determines which attribute should be modified. Possible attribute:" + getAttributeList());
    public static final PalladiumProperty<Double> AMOUNT = new DoubleProperty("amount").configurable("The amount for the giving attribute modifier");
    public static final PalladiumProperty<Integer> OPERATION = new IntegerProperty("operation").configurable("The operation for the giving attribute modifier (More: https://minecraft.gamepedia.com/Attribute#Operations)");
    public static final PalladiumProperty<UUID> UUID = new UUIDProperty("uuid").configurable("Sets the unique identifier for this attribute modifier. If not specified it will generate a random one");

    public AttributeModifierAbility() {
        this.withProperty(ICON, new ItemIcon(Items.IRON_CHESTPLATE));
        this.withProperty(ATTRIBUTE, Attributes.ARMOR);
        this.withProperty(AMOUNT, 1D);
        this.withProperty(OPERATION, 0);
        this.withProperty(UUID, java.util.UUID.fromString("498be4fb-af04-42f2-8948-e6ccdc0d99e1"));
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            Attribute attribute = entry.getProperty(ATTRIBUTE);
            AttributeInstance instance = entity.getAttribute(attribute);

            if (instance == null || entity.level.isClientSide) {
                return;
            }

            UUID uuid = entry.getProperty(UUID);
            AttributeModifier modifier = instance.getModifier(uuid);

            // Remove modifier if amount or operation dont match
            if (modifier != null && (modifier.getAmount() != entry.getProperty(AMOUNT) || modifier.getOperation().toValue() != entry.getProperty(OPERATION))) {
                instance.removeModifier(uuid);
                modifier = null;
            }

            if (modifier == null) {
                modifier = new AttributeModifier(uuid, entry.getConfiguration().getDisplayName().getString(), entry.getProperty(AMOUNT), AttributeModifier.Operation.fromValue(entry.getProperty(OPERATION)));
                instance.addTransientModifier(modifier);
            }
        } else {
            this.lastTick(entity, entry, holder, false);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (entity.getAttribute(entry.getProperty(ATTRIBUTE)) != null && entity.getAttribute(entry.getProperty(ATTRIBUTE)).getModifier(entry.getProperty(UUID)) != null) {
            entity.getAttribute(entry.getProperty(ATTRIBUTE)).removeModifier(entry.getProperty(UUID));
        }
    }

    private static String getAttributeList() {
        StringBuilder stringBuilder = new StringBuilder();
        Registry.ATTRIBUTE.forEach(attribute -> {
            stringBuilder.append(", \"").append(Objects.requireNonNull(Registry.ATTRIBUTE.getKey(attribute))).append("\"");
        });

        return stringBuilder.substring(2);
    }

    @Override
    public String getDocumentationDescription() {
        return "Modifies an attribute.";
    }
}
