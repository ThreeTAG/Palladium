package net.threetag.palladium.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class SimpleArmorMaterial implements net.minecraft.world.item.ArmorMaterial {

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final Supplier<SoundEvent> soundEvent;
    private final float toughness;
    private final float knockBackResistance;
    private final Supplier<Ingredient> repairMaterial;

    public SimpleArmorMaterial(String nameIn, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, Supplier<SoundEvent> equipSoundIn, float toughness, float knockBackResistance, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = nameIn;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.soundEvent = equipSoundIn;
        this.toughness = toughness;
        this.knockBackResistance = knockBackResistance;
        this.repairMaterial = repairMaterialSupplier;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slotIn) {
        return this.slotProtections[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockBackResistance;
    }
}
