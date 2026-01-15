package net.threetag.palladium.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.power.ability.ShaderEffectAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract void setPostEffect(Identifier postEffectId);

    @Shadow
    private @Nullable Identifier postEffectId;

    @Inject(method = "checkEntityPostEffect", at = @At("RETURN"))
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (this.postEffectId == null && this.minecraft.player != null) {
            var shader = ShaderEffectAbility.get(this.minecraft.player);
            if (shader != null) {
                this.setPostEffect(shader);
            }
        }
    }

}
