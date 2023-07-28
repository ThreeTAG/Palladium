package net.threetag.palladium.client.renderer.item.armor;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.item.ArmorWithRenderer;
import net.threetag.palladiumcore.util.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmorRendererManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final List<ArmorWithRenderer> EXTENDED_ARMOR_ITEMS = new ArrayList<>();
    private static boolean LOOK_FOR_ITEMS = false;
    public static ArmorRendererManager INSTANCE;

    public Map<ResourceLocation, ArmorRendererData> byName = ImmutableMap.of();

    public ArmorRendererManager() {
        super(GSON, "palladium/armor_renderers");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        if (!LOOK_FOR_ITEMS) {
            for (Item item : Registry.ITEM) {
                if (item instanceof ArmorWithRenderer armor) {
                    EXTENDED_ARMOR_ITEMS.add(armor);
                }
            }
            LOOK_FOR_ITEMS = true;
        }

        EXTENDED_ARMOR_ITEMS.forEach(i -> i.setCachedArmorRenderer(null));
        var entityModelSet = Minecraft.getInstance().getEntityModels();
        ImmutableMap.Builder<ResourceLocation, ArmorRendererData> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                var data = ArmorRendererData.fromJson(GsonHelper.convertToJsonObject(json, "$"));
                data.buildModels(entityModelSet);
                builder.put(id, data);
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading armor renderer {}", id, e);
            }
        });
        this.byName = builder.build();
        EXTENDED_ARMOR_ITEMS.forEach(i -> i.setCachedArmorRenderer(this.byName.get(Registry.ITEM.getKey((Item) i))));

        AddonPackLog.info("Loaded {} armor renderers", this.byName.size());
    }

    public static void renderFirstPerson(AbstractClientPlayer player, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
        var stack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!stack.isEmpty() && stack.getItem() instanceof ArmorWithRenderer item && item.getCachedArmorRenderer() instanceof ArmorRendererData renderer) {
            var context = DataContext.forArmorInSlot(player, EquipmentSlot.CHEST);
            var armorModel = renderer.getModel(player, context);
            var vertex = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(renderer.getTexture(context)), false, stack.hasFoil());
            var arm = rightArm ? armorModel.rightArm : armorModel.leftArm;
            arm.copyFrom(rendererArm);
            arm.xRot = 0.0F;
            arm.render(poseStack, vertex, combinedLight, OverlayTexture.NO_OVERLAY);

            if (armorModel instanceof PlayerModel<?> playerModel) {
                arm = rightArm ? playerModel.rightSleeve : playerModel.leftSleeve;
                arm.copyFrom(rendererArm);
                arm.xRot = 0.0F;
                arm.render(poseStack, vertex, combinedLight, OverlayTexture.NO_OVERLAY);
            }

            if (Platform.isModLoaded("geckolib3")) {
                GeckoLibCompat.renderFirstPerson(player, stack, poseStack, buffer, combinedLight, rendererArm, rightArm);
            }
        }
    }
}
