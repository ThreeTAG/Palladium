package net.threetag.palladium.util.molang;

import team.unnamed.mocha.runtime.compiled.MochaCompiledFunction;
import team.unnamed.mocha.runtime.compiled.Named;

public interface ModifyStringFunction extends MochaCompiledFunction {

    String modify(@Named("value") String value);

}
