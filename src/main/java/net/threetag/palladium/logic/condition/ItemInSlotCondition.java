package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

public record ItemInSlotCondition(Ingredient ingredient, PlayerSlot slot) implements Condition {

    public static final MapCodec<ItemInSlotCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    PalladiumCodecs.INGREDIENT_CODEC.fieldOf("item").forGetter(ItemInSlotCondition::ingredient),
                    PlayerSlot.CODEC.fieldOf("slot").forGetter(ItemInSlotCondition::slot)
            ).apply(instance, ItemInSlotCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInSlotCondition> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ItemInSlotCondition::ingredient,
            PlayerSlot.STREAM_CODEC, ItemInSlotCondition::slot,
            ItemInSlotCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        for (ItemStack item : this.slot.getItems(entity)) {
            if (this.ingredient.test(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConditionSerializer<ItemInSlotCondition> getSerializer() {
        return ConditionSerializers.ITEM_IN_SLOT.get();
    }

    public static class Serializer extends ConditionSerializer<ItemInSlotCondition> {

        @Override
        public MapCodec<ItemInSlotCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, ItemInSlotCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Item in Slot")
                    .setDescription("Checks if the given item is in the given slot")
                    .add("item", TYPE_INGREDIENT, "Ingredient definition for the required item stack.")
                    .add("slot", TYPE_PLAYER_SLOT, "The slot that is being looked into.")
                    .addExampleObject(new ItemInSlotCondition(Ingredient.of(Items.STICK), PlayerSlot.get(EquipmentSlot.CHEST)));
        }
    }
}
