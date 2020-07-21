package net.threetag.threecore.compat.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DefaultCuriosHandler {

    public static DefaultCuriosHandler INSTANCE = new DefaultCuriosHandler();

    public Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioEquipped(Item item, @Nonnull LivingEntity livingEntity) {
        return Optional.empty();
    }

    public Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioEquipped(Predicate<ItemStack> filter, @Nonnull LivingEntity livingEntity) {
        return Optional.empty();
    }

    public List<ItemStack> getItemsInSlot(LivingEntity entity, String identifier) {
        return Collections.emptyList();
    }

}
