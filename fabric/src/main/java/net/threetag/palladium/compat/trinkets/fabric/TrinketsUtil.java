package net.threetag.palladium.compat.trinkets.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;
import net.threetag.palladium.compat.curiostinkets.CurioTrinket;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsSlotInv;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;

public class TrinketsUtil extends CuriosTrinketsUtil {

    @Override
    public boolean isTrinkets() {
        return true;
    }

    @Override
    public void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        TrinketsApi.registerTrinket(item, new Trinket() {
            @Override
            public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
                curioTrinket.tick(entity, stack);
            }

            @Override
            public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
                curioTrinket.onEquip(stack, entity);
            }

            @Override
            public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
                curioTrinket.onUnequip(stack, entity);
            }

            @Override
            public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
                return curioTrinket.canEquip(stack, entity);
            }

            @Override
            public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
                return curioTrinket.canUnequip(stack, entity);
            }
        });
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        TrinketRendererRegistry.registerRenderer(item, new Renderer(renderer));
    }

    @Override
    public boolean equipItem(Player user, ItemStack stack) {
        var optional = TrinketsApi.getTrinketComponent(user);
        if (optional.isPresent()) {
            TrinketComponent comp = optional.get();
            for (var group : comp.getInventory().values()) {
                for (TrinketInventory inv : group.values()) {
                    for (int i = 0; i < inv.getContainerSize(); i++) {
                        if (inv.getItem(i).isEmpty()) {
                            SlotReference ref = new SlotReference(inv, i);
                            if (TrinketSlot.canInsert(stack, ref, user)) {
                                ItemStack newStack = stack.copy();
                                inv.setItem(i, newStack);
                                SoundEvent soundEvent = stack.getEquipSound();
                                if (!stack.isEmpty() && soundEvent != null) {
                                    user.gameEvent(GameEvent.EQUIP);
                                    user.playSound(soundEvent, 1.0F, 1.0F);
                                }
                                stack.setCount(0);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public CuriosTrinketsSlotInv getSlot(LivingEntity entity, String slotKey) {
        final CuriosTrinketsSlotInv[] container = {CuriosTrinketsSlotInv.EMPTY};
        var slot = parseSlot(slotKey);

        TrinketsApi.getTrinketComponent(entity).ifPresent(comp -> {
            var group = comp.getInventory().get(slot.getFirst());

            if (group != null) {
                var trinketSlot = group.get(slot.getSecond());

                if (trinketSlot != null) {
                    container[0] = new SlotInv(trinketSlot);
                }
            }
        });

        return container[0];
    }

    public Pair<String, String> parseSlot(String slot) {
        var split = slot.split("/");

        if (split.length != 2) {
            return Pair.of("", "");
        } else {
            return Pair.of(split[0], split[1]);
        }
    }

    public static class SlotInv implements CuriosTrinketsSlotInv {

        private final TrinketInventory inventory;

        public SlotInv(TrinketInventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public int getSlots() {
            return this.inventory.getContainerSize();
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return this.inventory.getItem(index);
        }

        @Override
        public void setStackInSlot(int index, ItemStack stack) {
            this.inventory.setItem(index, stack);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements TrinketRenderer {

        private final CurioTrinketRenderer renderer;

        public Renderer(CurioTrinketRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity, float v, float v1, float v2, float v3, float v4, float v5) {
            this.renderer.render(itemStack, poseStack, entityModel, livingEntity, multiBufferSource, i, v, v1, v2, v3, v4, v5);
        }
    }
}
