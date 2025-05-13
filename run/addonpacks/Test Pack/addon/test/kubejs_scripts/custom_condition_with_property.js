// Add custom condition
StartupEvents.registry('palladium:condition_serializer', (event) => {

    // ID of the condition will be: 'kubejs:testpack/test_condition'
    event.create('testpack/test_condition_with_property')
        .documentationDescription('This is a test condition, defined in a KubeJS script. The condition is active if the configured property is 4.')

        // Adding a configurable property for the condition that can be changed in the power json
        .addProperty("my_integer", "integer", 0, "This is a test integer, defined in a KubeJS script")

        // Handler for the condition, in this case the condition will be fullfilled when the entity is crouching
        .test((entity, properties) => {
            // Only active when configured property is 4
            return properties.get("my_integer") == 4;
        });

});