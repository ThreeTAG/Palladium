package net.threetag.palladium.addonpack.builder;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.item.SuitSet;

public class SuitSetBuilder extends AddonBuilder<SuitSet, SuitSetBuilder> {

    private ResourceLocation mainHandBuilder, offHandBuilder, helmetBuilder, chestplateBuilder, leggingsBuilder, bootsBuilder;

    public SuitSetBuilder(ResourceLocation id) {
        super(id);
    }

    public SuitSetBuilder mainHand(ResourceLocation itemBuilder) {
        this.mainHandBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder offHand(ResourceLocation itemBuilder) {
        this.offHandBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder helmet(ResourceLocation itemBuilder) {
        this.helmetBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder chestplate(ResourceLocation itemBuilder) {
        this.chestplateBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder leggings(ResourceLocation itemBuilder) {
        this.leggingsBuilder = itemBuilder;
        return this;
    }

    public SuitSetBuilder boots(ResourceLocation itemBuilder) {
        this.bootsBuilder = itemBuilder;
        return this;
    }

    @Override
    protected SuitSet create() {
        return new SuitSet(this.mainHandBuilder, this.offHandBuilder, this.helmetBuilder, this.chestplateBuilder, this.leggingsBuilder, this.bootsBuilder);
    }
}
