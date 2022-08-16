onEvent('item.right_click', (event) => {

  // Show player's SUPERPOWER [when rightclicking bread]
  if (event.getItem().getId() === 'minecraft:bread') {
    event.player.tell('Current Superpower: ' + event.player.data.powers.getSuperpower());
  }

  // Show all of player's powers (superpower, suit, etc.) [when rightclicking glow berries]
  if (event.getItem().getId() === 'minecraft:glow_berries') {
    event.player.tell('Current Powers:');
    event.player.data.powers.getPowers().forEach(element => {
      event.player.tell(' - ' + element);
    });
  }

  // Set player's superpower to Cape Test [when rightclicking apple]
  else if (event.getItem().getId() === 'minecraft:apple') {
    let superpower = 'test:cape_test';
    event.player.data.powers.setSuperpower(superpower);
    event.player.tell('New Superpower:' + superpower);
  }

  // Show ALL AVAILABLE powers [when rightclicking rabbit stew]
  else if (event.getItem().getId() === 'minecraft:rabbit_stew') {
    event.player.tell('All Powers:');
    event.level.data.powers.getPowers().forEach(element => {
      event.player.tell(' - ' + element);
    });
  }

  // Swing arm [when rightclicking bone]
  else if (event.getItem().getId() === 'minecraft:bone') {
    palladium.swingArm(event.player, event.getHand());
  }
});

// Add condition code for test:kubejs_test power
onEvent('palladium.condition.scriptable', (event) => {
  event.setHandler('test:scriptable_condition_test', (entity) => {
    return entity.isCrouching();
  });
});

// Add ability code for test:kubejs_test power
onEvent('palladium.ability.scriptable', (event) => {
  event.setTickHandler('test:scriptable_ability_test', (entity) => {
    entity.tell('Tick!');
  });

  event.setFirstTickHandler('test:scriptable_ability_test', (entity) => {
    entity.tell('First Tick!');
  });

  event.setLastTickHandler('test:scriptable_ability_test', (entity) => {
    entity.tell('Last Tick!');
  });
});