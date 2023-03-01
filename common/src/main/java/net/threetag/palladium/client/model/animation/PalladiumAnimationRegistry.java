package net.threetag.palladium.client.model.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.event.PalladiumClientEvents;

import java.util.*;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class PalladiumAnimationRegistry extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<ResourceLocation, PalladiumAnimation> animations = new LinkedHashMap<>();
    private final List<PalladiumAnimation> animationsSorted = new LinkedList<>();
    public static float PARTIAL_TICK = 0F;
    public static float FIRST_PERSON_PARTIAL_TICK = 0F;
    public static boolean SKIP_ANIMATIONS = false;
    public static final PalladiumAnimationRegistry INSTANCE = new PalladiumAnimationRegistry();

    public PalladiumAnimationRegistry() {
        super(GSON, "palladium/animations");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.animations.clear();
        this.animationsSorted.clear();

        PalladiumClientEvents.REGISTER_ANIMATIONS.invoker().register(this::registerAnimation);

        this.animationsSorted.addAll(this.animations.values());
        this.animationsSorted.sort(Comparator.comparingInt(PalladiumAnimation::getPriority));
    }

    public void registerAnimation(ResourceLocation id, PalladiumAnimation animation) {
        animations.put(id, animation);
    }

    public static void resetPoses(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts) {
        for (ModelPart bodyPart : headParts) {
            bodyPart.resetPose();
        }

        for (ModelPart bodyPart : bodyParts) {
            bodyPart.resetPose();
        }
    }

    public static void forEach(AbstractClientPlayer player, HumanoidModel<?> model, PalladiumAnimation.FirstPersonContext firstPersonContext, float partialTicks, BiConsumer<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> consumer) {
        Map<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> gathered = new HashMap<>();

        for (PalladiumAnimation animation : INSTANCE.animationsSorted) {
            PalladiumAnimation.Builder builder = new PalladiumAnimation.Builder();
            animation.animate(builder, player, model, firstPersonContext, partialTicks);
            for (Map.Entry<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> entry : builder.getAnimationData().entrySet()) {
                consumer.accept(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void forEach(AbstractClientPlayer player, HumanoidModel<?> model, PalladiumAnimation.FirstPersonContext firstPersonContext, float partialTicks, BiConsumer<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> consumer, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Map<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> gathered = new HashMap<>();

        for (PalladiumAnimation animation : INSTANCE.animationsSorted) {
            PalladiumAnimation.Builder builder = new PalladiumAnimation.Builder(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            animation.animate(builder, player, model, firstPersonContext, partialTicks);
            for (Map.Entry<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> entry : builder.getAnimationData().entrySet()) {
                consumer.accept(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void applyAnimations(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof AbstractClientPlayer player) {
            forEach(player, model, PalladiumAnimation.FirstPersonContext.NONE, PARTIAL_TICK, (part, data) -> {
                part.applyToModelPart(model, data);
            }, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    public static void applyFirstPersonAnimations(PoseStack poseStack, AbstractClientPlayer player, HumanoidModel<?> model, boolean rightArm) {
        PalladiumAnimation.PoseStackResult result = new PalladiumAnimation.PoseStackResult();
        forEach(player, model, rightArm ? PalladiumAnimation.FirstPersonContext.RIGHT_ARM : PalladiumAnimation.FirstPersonContext.LEFT_ARM, FIRST_PERSON_PARTIAL_TICK, (part, data) -> {
            if (rightArm && part == PalladiumAnimation.PlayerModelPart.RIGHT_ARM) {
                data.apply(result);
            }

            if (!rightArm && part == PalladiumAnimation.PlayerModelPart.LEFT_ARM) {
                data.apply(result);
            }
        });
        result.apply(poseStack);
    }

    public static void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        PalladiumAnimation.PoseStackResult result = new PalladiumAnimation.PoseStackResult();
        forEach(player, playerRenderer.getModel(), PalladiumAnimation.FirstPersonContext.NONE, partialTicks, (part, data) -> {
            if (part == PalladiumAnimation.PlayerModelPart.BODY) {
                data.apply(result);
            }
        });
        result.apply(poseStack);
    }

}
