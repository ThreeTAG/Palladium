package net.threetag.palladium.client.model.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
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
    public static final Map<ModelPart, ModelPartState> CACHE = new HashMap<>();
    public static float PARTIAL_TICK = 0F;
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
        animations.put( id, animation);
    }

    public static void cacheOrResetModelParts(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts) {
        for (ModelPart bodyPart : headParts) {
            cacheOrResetModelPart(bodyPart);
        }

        for (ModelPart bodyPart : bodyParts) {
            cacheOrResetModelPart(bodyPart);
        }
    }

    private static void cacheOrResetModelPart(ModelPart modelPart) {
        ModelPartState state = CACHE.get(modelPart);

        if (state != null) {
            state.apply(modelPart);
        } else {
            CACHE.put(modelPart, new ModelPartState(modelPart));
        }

        for (ModelPart value : modelPart.children.values()) {
            cacheOrResetModelPart(value);
        }
    }

    public static void pre(AgeableListModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (model instanceof HumanoidModel<?> humanoidModel) {
            for (Animation animation : INSTANCE.animationsSorted) {
                if (animation.active(entity)) {
                    animation.setupAnimation(humanoidModel, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, PARTIAL_TICK);
                }
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

    public static class ModelPartState {

        public final float x;
        public final float y;
        public final float z;
        public final float xRot;
        public final float yRot;
        public final float zRot;
        public final boolean visible;

        public ModelPartState(ModelPart modelPart) {
            this.x = modelPart.x;
            this.y = modelPart.y;
            this.z = modelPart.z;
            this.xRot = modelPart.xRot;
            this.yRot = modelPart.yRot;
            this.zRot = modelPart.zRot;
            this.visible = modelPart.visible;
        }

        public void apply(ModelPart modelPart) {
            modelPart.x = this.x;
            modelPart.y = this.y;
            modelPart.z = this.z;
            modelPart.xRot = this.xRot;
            modelPart.yRot = this.yRot;
            modelPart.zRot = this.zRot;
            modelPart.visible = this.visible;
        }
    }

}
