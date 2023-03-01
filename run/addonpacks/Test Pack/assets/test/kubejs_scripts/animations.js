// Register custom animations
PalladiumEvents.registerAnimations((event) => {
    event.register('test/ability_test', 10, (builder) => {

        // Gets the current animation timer progress from the ability, returned value is a number from 0.0 to 1.0
        const progress = animationUtil.getAnimationTimerAbilityValue(builder.getPlayer(), 'test:kubejs_animation_test', 'toggle_animation', builder.getPartialTicks());

        // only apply animation if progress is above 0!
        if (progress > 0.0) {
            const halfPi = 1.57079632679;

            // if first person
            if (builder.isFirstPerson()) {
                builder.get('right_arm')
                    .rotateX(10)
                    .animate('InOutElastic', progress);
            } else {
                // third person animations
                builder.get('right_arm')
                    .rotateX(builder.getModel().head.xRot - halfPi)
                    .rotateY(builder.getModel().head.yRot)
                    .rotateZ(builder.getModel().head.zRot)
                    .animate('InOutElastic', progress);
            }
        }

    });
});