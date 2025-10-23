package net.threetag.palladium.client.renderer.entity.state;

import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.item.DyeColor;

public class SuitStandRenderState extends ArmorStandRenderState {

    public int id;
    public DyeColor color;
    public boolean rainbowColor;

    public int getDyeColor() {
        return this.rainbowColor ? ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, this.ageInTicks) : ColorLerper.Type.SHEEP.getColor(this.color);
    }

}
