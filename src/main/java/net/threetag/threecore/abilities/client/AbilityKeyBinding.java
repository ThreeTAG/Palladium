package net.threetag.threecore.abilities.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;

public class AbilityKeyBinding extends KeyBinding {

    public final int index;

    public AbilityKeyBinding(String description, IKeyConflictContext keyConflictContext, InputMappings.Type inputType, int keyCode, int index, String category) {
        super(description, keyConflictContext, inputType, keyCode, category);
        this.index = index;
    }
}
