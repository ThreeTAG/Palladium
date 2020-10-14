package net.threetag.threecore.compat.curios;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.IAbilityContainer;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class CuriosHandler extends DefaultCuriosHandler {

    public static void enable() {
        DefaultCuriosHandler.INSTANCE = new CuriosHandler();

        AbilityHelper.registerAbilityContainer((entity) -> {
            List<IAbilityContainer> containers = Lists.newArrayList();
            CuriosApi.getSlotHelper().getSlotTypeIds().forEach(id -> {
                for (ItemStack stack : INSTANCE.getItemsInSlot(entity, id)) {
                    stack.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(containers::add);
                }
            });
            return containers;
        });

        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @Override
    public Set<String> getSlotTypeIds() {
        return CuriosApi.getSlotHelper().getSlotTypeIds();
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

    @SubscribeEvent
    public void onCurioChange(CurioChangeEvent e) {
        e.getFrom().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(a -> a.getAbilityMap().forEach((s, ability) -> ability.lastTick(e.getEntityLiving())));
    }
}
