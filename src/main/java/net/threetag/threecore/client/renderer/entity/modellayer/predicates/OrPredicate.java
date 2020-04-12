package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;
import net.threetag.threecore.client.renderer.entity.modellayer.ModelLayerManager;

import java.util.List;

public class OrPredicate implements IModelLayerPredicate {

    public final List<IModelLayerPredicate> predicates = Lists.newArrayList();

    public OrPredicate add(IModelLayerPredicate predicate) {
        this.predicates.add(predicate);
        return this;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        for (IModelLayerPredicate predicate : this.predicates) {
            if (predicate.test(context)) {
                return true;
            }
        }
        return false;
    }

    public static OrPredicate parse(JsonObject json) {
        JsonArray jsonArray = JSONUtils.getJsonArray(json, "predicates");
        OrPredicate predicate = new OrPredicate();

        for (int i = 0; i < jsonArray.size(); i++) {
            predicate.add(ModelLayerManager.parsePredicate(jsonArray.get(i).getAsJsonObject()));
        }

        return predicate;
    }

}
