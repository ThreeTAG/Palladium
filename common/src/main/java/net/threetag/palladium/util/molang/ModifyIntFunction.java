package net.threetag.palladium.util.molang;

import com.zigythebird.playeranim.lib.mochafloats.runtime.compiled.MochaCompiledFunction;
import com.zigythebird.playeranim.lib.mochafloats.runtime.compiled.Named;

public interface ModifyIntFunction extends MochaCompiledFunction {

    int modify(@Named("value") int value);

}
