// Objectives names to receive from the server
const OBJECTIVE_NAMES = [
    "TestScoreName",
];

PalladiumEvents.registerPropertiesClientSided((event) => {
    if (event.getEntityType() === "minecraft:player") {
        for (let i = 0; i < OBJECTIVE_NAMES.length; i++) {
            event.registerProperty(OBJECTIVE_NAMES[i], 'integer', 0);
        }
    }
});