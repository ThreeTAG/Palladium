// Objectives names to receive from the server
const OBJECTIVE_NAMES = [
    "TestScoreName",
];

// Event for registering HUDs
PalladiumEvents.registerGuiOverlays((event) => {
    event.register(
        // ID for the overlay
        'test/synced_score',

        // Function for rendering
        (minecraft, gui, poseStack, partialTick, screenWidth, screenHeight) => {
            const score = palladium.getProperty(minecraft.player, "TestScoreName");

            guiUtil.drawString(poseStack, Component.string("Test Score: " + score), 40, 10, 0xffffff);
        });
});

PalladiumEvents.registerPropertiesClientSided((event) => {
    if (event.getEntityType() === "minecraft:player") {
        for (let i = 0; i < OBJECTIVE_NAMES.length; i++) {
            event.registerProperty(OBJECTIVE_NAMES[i], 'integer', 0);
        }
    }
});