package net.threetag.threecore.compat.curios;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CuriosHandler extends DefaultCuriosHandler {

    public static void enable() {
        DefaultCuriosHandler.INSTANCE = new CuriosHandler();

//        CuriosApi.getSlotHelper().getSlotTypeIds().forEach(id -> {
//            AbilityHelper.registerAbilityContainer(new ResourceLocation("curios", id), (p) -> {
//                List<ItemStack> list = INSTANCE.getItemsInSlot(p, id);
//                return list.size() > 0 ? list.get(0).getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null) : null;
//            });
//        });
    }

    @Override
    public Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioEquipped(Item item, @Nonnull LivingEntity livingEntity) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(item, livingEntity);
    }

    @Override
    public Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioEquipped(Predicate<ItemStack> filter, @Nonnull LivingEntity livingEntity) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(filter, livingEntity);
    }

    @Override
    public List<ItemStack> getItemsInSlot(LivingEntity entity, String identifier) {
        List<ItemStack> list = Lists.newArrayList();
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(curioHandler -> {
            curioHandler.getStacksHandler(identifier).ifPresent(slotHandler -> {
                for (int i = 0; i < slotHandler.getStacks().getSlots(); i++) {
                    list.add(slotHandler.getStacks().getStackInSlot(i));
                }
            });
        });
        return list;
    }
}
