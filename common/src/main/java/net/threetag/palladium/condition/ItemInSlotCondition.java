package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.EquipmentSlotProperty;
import net.threetag.palladium.util.property.IngredientProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import org.jetbrains.annotations.Nullable;

public class ItemInSlotCondition extends Condition {

    private final Ingredient ingredient;
    private final EquipmentSlot slot;

    public ItemInSlotCondition(Ingredient ingredient, EquipmentSlot slot) {
        this.ingredient = ingredient;
        this.slot = slot;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return this.ingredient.test(entity.getItemBySlot(this.slot));
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ITEM_IN_SLOT.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Ingredient> ITEM = new IngredientProperty("item").configurable("Item (defined as an ingredient) that must be in the given slot");
        public static final PalladiumProperty<EquipmentSlot> SLOT = new EquipmentSlotProperty("slot").configurable("Slot that must contain the item");

        public Serializer() {
            this.withProperty(ITEM, Ingredient.of(Items.LEATHER_CHESTPLATE));
            this.withProperty(SLOT, EquipmentSlot.CHEST);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ItemInSlotCondition(this.getProperty(json, ITEM), this.getProperty(json, SLOT));
        }
    }
}
