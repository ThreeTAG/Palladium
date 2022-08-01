package net.threetag.palladium.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;

public class CurioTrinketRegistry {

    public static final Slot HAT = new Slot("head", "head/hat");
    public static final Slot NECKLACE = new Slot("necklace", "chest/necklace");
    public static final Slot BACK = new Slot("back", "chest/back");
    public static final Slot CAPE = new Slot("back", "chest/cape");
    public static final Slot BELT = new Slot("belt", "legs/belt");
    public static final Slot HAND = new Slot("hand", "hand/glove");
    public static final Slot OFFHAND = new Slot("hand", "offhand/glove");
    public static final Slot RING = new Slot("hand", "hand/ring");
    public static final Slot OFFHAND_RING = new Slot("ring", "offhand/ring");

    @ExpectPlatform
    public static void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        throw new AssertionError();
    }

    public static void registerCurioTrinket(Item item) {
        if (item instanceof CurioTrinket curioTrinket) {
            registerCurioTrinket(item, curioTrinket);
        } else {
            throw new RuntimeException("Tried to register item that isn't implementing CurioTrinket");
        }
    }

    @ExpectPlatform
    public static boolean equipItem(Player user, ItemStack stack) {
        throw new AssertionError();
    }

    @Environment(EnvType.CLIENT)
    @ExpectPlatform
    public static void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        throw new AssertionError();
    }

    public static class Slot {

        private final TagKey<Item> forge, fabric;

        public Slot(String forge, String fabric) {
            this.forge = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("curios:" + forge));
            this.fabric = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("trinkets:" + fabric));
        }

        public TagKey<Item> getFabric() {
            return fabric;
        }

        public TagKey<Item> getForge() {
            return forge;
        }
    }

}
