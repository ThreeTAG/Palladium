package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.VillagerProfessionBuilder;
import net.threetag.palladium.util.json.GsonUtil;

public class VillagerProfessionParser extends AddonParser<VillagerProfession> {

    public VillagerProfessionParser() {
        super(GSON, "villager_professions", Registry.VILLAGER_PROFESSION_REGISTRY);
    }

    @Override
    public AddonBuilder<VillagerProfession> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "top element");
        var builder = new VillagerProfessionBuilder(id);

        builder.poiType(GsonUtil.getAsResourceLocation(json, "poi_type"));
        builder.sound(GsonUtil.getAsResourceLocation(json, "sound_event"));

        return builder;
    }

}
