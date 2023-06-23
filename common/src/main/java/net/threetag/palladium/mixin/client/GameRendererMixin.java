package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.ShaderEffectAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private PostChain postEffect;

    @Shadow
    public abstract void loadEffect(ResourceLocation resourceLocation);

    @Shadow
    public abstract Camera getMainCamera();

    @Shadow
    public abstract Minecraft getMinecraft();

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(PoseStack matrixStack, float partialTicks, CallbackInfo ci) {
        if (this.minecraft.getCameraEntity() instanceof LivingEntity livingEntity && !AbilityUtil.getEnabledEntries(livingEntity, Abilities.ENERGY_BLAST.get()).isEmpty()) {
            ci.cancel();
        }
    }

    @Inject(method = "checkEntityPostEffect", at = @At("RETURN"))
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (this.postEffect == null && this.minecraft.player != null) {
            var shader = ShaderEffectAbility.get(this.minecraft.player);
            if (shader != null) {
                this.loadEffect(shader);
            }
        }
    }

    private HashMap<BlockPos, BlockState> savedStates = new HashMap<>();

    @SuppressWarnings("DataFlowIssue")
    @Inject(at = @At(value = "HEAD"), method = "render")
    private void beforeRender(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        if (this.getMainCamera().getEntity() instanceof LivingEntity living && AbilityUtil.isTypeEnabled(living, Abilities.INTANGIBILITY.get())) {
            Set<BlockPos> eyePositions = getEyePos(0.25F, 0.05F, 0.25F);
            Set<BlockPos> noLongerEyePositions = new HashSet<>();
            for (BlockPos p : savedStates.keySet()) {
                if (!eyePositions.contains(p)) {
                    noLongerEyePositions.add(p);
                }
            }
            for (BlockPos eyePosition : noLongerEyePositions) {
                BlockState state = savedStates.get(eyePosition);
                this.getMinecraft().level.setBlockAndUpdate(eyePosition, state);
                savedStates.remove(eyePosition);
            }
            for (BlockPos p : eyePositions) {
                BlockState stateAtP = this.getMinecraft().level.getBlockState(p);
                if (!savedStates.containsKey(p) && !this.getMinecraft().level.isEmptyBlock(p) && !(stateAtP.getBlock() instanceof LiquidBlock)) {
                    savedStates.put(p, stateAtP);
                    this.getMinecraft().level.setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                }
            }
        } else if (savedStates.size() > 0) {
            Set<BlockPos> noLongerEyePositions = new HashSet<>(savedStates.keySet());
            for (BlockPos eyePosition : noLongerEyePositions) {
                BlockState state = savedStates.get(eyePosition);
                this.getMinecraft().level.setBlockAndUpdate(eyePosition, state);
                savedStates.remove(eyePosition);
            }
        }
    }

    private Set<BlockPos> getEyePos(float rangeX, float rangeY, float rangeZ) {
        Vec3 pos = this.getMainCamera().getEntity().position().add(0, this.getMainCamera().getEntity().getEyeHeight(this.getMainCamera().getEntity().getPose()), 0);
        AABB cameraBox = new AABB(pos, pos);
        cameraBox = cameraBox.inflate(rangeX, rangeY, rangeZ);
        HashSet<BlockPos> set = new HashSet<>();
        BlockPos.betweenClosedStream(cameraBox).forEach(p -> set.add(p.immutable()));
        return set;
    }

}
