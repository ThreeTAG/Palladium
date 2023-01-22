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

@Environment(EnvType.CLIENT)
public class PalladiumAnimationRegistry extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<ResourceLocation, PalladiumAnimation> animations = new LinkedHashMap<>();
    private final List<PalladiumAnimation> animationsSorted = new ArrayList<>();
    public static float PARTIAL_TICK = 0F;
    public static float FIRST_PERSON_PARTIAL_TICK = 0F;
    public static boolean SKIP_ANIMATIONS = false;
    public static final PalladiumAnimationRegistry INSTANCE = new PalladiumAnimationRegistry();

    public PalladiumAnimationRegistry() {
        super(GSON, "models/animations");
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

    public static Map<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> gather(AbstractClientPlayer player, HumanoidModel<?> model, PalladiumAnimation.FirstPersonContext firstPersonContext, float partialTicks) {
        Map<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> gathered = new HashMap<>();

        for (PalladiumAnimation animation : INSTANCE.animationsSorted) {
            PalladiumAnimation.Builder builder = new PalladiumAnimation.Builder();
            animation.animate(builder, player, model, firstPersonContext, partialTicks);
            for (Map.Entry<PalladiumAnimation.PlayerModelPart, PalladiumAnimation.PartAnimationData> entry : builder.getAnimationData().entrySet()) {
                if (!gathered.containsKey(entry.getKey())) {
                    gathered.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return gathered;
    }

    public static void applyAnimations(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof AbstractClientPlayer player) {
            gather(player, model, PalladiumAnimation.FirstPersonContext.NONE, PARTIAL_TICK).forEach((part, data) -> {
                part.applyToModelPart(model, data);
            });
        }
    }

    public static void applyFirstPersonAnimations(PoseStack poseStack, AbstractClientPlayer player, HumanoidModel<?> model, boolean rightArm) {
        var data = gather(player, model, rightArm ? PalladiumAnimation.FirstPersonContext.RIGHT_ARM : PalladiumAnimation.FirstPersonContext.LEFT_ARM, PARTIAL_TICK)
                .get(rightArm ? PalladiumAnimation.PlayerModelPart.RIGHT_ARM : PalladiumAnimation.PlayerModelPart.LEFT_ARM);

        if (data != null) {
            data.apply(poseStack);
        }
    }

    public static void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        var data = gather(player, playerRenderer.getModel(), PalladiumAnimation.FirstPersonContext.NONE, partialTicks).get(PalladiumAnimation.PlayerModelPart.BODY);

        if (data != null) {
            data.apply(poseStack);
        }
    }

}
