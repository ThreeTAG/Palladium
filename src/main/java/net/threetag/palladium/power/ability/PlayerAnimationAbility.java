package net.threetag.palladium.power.ability;

public class PlayerAnimationAbility {

//    public static final PalladiumProperty<ResourceLocation> ANIMATION = new ResourceLocationProperty("animation").configurable("ID of the animation");
//
//    public PlayerAnimationAbility() {
//        this.withProperty(ANIMATION, new ResourceLocation("example:animation"));
//    }
//
//    @Override
//    public void firstTick(LivingEntity entity, AbilityInstance<?> entry, PowerHolder holder, boolean enabled) {
//        if (enabled && entity.level().isClientSide && entity instanceof AnimatablePlayer animatablePlayer) {
//            var animationContainer = animatablePlayer.palladium_getModifierLayer();
//            KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(entry.getProperty(ANIMATION));
//
//            if (anim != null) {
//                animationContainer.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value), new KeyframeAnimationPlayer(anim));
//            }
//        }
//    }
//
//    @Override
//    public String getDocumentationDescription() {
//        return "Allows you to play a custom player animation.";
//    }
//
//    @Override
//    public boolean isExperimental() {
//        return true;
//    }
}
