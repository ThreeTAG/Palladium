package net.threetag.palladium.client;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class PalladiumKeyMappings {

    public static final String CATEGORY = "key.palladium.categories.abilities";
    public static AbilityKeyMapping[] ABILITY_KEYS = new AbilityKeyMapping[5];

    public static void init() {
        for (int i = 0; i < ABILITY_KEYS.length; i++) {
            KeyMappingRegistry.register(ABILITY_KEYS[i] = new AbilityKeyMapping("key.palladium.ability_" + i, i == 1 ? 86 : i == 2 ? 66 : i == 3 ? 78 : i == 4 ? 77 : i == 5 ? 44 : -1, CATEGORY, i));
        }
    }

    public static class AbilityKeyMapping extends KeyMapping {

        public final int index;

        public AbilityKeyMapping(String description, int keyCode, String category, int index) {
            super(description, keyCode, category);
            this.index = index;
        }
    }

}
