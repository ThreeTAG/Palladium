package net.threetag.palladium.item;

import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SuitSet extends RegistryEntry<SuitSet> {

    public static final ResourceKey<Registry<SuitSet>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "suit_sets"));
    public static final Registrar<SuitSet> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new SuitSet[0]).build();

    private final Supplier<Item> mainHand, offHand, helmet, chestplate, leggings, boots;

    public SuitSet(@Nullable Supplier<Item> mainHand, @Nullable Supplier<Item> offHand, @Nullable Supplier<Item> helmet, @Nullable Supplier<Item> chestplate, @Nullable Supplier<Item> leggings, @Nullable Supplier<Item> boots) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    @Nullable
    public Item getMainHand() {
        return this.mainHand != null ? this.mainHand.get() : null;
    }

    @Nullable
    public Item getOffHand() {
        return this.offHand != null ? this.offHand.get() : null;
    }

    @Nullable
    public Item getHelmet() {
        return this.helmet != null ? this.helmet.get() : null;
    }

    @Nullable
    public Item getChestplate() {
        return this.chestplate.get();
    }

    @Nullable
    public Item getLeggings() {
        return this.leggings != null ? this.leggings.get() : null;
    }

    @Nullable
    public Item getBoots() {
        return this.boots != null ? this.boots.get() : null;
    }

    public boolean isWearing(LivingEntity entity) {
        if (this.getChestplate() != null && !entity.getItemBySlot(EquipmentSlot.CHEST).is(this.getChestplate())) {
            return false;
        }

        if (this.getMainHand() != null && !entity.getItemBySlot(EquipmentSlot.MAINHAND).is(this.getMainHand())) {
            return false;
        }

        if (this.getOffHand() != null && !entity.getItemBySlot(EquipmentSlot.OFFHAND).is(this.getOffHand())) {
            return false;
        }

        if (this.getHelmet() != null && !entity.getItemBySlot(EquipmentSlot.HEAD).is(this.getHelmet())) {
            return false;
        }

        if (this.getLeggings() != null && !entity.getItemBySlot(EquipmentSlot.LEGS).is(this.getLeggings())) {
            return false;
        }

        return this.getBoots() == null || entity.getItemBySlot(EquipmentSlot.FEET).is(this.getBoots());
    }
}
