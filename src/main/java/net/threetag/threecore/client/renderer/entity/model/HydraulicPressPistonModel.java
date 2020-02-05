package net.threetag.threecore.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HydraulicPressPistonModel extends Model {

    public final RendererModel plate1;
    public final RendererModel plate2;

    public HydraulicPressPistonModel() {
        textureWidth = 32;
        textureHeight = 32;

        this.plate1 = new RendererModel(this, 0, 16);
        this.plate1.addBox(12, -1, 4.0F, 1, 8, 8, 0.0F, false);
        this.plate2 = new RendererModel(this);
        this.plate2.addBox(3, -1, 4.0F, 1, 8, 8, 0.0F, false);
    }

    public void render(float progress, float scale) {
        this.plate1.rotationPointX = -progress * 4;
        this.plate2.rotationPointX = progress * 4;
        this.plate1.render(scale);
        this.plate2.render(scale);
    }
}
