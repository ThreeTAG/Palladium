StartupEvents.registry("palladium:abilities", event => {

    event.create("testpack/test_ability_with_property")

        // Adding a configurable property for the condition that can be changed in the power json
        .addProperty("some_important_number", "integer", 0, "This is a test integer, defined in a KubeJS script")

        .firstTick((entity, entry, holder, enabled) => {
            if (enabled) {
                // Tell player the configured number
                entity.tell("Some important number: " + entry.getProperty("some_important_number"));
            }
        });
});