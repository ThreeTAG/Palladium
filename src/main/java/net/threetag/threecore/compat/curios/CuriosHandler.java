package net.threetag.threecore.compat.curios;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosAPI;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CuriosHandler extends DefaultCuriosHandler {

    public static void enable() {
        DefaultCuriosHandler.INSTANCE = new CuriosHandler();

        CuriosAPI.getTypeIdentifiers().forEach(id -> {
            AbilityHelper.registerAbilityContainer(new ResourceLocation("curios", id), (p) -> {
                List<ItemStack> list = INSTANCE.getItemsInSlot(p, id);
                return list.size() > 0 ? list.get(0).getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null) : null;
            });
        });
    }

    @Override
    public Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioEquipped(Item item, @Nonnull LivingEntity livingEntity) {
        return CuriosAPI.getCurioEquipped(item, livingEntity);
    }

    @Override
    public Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioEquipped(Predicate<ItemStack> filter, @Nonnull LivingEntity livingEntity) {
        return CuriosAPI.getCurioEquipped(filter, livingEntity);
    }

    @Override
    public List<ItemStack> getItemsInSlot(LivingEntity entity, String identifier) {
        List<ItemStack> list = Lists.newArrayList();
        CuriosAPI.getCuriosHandler(entity).ifPresent(curioHandler -> {
            for (int i = 0; i < curioHandler.getStackHandler(identifier).getSlots(); i++) {
                list.add(curioHandler.getStackInSlot(identifier, i));
            }
        });
        return list;
    }
}
