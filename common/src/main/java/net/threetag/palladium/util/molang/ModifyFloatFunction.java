package net.threetag.palladium.util.molang;

import com.zigythebird.playeranim.lib.mochafloats.runtime.compiled.MochaCompiledFunction;
import com.zigythebird.playeranim.lib.mochafloats.runtime.compiled.Named;

public interface ModifyFloatFunction extends MochaCompiledFunction {

    float modify(@Named("value") float value);

}
