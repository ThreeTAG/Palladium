package net.threetag.palladium.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.core.RegistryEntry;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncAccessoriesMessage;
import net.threetag.palladium.util.SupporterHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class Accessory extends RegistryEntry<Accessory> {

    public static final ResourceKey<Registry<Accessory>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "accessories"));
    public static final Registrar<Accessory> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new Accessory[0]).build();

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(player -> Accessory.getPlayerData(player).ifPresent(data -> new SyncAccessoriesMessage(player.getId(), data.accessories).sendTo(player)));

        PalladiumEvents.START_TRACKING.register((tracker, target) -> {
            if (target instanceof Player player && tracker instanceof ServerPlayer serverPlayer) {
                Accessory.getPlayerData(player).ifPresent(data -> new SyncAccessoriesMessage(player.getId(), data.accessories).sendTo(serverPlayer));
            }
        });
    }

    public boolean isAvailable(Player entity) {
        return Platform.isDevelopmentEnvironment() || SupporterHandler.getPlayerData(entity.getUUID()).hasAccessory(this);
    }

    public Component getDisplayName() {
        return new TranslatableComponent(Util.makeDescriptionId("accessory", REGISTRY.getId(this)));
    }

    @Environment(EnvType.CLIENT)
    public void onReload(EntityModelSet entityModelSet) {

    }

    @Environment(EnvType.CLIENT)
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Environment(EnvType.CLIENT)
    public boolean isVisible(AccessorySlot slot, AbstractClientPlayer player) {
        return slot.getCorrespondingEquipmentSlot() == null || player.getItemBySlot(slot.getCorrespondingEquipmentSlot()).isEmpty();
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

        for (Accessory Accessory : Accessory.REGISTRY) {
            if (Platform.isDevelopmentEnvironment() || data.hasAccessory(Accessory)) {
                list.add(Accessory);
            }
        }

        return list;
    }

    public static List<Accessory> getAvailableAccessories(SupporterHandler.PlayerData data, AccessorySlot slot) {
        List<Accessory> list = new ArrayList<>();

        for (Accessory Accessory : Accessory.REGISTRY) {
            if (Accessory.getPossibleSlots().contains(slot) && data.hasAccessory(Accessory)) {
                list.add(Accessory);
            }
        }

        return list;
    }

    @ExpectPlatform
    public static Optional<AccessoryPlayerData> getPlayerData(Player player) {
        throw new AssertionError();
    }

    public static class ReloadManager implements ResourceManagerReloadListener {

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            for (Accessory accessory : Accessory.REGISTRY) {
                accessory.onReload(Minecraft.getInstance().getEntityModels());
            }
        }
    }
}
