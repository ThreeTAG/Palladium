
    StartupEvents.registry("palladium:abilities", event => {
        event.create("testpack/test_ability_custom_prop")
            .addProperty("selected_alien", "integer", 0, null)
            .firstTick((entity, entry, holder, enabled) => {
                console.log("old alien:" + entry.getProperty("selected_alien"))

                let new_alien = entry.getProperty("selected_alien") + 1;
                let result = entry.setProperty("selected_alien", new_alien)
                console.log("set alien result:" + result)

                console.log("new alien:" + entry.getProperty("selected_alien"))
            })
    })