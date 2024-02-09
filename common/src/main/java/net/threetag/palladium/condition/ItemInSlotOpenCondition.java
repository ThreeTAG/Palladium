package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.item.Openable;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PlayerSlotProperty;

public class ItemInSlotOpenCondition extends Condition {

    private final PlayerSlot slot;

    public ItemInSlotOpenCondition(PlayerSlot slot) {
        this.slot = slot;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        var stacks = this.slot.getItems(entity);

        for (ItemStack stack : stacks) {
            if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
                if (openable.isOpen(stack)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ITEM_IN_SLOT_OPEN.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<PlayerSlot> SLOT = new PlayerSlotProperty("slot").configurable("Slot that must contain an opened item");

        public Serializer() {
            this.withProperty(SLOT, PlayerSlot.get(EquipmentSlot.CHEST.getName()));
        }

        @Override
        public Condition make(JsonObject json) {
            return new ItemInSlotOpenCondition(this.getProperty(json, SLOT));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the item in the given slot is opened. Needs to be using the openable-system for items.";
        }
    }
}
