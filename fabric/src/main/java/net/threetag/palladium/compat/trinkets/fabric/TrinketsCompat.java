package net.threetag.palladium.compat.trinkets.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.TrinketsMain;
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
import net.threetag.palladium.item.CurioTrinket;
import net.threetag.palladium.item.CurioTrinketRenderer;
import net.threetag.palladium.power.provider.PowerProvider;

public class TrinketsCompat {

    public static final DeferredRegister<PowerProvider> FACTORIES = DeferredRegister.create(TrinketsMain.MOD_ID, PowerProvider.RESOURCE_KEY);
    public static final RegistrySupplier<PowerProvider> TRINKETS = FACTORIES.register("trinkets", TrinketsPowerProvider::new);

    public static void init() {
        FACTORIES.register();
    }

    public static void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
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

    @Environment(EnvType.CLIENT)
    public static void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        TrinketRendererRegistry.registerRenderer(item, new Renderer(renderer));
    }

    public static boolean equipItem(Player user, ItemStack stack) {
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
