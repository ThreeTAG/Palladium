package net.threetag.palladium.util.molang;

import com.zigythebird.playeranim.lib.mochafloats.runtime.compiled.MochaCompiledFunction;
import com.zigythebird.playeranim.lib.mochafloats.runtime.compiled.Named;

public interface ModifyStringFunction extends MochaCompiledFunction {

    String modify(@Named("value") String value);

}
