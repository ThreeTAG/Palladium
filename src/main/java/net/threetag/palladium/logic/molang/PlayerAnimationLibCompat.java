package net.threetag.palladium.logic.molang;

import com.zigythebird.playeranimcore.animation.AnimationController;
import team.unnamed.mocha.MochaEngine;

public class PlayerAnimationLibCompat {

    public static void setup(MochaEngine<AnimationController> engine, AnimationController controller) {
        MoLangQueryRegistry.QUERY_BUILDERS.forEach((factory) -> {
            if (!factory.name().equalsIgnoreCase("query")) {
                factory.add(engine, (EntityContext) controller);
            }
        });
    }

}
