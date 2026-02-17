package net.threetag.palladium.logic.molang;

import com.zigythebird.playeranimcore.molang.MochaMathExtensions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.event.RegisterMoLangQueriesEvent;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.parser.ParseException;
import team.unnamed.mocha.runtime.binding.JavaObjectBinding;
import team.unnamed.mocha.runtime.value.ObjectValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class MoLangQueryRegistry {

    private static final Consumer<ParseException> HANDLER = (e) -> AddonPackLog.warning("Failed to parse!", e);
    static final List<QueryFactory<?>> QUERY_BUILDERS = new ArrayList<>();

    @SubscribeEvent
    static void setup(FMLCommonSetupEvent e) {
        NeoForge.EVENT_BUS.post(new RegisterMoLangQueriesEvent(QUERY_BUILDERS));
    }

    public static <T extends EntityContext> MochaEngine<T> create(T context) {
        MochaEngine<T> engine = createBaseEngine(context);

        QUERY_BUILDERS.forEach((factory) -> {
            factory.add(engine, context);
        });

        return engine;
    }

    public static <T> MochaEngine<T> createBaseEngine(T entity) {
        MochaEngine<T> engine = MochaEngine.createStandard(entity);
        engine.handleParseExceptions(HANDLER);
        engine.warnOnReflectiveFunctionUsage(false);
        engine.scope().set("math", new MochaMathExtensions(engine.scope().getProperty("math")));
        return engine;
    }

    public record QueryFactory<T extends ObjectValue>(String name, Class<T> clazz,
                                                      java.util.function.Function<EntityContext, T> factory) {

        @SuppressWarnings("UnstableApiUsage")
        public void add(MochaEngine<?> engine, EntityContext context) {
            var binding = JavaObjectBinding.of(this.clazz(), this.factory().apply(context), null);
            engine.scope().set(this.name(), binding);

            if (this.name().equals("query")) {
                engine.scope().set("q", binding);
            }
        }

    }

}
