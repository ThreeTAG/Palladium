package net.threetag.palladium.event;

import net.neoforged.bus.api.Event;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;

import java.util.Map;

public class RegisterMoLangQueriesEvent extends Event {

    private final Map<String, MoLangQueryRegistry.QueryBuilder> queries;

    public RegisterMoLangQueriesEvent(Map<String, MoLangQueryRegistry.QueryBuilder> queries) {
        this.queries = queries;
    }

    public MoLangQueryRegistry.QueryBuilder getQuery(String queryName) {
        return this.queries.computeIfAbsent(queryName, s -> new MoLangQueryRegistry.QueryBuilder());
    }

    public MoLangQueryRegistry.QueryBuilder getVanillaQuery() {
        return this.getQuery("query");
    }

    public MoLangQueryRegistry.QueryBuilder getPalladiumQuery() {
        return this.getQuery(Palladium.MOD_ID);
    }

}
