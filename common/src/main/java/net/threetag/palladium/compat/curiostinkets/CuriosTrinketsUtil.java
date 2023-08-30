package net.threetag.palladium.compat.curiostinkets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;
import net.threetag.palladiumcore.util.Platform;

import java.util.ArrayList;
import java.util.List;

public class CuriosTrinketsUtil {

    private static CuriosTrinketsUtil INSTANCE = new CuriosTrinketsUtil();
    public static final Slot HAT = new Slot("head", "head/hat");
    public static final Slot NECKLACE = new Slot("necklace", "chest/necklace");
    public static final Slot BACK = new Slot("back", "chest/back");
    public static final Slot CAPE = new Slot("back", "chest/cape");
    public static final Slot BELT = new Slot("belt", "legs/belt");
    public static final Slot HAND = new Slot("hand", "hand/glove");
    public static final Slot OFFHAND = new Slot("hand", "offhand/glove");
    public static final Slot RING = new Slot("hand", "hand/ring");
    public static final Slot OFFHAND_RING = new Slot("ring", "offhand/ring");

    public static void setInstance(CuriosTrinketsUtil instance) {
        INSTANCE = instance;
    }

    public static CuriosTrinketsUtil getInstance() {
        return INSTANCE;
    }

    public boolean isTrinkets() {
        return false;
    }

    public boolean isCurios() {
        return false;
    }

    public List<ItemStack> getItemsInSlot(LivingEntity entity, String slot) {
        var inv = this.getSlot(entity, slot);
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < inv.getSlots(); i++) {
            var stack = inv.getStackInSlot(i);

            if(!stack.isEmpty()) {
                items.add(stack);
            }
        }

        return items;
    }

    public CuriosTrinketsSlotInv getSlot(LivingEntity entity, String slot) {
        return CuriosTrinketsSlotInv.EMPTY;
    }

    public CuriosTrinketsSlotInv getSlot(LivingEntity entity, Slot slot) {
        return this.getSlot(entity, (Platform.isForge() ? slot.getForge() : slot.getFabric()).location().getPath());
    }

    public boolean equipItem(Player user, ItemStack stack) {
        return false;
    }

    public void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        // nothing
    }

    public void registerCurioTrinket(Item item) {
        if (item instanceof CurioTrinket curioTrinket) {
            registerCurioTrinket(item, curioTrinket);
        } else {
            throw new RuntimeException("Tried to register item that isn't implementing CurioTrinket");
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        // nothing
    }

    public static class Slot {

        private final TagKey<Item> forge, fabric;

        public Slot(String forge, String fabric) {
            this.forge = TagKey.create(Registries.ITEM, new ResourceLocation("curios:" + forge));
            this.fabric = TagKey.create(Registries.ITEM, new ResourceLocation("trinkets:" + fabric));
        }

        public TagKey<Item> getFabric() {
            return fabric;
        }

        public TagKey<Item> getForge() {
            return forge;
        }
    }

}
