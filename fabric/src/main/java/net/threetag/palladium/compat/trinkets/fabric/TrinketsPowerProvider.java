package net.threetag.palladium.compat.trinkets.fabric;

import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.*;
import net.threetag.palladium.power.provider.PowerProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TrinketsPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        for (Map.Entry<String, SlotGroup> entry : TrinketsApi.getPlayerSlots().entrySet()) {
            TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
                if (trinketComponent.getInventory().containsKey(entry.getKey())) {
                    trinketComponent.getInventory().get(entry.getKey()).forEach((key, trinketInventory) -> {
                        for (int i = 0; i < trinketInventory.getContainerSize(); i++) {
                            ItemStack stack = trinketInventory.getItem(i);

                            if (!stack.isEmpty()) {
                                List<Power> powers = ItemPowerManager.getInstance().getPowerForItem("trinkets:" + entry.getKey() + "/" + key, stack.getItem());

                                if (powers != null) {
                                    for (Power power : powers) {
                                        collector.addPower(power, () -> new Validator(stack, Pair.of(entry.getKey(), key)));
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
    }


    public record Validator(Item item, Pair<String, String> slot) implements IPowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            AtomicReference<ItemStack> stack = new AtomicReference<>(ItemStack.EMPTY);
            TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
                if (trinketComponent.getInventory().containsKey(this.slot.getFirst())) {
                    stack.set(trinketComponent.getInventory().get(this.slot.getFirst()).get(this.slot.getSecond()).getItem(0));
                }
            });
            return stack.get().is(this.item);
        }
    }

}
