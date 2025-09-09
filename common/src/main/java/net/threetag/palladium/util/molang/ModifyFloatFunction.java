package net.threetag.palladium.util.molang;

import team.unnamed.mocha.runtime.compiled.MochaCompiledFunction;
import team.unnamed.mocha.runtime.compiled.Named;

public interface ModifyFloatFunction extends MochaCompiledFunction {

    float modify(@Named("value") float value);

}
