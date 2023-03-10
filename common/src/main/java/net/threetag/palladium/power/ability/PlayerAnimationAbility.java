package net.threetag.palladium.power.ability;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.model.animation.IAnimatablePlayer;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class PlayerAnimationAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> ANIMATION = new ResourceLocationProperty("animation").configurable("ID of the animation");

    public PlayerAnimationAbility() {
        this.withProperty(ANIMATION, new ResourceLocation("example:animation"));
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level.isClientSide && entity instanceof IAnimatablePlayer animatablePlayer) {
            var animationContainer = animatablePlayer.palladium_getModifierLayer();
            KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(entry.getProperty(ANIMATION));

            if (anim != null) {
                animationContainer.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value), new KeyframeAnimationPlayer(anim));
            }
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Allows you to play a custom player animation.";
    }
}
