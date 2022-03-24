package net.threetag.palladium.addonpack.builder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.threetag.palladium.item.SuitSet;

public class SuitSetBuilder extends AddonBuilder<SuitSet> {

    private AddonBuilder<Item> mainHandBuilder, offHandBuilder, helmetBuilder, chestplateBuilder, leggingsBuilder, bootsBuilder;

    public SuitSetBuilder(ResourceLocation id) {
        super(id);
    }

    public SuitSetBuilder mainHand(AddonBuilder<Item> itemBuilder) {
        this.mainHandBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder offHand(AddonBuilder<Item> itemBuilder) {
        this.offHandBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder helmet(AddonBuilder<Item> itemBuilder) {
        this.helmetBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder chestplate(AddonBuilder<Item> itemBuilder) {
        this.chestplateBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder leggings(AddonBuilder<Item> itemBuilder) {
        this.leggingsBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder boots(AddonBuilder<Item> itemBuilder) {
        this.bootsBuilder = itemBuilder;
        return this;
    }

    @Override
    protected SuitSet create() {
        return new SuitSet(this.mainHandBuilder, this.offHandBuilder, this.helmetBuilder, this.chestplateBuilder, this.leggingsBuilder, this.bootsBuilder);
    }
}
