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
    if (palladium.hasProperty(event.player, BROKEN_BLOCKS)) {
        const value = palladium.getProperty(event.player, BROKEN_BLOCKS) + 1;
        // Set it to previous value + 1
        palladium.setProperty(event.player, BROKEN_BLOCKS, value);
        // Tell the player!
        event.player.tell('You have broken ' + value + ' blocks so far!');
    }
});