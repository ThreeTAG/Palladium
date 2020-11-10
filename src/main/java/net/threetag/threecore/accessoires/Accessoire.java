package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.threetag.threecore.ThreeCore;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Accessoire extends ForgeRegistryEntry<Accessoire> {

    public static IForgeRegistry<Accessoire> REGISTRY;

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<Accessoire>().setName(new ResourceLocation(ThreeCore.MODID, "accessoires")).setType(Accessoire.class).setIDRange(0, 512).create();
    }

    public abstract boolean isAvailable(PlayerEntity entity);

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(Util.makeTranslationKey("accessoire", this.getRegistryName()));
    }

    @OnlyIn(Dist.CLIENT)
    public void apply(AbstractClientPlayerEntity player) {
    }

    @OnlyIn(Dist.CLIENT)
    public void remove(AbstractClientPlayerEntity player) {
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public RenderType getRenderType(AbstractClientPlayerEntity player, ResourceLocation resourceLocation, Model model) {
        boolean flag = !player.isInvisible();
        boolean flag1 = !flag && !player.isInvisibleToPlayer(Minecraft.getInstance().player);
        if (flag1) {
            return RenderType.getEntityTranslucent(resourceLocation);
        } else if (flag) {
            return model.getRenderType(resourceLocation);
        } else {
            return player.isGlowing() ? RenderType.getOutline(resourceLocation) : null;
        }
    }

    @Nullable
    public PlayerPart getPlayerPart() {
        return null;
    }

    public enum PlayerPart {

        HEAD,
        CHEST,
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_LEG,
        LEFT_LEG;

        public void setVisibility(PlayerModel model, boolean visible) {
            switch (this) {
                case HEAD:
                    model.bipedHead.showModel = model.bipedBodyWear.showModel = visible;
                    return;
                case CHEST:
                    model.bipedBody.showModel = model.bipedBodyWear.showModel = visible;
                    return;
                case RIGHT_ARM:
                    model.bipedRightArm.showModel = model.bipedRightArmwear.showModel = visible;
                    return;
                case LEFT_ARM:
                    model.bipedLeftArm.showModel = model.bipedLeftArmwear.showModel = visible;
                    return;
                case RIGHT_LEG:
                    model.bipedRightLeg.showModel = model.bipedRightLegwear.showModel = visible;
                    return;
                default:
                    model.bipedLeftLeg.showModel = model.bipedLeftLegwear.showModel = visible;
            }
        }

    }

}
