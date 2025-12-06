package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.item.Openable;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PlayerSlotProperty;

public class OpenableItemProgressCondition extends Condition {

    private final PlayerSlot slot;
    private final int min;
    private final int max;

    public OpenableItemProgressCondition(PlayerSlot slot, int min, int max) {
        this.slot = slot;
        this.min = min;
        this.max = max;
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
                    int openProgress = openable.getOpeningProgress(stack);

                    if (openProgress >= this.min && openProgress <= this.max) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.OPENABLE_ITEM_PROGRESS.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<PlayerSlot> SLOT = new PlayerSlotProperty("slot").configurable("Slot that must contain an opened item");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the progress value");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the progress value");

        public Serializer() {
            this.withProperty(SLOT, PlayerSlot.get(EquipmentSlot.CHEST.getName()));
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new OpenableItemProgressCondition(this.getProperty(json, SLOT), this.getProperty(json, MIN), this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the openable item in the given slot has a certain amount of progress. Needs to be using the openable-system for items.";
        }
    }
}
