package net.threetag.palladium.client.renderer.renderlayer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.RenderLayerAbility;

public class PackRenderLayerRenderer extends RenderLayer<LivingEntity, HumanoidModel<LivingEntity>> {

    public PackRenderLayerRenderer(RenderLayerParent<LivingEntity, HumanoidModel<LivingEntity>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (AbilityEntry entry : Ability.getEntries(livingEntity, Abilities.RENDER_LAYER.get())) {
            IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(entry.getProperty(RenderLayerAbility.RENDER_LAYER));

            if (layer != null) {
                layer.render(livingEntity, entry, matrixStack, buffer, this.getParentModel(), packedLight, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }
}
