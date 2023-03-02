package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.property.IngredientProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PlayerSlotProperty;
import org.jetbrains.annotations.Nullable;

public class ItemInSlotCondition extends Condition {

    private final Ingredient ingredient;
    private final PlayerSlot slot;

    public ItemInSlotCondition(Ingredient ingredient, PlayerSlot slot) {
        this.ingredient = ingredient;
        this.slot = slot;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        for (ItemStack item : this.slot.getItems(entity)) {
            if (this.ingredient.test(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ITEM_IN_SLOT.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Ingredient> ITEM = new IngredientProperty("item").configurable("Item (defined as an ingredient) that must be in the given slot");
        public static final PalladiumProperty<PlayerSlot> SLOT = new PlayerSlotProperty("slot").configurable("Slot that must contain the item");

        public Serializer() {
            this.withProperty(ITEM, Ingredient.of(Items.LEATHER_CHESTPLATE));
            this.withProperty(SLOT, PlayerSlot.get(EquipmentSlot.CHEST.getName()));
        }

        @Override
        public Condition make(JsonObject json) {
            return new ItemInSlotCondition(this.getProperty(json, ITEM), this.getProperty(json, SLOT));
        }

        @Override
        public String getDescription() {
            return "Checks if the given item is in the given slot.";
        }
    }
}
