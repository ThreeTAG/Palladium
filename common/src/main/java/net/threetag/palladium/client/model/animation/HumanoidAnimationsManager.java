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
public class HumanoidAnimationsManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<ResourceLocation, Animation> animations = new LinkedHashMap<>();
    private final List<Animation> animationsSorted = new ArrayList<>();
    public static float PARTIAL_TICK = 0F;
    public static float FIRST_PERSON_PARTIAL_TICK = 0F;
    public static boolean SKIP_ANIMATIONS = false;
    public static final HumanoidAnimationsManager INSTANCE = new HumanoidAnimationsManager();

    public HumanoidAnimationsManager() {
        super(GSON, "models/animations");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.animations.clear();
        this.animationsSorted.clear();

        PalladiumClientEvents.REGISTER_ANIMATIONS.invoker().register(this::registerAnimation);

        this.animationsSorted.addAll(this.animations.values());
        this.animationsSorted.sort(Comparator.comparingInt(Animation::getPriority));
    }

    public void registerAnimation(ResourceLocation id, Animation animation) {
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

    public static void applyAnimations(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for (Animation animation : INSTANCE.animationsSorted) {
            if (animation.active(entity)) {
                animation.setupAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, PARTIAL_TICK);
            }
        }
    }

    public static void applyFirstPersonAnimations(PoseStack poseStack, AbstractClientPlayer player, boolean rightArm) {
        for (Animation animation : INSTANCE.animationsSorted) {
            if (animation.active(player)) {
                animation.setupFirstPersonAnimation(poseStack, player, rightArm, FIRST_PERSON_PARTIAL_TICK);
            }
        }
    }

    public static void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        for (Animation animation : INSTANCE.animationsSorted) {
            if (animation.active(player)) {
                // TODO dont allow PoseStack transformations, use something more compatible across multiple animations
                animation.setupRotations(playerRenderer, player, poseStack, ageInTicks, rotationYaw, partialTicks);
            }
        }
    }

}
