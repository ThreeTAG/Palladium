package net.threetag.palladium.logic.molang;

import com.zigythebird.playeranimcore.molang.MochaMathExtensions;
import com.zigythebird.playeranimcore.molang.QueryBinding;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.event.RegisterMoLangQueriesEvent;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.parser.ParseException;
import team.unnamed.mocha.runtime.value.Function;
import team.unnamed.mocha.runtime.value.NumberValue;
import team.unnamed.mocha.runtime.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class MoLangQueryRegistry {

    private static final Consumer<ParseException> HANDLER = (e) -> AddonPackLog.warning("Failed to parse!", e);
    static final Map<String, QueryBuilder> QUERY_BUILDERS = new HashMap<>();

    @SubscribeEvent
    static void setup(FMLCommonSetupEvent e) {
        NeoForge.EVENT_BUS.post(new RegisterMoLangQueriesEvent(QUERY_BUILDERS));
    }

    public static <T extends EntityContext> MochaEngine<T> create(T context) {
        MochaEngine<T> engine = createBaseEngine(context);

        QUERY_BUILDERS.forEach((namespace, builder) -> {
            QueryBinding<T> queryBinding = new QueryBinding<>(context);
            builder.build(queryBinding);
            queryBinding.block();
            engine.scope().set(namespace, queryBinding);
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

    public static class QueryBuilder {

        private final Map<String, DoubleQueryHandler> doubleQueries = new HashMap<>();
        private final Map<String, BooleanQueryHandler> booleanQueries = new HashMap<>();

        public QueryBuilder setDouble(String name, DoubleQueryHandler handler) {
            this.doubleQueries.put(name, handler);
            return this;
        }

        public QueryBuilder setBoolean(String name, BooleanQueryHandler handler) {
            this.booleanQueries.put(name, handler);
            return this;
        }

        public void build(QueryBinding<? extends EntityContext> binding) {
            this.doubleQueries.forEach((name, handler) -> {
                binding.set(name, (team.unnamed.mocha.runtime.value.Function<EntityContext>) (ctx, args) -> NumberValue.of(handler.get(ctx.entity(), args)));
            });

            this.booleanQueries.forEach((name, handler) -> {
                binding.set(name, (team.unnamed.mocha.runtime.value.Function<EntityContext>) (ctx, args) -> Value.of(handler.get(ctx.entity(), args)));
            });
        }
    }

    public interface DoubleQueryHandler {

        double get(EntityContext ctx, Function.Arguments args);

    }

    public interface BooleanQueryHandler {

        boolean get(EntityContext ctx, Function.Arguments args);

    }

}
