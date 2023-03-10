StartupEvents.registry("palladium:abilities", event => {

    event.create("testpack/test_ability_with_property")

        // Adding a configurable property for the condition that can be changed in the power json
        .addProperty("some_important_number", "integer", 0, "This is a test integer, defined in a KubeJS script")

        // Adding documentation description
        .documentationDescription("This is a test ability, defined in a KubeJS script.")

        // Adding a unique property that can be modified by an ability's entry (idk the desc xD)
        .addUniqueProperty("a_unique_number", "integer", 0)

        .firstTick((entity, entry, holder, enabled) => {
            if (enabled) {
                // Tell player the configured number
                entity.tell("Some important number: " + entry.getPropertyByName("some_important_number"));

                entry.setUniquePropertyByName("a_unique_number", entry.getPropertyByName("a_unique_number") + parseInt("1"))
                entity.tell("A unique number: " + entry.getPropertyByName("a_unique_number"))
            }
        });
});