package net.threetag.palladium.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class CapedHumanoidModel<T extends LivingEntity> extends HumanoidModel<T> {

    public final ModelPart cape;

    public CapedHumanoidModel(ModelPart modelPart) {
        super(modelPart);
        this.cape = this.body.getChild("cape");
    }

    public CapedHumanoidModel(ModelPart modelPart, Function<ResourceLocation, RenderType> function) {
        super(modelPart, function);
        this.cape = this.body.getChild("cape");
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);

        if (entity instanceof Player player) {
            double d0 = Mth.lerp(partialTicks, player.xCloakO, player.xCloak) - Mth.lerp(partialTicks, entity.xo, entity.getX());
            double d1 = Mth.lerp(partialTicks, player.yCloakO, player.yCloak) - Mth.lerp(partialTicks, entity.yo, entity.getY());
            double d2 = Mth.lerp(partialTicks, player.zCloakO, player.zCloak) - Mth.lerp(partialTicks, entity.zo, entity.getZ());
            float f = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO);
            double d3 = Mth.sin(f * ((float) Math.PI / 180F));
            double d4 = -Mth.cos(f * ((float) Math.PI / 180F));
            float f1 = (float) d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            float f4 = Mth.lerp(partialTicks, player.oBob, player.bob);
            f1 = f1 + Mth.sin(Mth.lerp(partialTicks, entity.walkDistO, entity.walkDist) * 6.0F) * 32.0F * f4;

            float rotation = 6.0F + f2 / 2.0F + f1;
            this.cape.xRot = (float) Math.toRadians(rotation + 10F);
        }
    }

}
