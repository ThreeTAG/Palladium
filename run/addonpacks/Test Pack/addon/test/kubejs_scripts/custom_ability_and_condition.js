// Add custom ability
StartupEvents.registry('palladium:abilities', (event) => {

    // ID of the ability will be: 'kubejs:testpack/test_ability'
    event.create('testpack/test_ability')

        // Preset icon, can also be changed individually in the power json
        .icon(palladium.createItemIcon('minecraft:bread'))

        // Documentation description
        .documentationDescription('This is a test ability, defined in a KubeJS script.')

        // Handler for what happens during the FIRST tick of the ability being active
        .firstTick((entity, entry, holder, enabled) => {
            entity.tell('First Tick!');
        })

        // Handler for what happens during EVERY tick of the ability being active, make sure to check the 'enabled' parameter
        .tick((entity, entry, holder, enabled) => {
            if (enabled) {
                entity.tell('Tick!');
            }
        })

        // Handler for what happens during the LAST tick of the ability being active
        .lastTick((entity, entry, holder, enabled) => {
            entity.tell('Last Tick!');
        });

});


// Add custom condition
StartupEvents.registry('palladium:condition_serializer', (event) => {

    // ID of the condition will be: 'kubejs:testpack/test_condition'
    event.create('testpack/test_condition')
        .documentationDescription('This is a test condition, defined in a KubeJS script. The condition is active if an entity is crouching.')

        // Handler for the condition, in this case the condition will be fullfilled when the entity is crouching
        .test((entity, properties) => {
            return entity.isCrouching();
        });

});