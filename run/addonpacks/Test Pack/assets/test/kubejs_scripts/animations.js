// Register custom animations
onEvent('palladium.animations.register', (event) => {
    event.register(
        'test/headbang',
        100,
        (entity) => {
            return entity.isCrouching();
        },
        (model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks) => {
            animationUtil.interpolateXRotTo(model.head, Math.sin(entity.getTicksExisted() + partialTicks), 1);
        });
});