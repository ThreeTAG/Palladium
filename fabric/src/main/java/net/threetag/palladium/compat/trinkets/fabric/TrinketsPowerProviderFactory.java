package net.threetag.palladium.compat.trinkets.fabric;

import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.TrinketsMain;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ItemPowerManager;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.holderfactory.PowerProviderFactory;
import net.threetag.palladium.power.provider.IPowerProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TrinketsPowerProviderFactory extends PowerProviderFactory {

    // TODO untested

    @Override
    public void create(Consumer<IPowerProvider> consumer) {
        for (Map.Entry<String, SlotGroup> entry : TrinketsApi.getPlayerSlots().entrySet()) {
            ResourceLocation key = new ResourceLocation(TrinketsMain.MOD_ID, entry.getValue().getName() + "/" + entry.getKey());
            consumer.accept(new PowerProvider(key, Pair.of(entry.getValue().getName(), entry.getKey())));
        }
    }

    public record PowerProvider(ResourceLocation key, Pair<String, String> slot) implements IPowerProvider {

        @Override
        public ResourceLocation getKey() {
            return this.key;
        }

        @Override
        public IPowerHolder createPower(LivingEntity entity, @Nullable Power power) {
            AtomicReference<ItemStack> stack = new AtomicReference<>(ItemStack.EMPTY);
            TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
                if (trinketComponent.getInventory().containsKey(this.slot.getFirst())) {
                    stack.set(trinketComponent.getInventory().get(this.slot.getFirst()).get(this.slot.getSecond()).getItem(0));
                }
            });
            power = power == null ? ItemPowerManager.getInstance().getPowerForItem("trinkets:" + this.slot.getFirst() + "/" + this.slot.getSecond(), stack.get().getItem()) : power;
            return power != null ? new PowerHolder(entity, power, this.getKey(), stack.get(), this.slot) : null;
        }
    }

    public static class PowerHolder extends DefaultPowerHolder {

        public final ItemStack stack;
        public final Pair<String, String> slot;

        public PowerHolder(LivingEntity entity, Power power, ResourceLocation provider, ItemStack stack, Pair<String, String> slot) {
            super(entity, power, provider, null);
            this.stack = stack;
            this.slot = slot;
        }

        @Override
        public boolean isInvalid() {
            AtomicReference<ItemStack> stack = new AtomicReference<>(ItemStack.EMPTY);
            TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
                if (trinketComponent.getInventory().containsKey(this.slot.getFirst())) {
                    stack.set(trinketComponent.getInventory().get(this.slot.getFirst()).get(this.slot.getSecond()).getItem(0));
                }
            });
            return this.getPower().isInvalid() || this.stack != stack.get();
        }
    }

}
