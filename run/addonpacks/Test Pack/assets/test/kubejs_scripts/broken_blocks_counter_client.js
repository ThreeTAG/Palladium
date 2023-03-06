// Name/key for the data, stored here to use everywhere else
const BROKEN_BLOCKS = 'broken_blocks';

// Resource Location of the bar texture. Using MC's here
const BAR_TEXTURE = 'minecraft:textures/gui/bars.png';


// Register the property on the client-side, so it should be the same code as in data/test/kubejs_scripts/broken_blocks_counter.js!
PalladiumEvents.registerPropertiesClientSided((event) => {
    // Only register for players
    if (event.getEntityType() === "minecraft:player") {
        // Arguments: Key of the property, type of the property, default/starting value
        event.registerProperty(BROKEN_BLOCKS, 'integer', 0);
    }
});


// Event for registering HUDs
PalladiumEvents.registerGuiOverlays((event) => {
    event.register(
        // ID for the overlay
        'test/broken_blocks_counter',

        // Function for rendering
        (minecraft, gui, poseStack, partialTick, screenWidth, screenHeight) => {
            if (abilityUtil.hasPower(minecraft.player, 'test:count_broken_blocks')) {
                const brokenBlocks = palladium.getProperty(minecraft.player, BROKEN_BLOCKS);

                // Drawing text. Parameters: PoseStack, Text (as text component), X, Y, Color
                guiUtil.drawString(poseStack, Component.string("Broken Blocks: " + brokenBlocks), 10, 10, 0xffffff);

                // Drawing bar. Firstly we draw the empty one as a background
                // Parameters: Texture, Gui, PoseStack, x, y, U on the texture, V on the texture, width, height
                guiUtil.blit(BAR_TEXTURE, gui, poseStack, 10, 20, 0, 10, 182, 5);

                // Now drawing the progress of the broken blocks above it
                const progress = brokenBlocks / 20.0;
                guiUtil.blit(BAR_TEXTURE, gui, poseStack, 10, 20, 0, 15, 182 * progress, 5);
            }
        });
});
