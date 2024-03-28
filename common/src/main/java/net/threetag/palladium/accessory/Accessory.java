package net.threetag.palladium.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.SupporterHandler;
import net.threetag.palladiumcore.registry.PalladiumRegistry;
import net.threetag.palladiumcore.util.Platform;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class Accessory {

    public static final PalladiumRegistry<Accessory> REGISTRY = PalladiumRegistry.create(Accessory.class, Palladium.id("accessories"));
    private boolean exclusive = false;

    public boolean isAvailable(Player entity) {
        return this.isAvailable(SupporterHandler.getPlayerData(entity.getUUID()));
    }

    public boolean isAvailable(SupporterHandler.PlayerData data) {
        return !this.exclusive || !Platform.isProduction() || data.hasAccessory(this);
    }

    public Component getDisplayName() {
        return Component.translatable(Util.makeDescriptionId("accessory", REGISTRY.getKey(this)));
    }

    @Override
    public String toString() {
        return REGISTRY.getKey(this).toString();
    }

    public Accessory setExclusive() {
        this.exclusive = true;
        return this;
    }

    @Environment(EnvType.CLIENT)
    public void onReload(EntityModelSet entityModelSet) {

    }

    @Environment(EnvType.CLIENT)
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Environment(EnvType.CLIENT)
    public void renderArm(HumanoidArm arm, AbstractClientPlayer player, PlayerRenderer playerRenderer, ModelPart armPart, ModelPart armWearPart, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
    }

    @Environment(EnvType.CLIENT)
    public boolean isVisible(AccessorySlot slot, AbstractClientPlayer player, boolean isFirstPerson) {
        return (slot.getCorrespondingEquipmentSlot() == null || player.getItemBySlot(slot.getCorrespondingEquipmentSlot()).isEmpty()) && !slot.wasHidden(player, isFirstPerson);
    }

    @Environment(EnvType.CLIENT)
    public boolean canRenderAsArm(AccessorySlot slot, HumanoidArm arm, AbstractClientPlayer player) {
        if (player.getMainArm() == HumanoidArm.RIGHT) {
            if (slot == AccessorySlot.MAIN_ARM || slot == AccessorySlot.MAIN_HAND) {
                return arm == HumanoidArm.RIGHT;
            } else if (slot == AccessorySlot.OFF_ARM || slot == AccessorySlot.OFF_HAND) {
                return arm == HumanoidArm.LEFT;
            }
        } else {
            if (slot == AccessorySlot.MAIN_ARM || slot == AccessorySlot.MAIN_HAND) {
                return arm == HumanoidArm.LEFT;
            } else if (slot == AccessorySlot.OFF_ARM || slot == AccessorySlot.OFF_HAND) {
                return arm == HumanoidArm.RIGHT;
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static ModelPart getArm(PlayerModel<?> model, boolean mainHand, HumanoidArm primaryHand) {
        if (mainHand) {
            return primaryHand == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        } else {
            return primaryHand == HumanoidArm.RIGHT ? model.leftArm : model.rightArm;
        }
    }

    @Environment(EnvType.CLIENT)
    @Nullable
    public RenderType getRenderType(AbstractClientPlayer player, ResourceLocation resourceLocation, Model model) {
        boolean flag = !player.isInvisible();
        boolean flag1 = !flag && !player.isInvisibleTo(Minecraft.getInstance().player);
        if (flag1) {
            return RenderType.entityTranslucent(resourceLocation);
        } else if (flag) {
            return model.renderType(resourceLocation);
        } else {
            return Minecraft.getInstance().shouldEntityAppearGlowing(player) ? RenderType.outline(resourceLocation) : null;
        }
    }

    public abstract Collection<AccessorySlot> getPossibleSlots();

    public static List<Accessory> getAvailableAccessories(SupporterHandler.PlayerData data) {
        List<Accessory> list = new ArrayList<>();

        for (Accessory accessory : Accessory.REGISTRY.getValues()) {
            if (accessory.isAvailable(data)) {
                list.add(accessory);
            }
        }

        return list;
    }

    public static List<Accessory> getAvailableAccessories(SupporterHandler.PlayerData data, AccessorySlot slot) {
        List<Accessory> list = new ArrayList<>();

        for (Accessory accessory : Accessory.REGISTRY.getValues()) {
            if (accessory.getPossibleSlots().contains(slot) && accessory.isAvailable(data)) {
                list.add(accessory);
            }
        }

        return list;
    }

    public static Optional<AccessoryPlayerData> getPlayerData(Player player) {
        if (player instanceof PalladiumPlayerExtension ext) {
            return Optional.of(ext.palladium$getAccessories());
        } else {
            return Optional.empty();
        }
    }

    public static class ReloadManager implements ResourceManagerReloadListener {

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            for (Accessory accessory : Accessory.REGISTRY.getValues()) {
                accessory.onReload(Minecraft.getInstance().getEntityModels());
            }
        }
    }
}
