package net.threetag.palladium.compat.curios.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.ItemPowerManager;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.provider.PowerProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CuriosPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler) {
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(curios -> {
            for (ISlotType slotType : CuriosApi.getSlotHelper().getSlotTypes(entity)) {
                curios.getStacksHandler(slotType.getIdentifier()).ifPresent(stacks -> {
                    for (int i = 0; i < stacks.getStacks().getSlots(); i++) {
                        ItemStack stack = stacks.getStacks().getStackInSlot(i);

                        if (!stack.isEmpty()) {
                            List<Power> powers = ItemPowerManager.getInstance().getPowerForItem("curios:" + slotType.getIdentifier(), stack.getItem());

                            if (powers != null) {
                                for (Power power : powers) {
                                    if (power != null && !handler.hasPower(power)) {
                                        handler.setPowerHolder(power, new PowerHolder(entity, power, stack, slotType.getIdentifier()));
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public static class PowerHolder extends DefaultPowerHolder {

        public final ItemStack stack;
        public final String slot;

        public PowerHolder(LivingEntity entity, Power power, ItemStack stack, String slot) {
            super(entity, power, null);
            this.stack = stack;
            this.slot = slot;
        }

        @Override
        public boolean isInvalid() {
            AtomicBoolean available = new AtomicBoolean(false);
            CuriosApi.getCuriosHelper().getCuriosHandler(this.entity).ifPresent(curios -> {
                curios.getStacksHandler(this.slot).ifPresent(stacks -> {
                    for (int i = 0; i < stacks.getStacks().getSlots(); i++) {
                        ItemStack stack = stacks.getStacks().getStackInSlot(i);

                        if (stack == this.stack) {
                            available.set(true);
                        }
                    }
                });
            });

            return this.getPower().isInvalid() || !available.get();
        }
    }
}
