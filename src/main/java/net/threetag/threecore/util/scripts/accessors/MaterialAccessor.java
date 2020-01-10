package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.block.material.Material;

public class MaterialAccessor extends ScriptAccessor<Material> {

    public MaterialAccessor(Material value) {
        super(value);
    }

    public boolean isLiquid() {
        return this.value.isLiquid();
    }

    public boolean isSolid() {
        return this.value.isSolid();
    }

    public boolean blocksMovement() {
        return this.value.blocksMovement();
    }

    public boolean isFlammable() {
        return this.value.isFlammable();
    }

    public boolean isReplaceable() {
        return this.value.isReplaceable();
    }

    public boolean isOpaque() {
        return this.value.isOpaque();
    }

    public boolean isToolNotRequired() {
        return this.value.isToolNotRequired();
    }

    public String getPushReaction() {
        return this.value.toString().toLowerCase();
    }

}
