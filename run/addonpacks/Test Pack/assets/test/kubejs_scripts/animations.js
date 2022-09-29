// Register custom animations
PalladiumEvents.registerAnimations((event) => {
    event.register(
        'test/headbang',
        100,
        (entity) => {
            return entity.isOnGround();
        },
        (model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks) => {
            animationUtil.interpolateXRotTo(model.head, Math.sin(entity.tickCount + partialTicks), 1);
        });
});