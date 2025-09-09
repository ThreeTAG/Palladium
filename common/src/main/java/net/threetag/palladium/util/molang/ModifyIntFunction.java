package net.threetag.palladium.util.molang;

import team.unnamed.mocha.runtime.compiled.MochaCompiledFunction;
import team.unnamed.mocha.runtime.compiled.Named;

public interface ModifyIntFunction extends MochaCompiledFunction {

    int modify(@Named("value") int value);

}
