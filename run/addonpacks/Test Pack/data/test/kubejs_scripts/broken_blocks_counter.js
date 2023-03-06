// Name/key for the data, stored here to use everywhere else
const BROKEN_BLOCKS = 'broken_blocks';


// Firstly, register the property in the entity
PalladiumEvents.registerProperties((event) => {
    // Only register for players
    if (event.getEntityType() === "minecraft:player") {
        // Arguments: Key of the property, type of the property, default/starting value
        event.registerProperty(BROKEN_BLOCKS, 'integer', 0);
    }
});


// Event for breaking blocks
BlockEvents.broken((event) => {
    // Check if entity has the property
    if (palladium.hasProperty(event.player, BROKEN_BLOCKS) && abilityUtil.hasPower(event.player, 'test:count_broken_blocks')) {

        // Get value, add 1
        const value = palladium.getProperty(event.player, BROKEN_BLOCKS) + 1;

        // if broken blocks == 20
        if (value === 20) {

            // give the player 1 XP level because why not
            event.player.giveExperienceLevels(1);

            // Set to 0 again
            palladium.setProperty(event.player, BROKEN_BLOCKS, 0);
        } else {
            // else: save the increased value
            palladium.setProperty(event.player, BROKEN_BLOCKS, value);
        }
    }
});