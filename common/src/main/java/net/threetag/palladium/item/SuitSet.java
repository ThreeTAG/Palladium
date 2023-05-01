package net.threetag.palladium.item;

import net.minecraft.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.PalladiumRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SuitSet {

    public static final PalladiumRegistry<SuitSet> REGISTRY = PalladiumRegistry.create(SuitSet.class, Palladium.id("suit_sets"));

    private final Supplier<Item> mainHand, offHand, helmet, chestplate, leggings, boots;
    @Nullable
    private String descriptionId;

    public SuitSet(@Nullable Supplier<Item> mainHand, @Nullable Supplier<Item> offHand, @Nullable Supplier<Item> helmet, @Nullable Supplier<Item> chestplate, @Nullable Supplier<Item> leggings, @Nullable Supplier<Item> boots) {
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("suitset", REGISTRY.getKey(this));
        }

        return this.descriptionId;
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
        return this.chestplate != null ? this.chestplate.get() : null;
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
