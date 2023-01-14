
    StartupEvents.registry("palladium:abilities", event => {
        event.create("testpack/test_ability_custom_prop")
            .addProperty("some_important_number", "integer", 0, null)
            .firstTick((entity, entry, holder, enabled) => {
                // console.log prints out to the log text files
                console.log("my custom property:" + entry.getProperty("some_important_number"))

                // note that you cannot set property, as the property just added can never be changed.
                let new_alien = entry.getProperty("some_important_number") + 1;
                let result = entry.setProperty("some_important_number", new_alien)
                console.log("setProperty result:" + result) // result always prints out as false, because the property CANNOT be changed
            })
    })