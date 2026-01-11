package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerCollector;
import net.threetag.palladium.power.PowerValidator;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;

public class ItemPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        var registry = entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER);
        for (PlayerSlot slot : PlayerSlot.values(entity.level())) {
            for (int i = 0; i < slot.getSize(entity); i++) {
                var stack = slot.getItem(entity, i);

                if (stack.has(PalladiumDataComponents.Items.POWERS.get())) {
                    var itemPowers = stack.get(PalladiumDataComponents.Items.POWERS.get());

                    for (Identifier powerId : Objects.requireNonNull(itemPowers).forSlot(slot)) {
                        registry.get(powerId).ifPresent(power -> collector.addPower(power, PowerProviders.PRIORITY_ITEMS, () -> new Validator(slot, powerId)));
                    }
                }
            }
        }
    }

    public record Validator(PlayerSlot slot, Identifier id) implements PowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Holder<Power> power) {
            for (ItemStack item : this.slot.getItems(entity)) {
                var itemPowers = item.get(PalladiumDataComponents.Items.POWERS.get());

                if (itemPowers != null && itemPowers.forSlot(this.slot).contains(this.id)) {
                    return true;
                }
            }

            return false;
        }
    }

}
