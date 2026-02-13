package net.threetag.palladium.logic.molang;

import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.molang.QueryBinding;
import team.unnamed.mocha.MochaEngine;

public class PlayerAnimationLibCompat {

    public static void setup(MochaEngine<AnimationController> engine, AnimationController controller) {
        MoLangQueryRegistry.QUERY_BUILDERS.forEach((namespace, builder) -> {
            if (!namespace.equalsIgnoreCase("query")) {
                QueryBinding<? extends EntityContext> queryBinding = new QueryBinding<>((EntityContext) controller);
                builder.build(queryBinding);
                queryBinding.block();
                engine.scope().set(namespace, queryBinding);
            }
        });
    }

}
