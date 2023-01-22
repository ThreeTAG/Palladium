// Objective names to sync to the client
const OBJECTIVE_NAMES = [
    "TestScoreName",
];

// Registering the properties
PalladiumEvents.registerProperties((event) => {
    if (event.getEntityType() === "minecraft:player") {
        for (let i = 0; i < OBJECTIVE_NAMES.length; i++) {
            event.registerProperty(OBJECTIVE_NAMES[i], 'integer', 0);
        }
    }
});

// Checking every tick if scoreboard value has changed, and set property if needed
PlayerEvents.tick((event) => {
    for (let i = 0; i < OBJECTIVE_NAMES.length; i++) {
        copyScoreToProperty(event.player, OBJECTIVE_NAMES[i]);
    }
});

function getScore(entity, objectiveName) {
    const scoreboard = entity.getScoreboard();
    const objective = scoreboard.getObjective(objectiveName);

    if (objective) {
        return scoreboard.getOrCreatePlayerScore(entity.isPlayer() ? entity.getGameProfile().getName() : entity.getStringUUID(), objective).getScore();
    } else {
        return 0;
    }
}

function copyScoreToProperty(player, name) {
    const propertyValue = palladium.getProperty(player, name);
    const scoreValue = getScore(player, name);

    if (propertyValue != scoreValue) {
        palladium.setProperty(player, name, scoreValue);
    }
}