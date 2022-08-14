package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.world.InteractionHand;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.compat.kubejs.condition.ScriptableConditionEventJS;

public class PalladiumBinding {

    public static void swingArm(LivingEntityJS entity, InteractionHand hand) {
        entity.minecraftLivingEntity.swing(hand, true);
        new ScriptableConditionEventJS(Palladium.id("test"), null).post(ScriptType.SERVER, "palladium.condition.scriptable");
    }

}
