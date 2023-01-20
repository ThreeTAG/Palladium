package net.threetag.palladium.loot;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootPool;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LootTableModificationManager extends SimpleJsonResourceReloadListener {

    private static LootTableModificationManager INSTANCE;
    // Forge does very stupid shit with loot tables, this is used in Mixins to get around it
    public static boolean OVERRIDE_FORGE_NAME_LOGIC = false;
    private static final Gson GSON = Deserializers.createLootTableSerializer().create();
    private Map<ResourceLocation, Modification> modifications = ImmutableMap.of();

    public LootTableModificationManager() {
        super(GSON, "palladium/loot_table_modifications");
    }

    public static LootTableModificationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LootTableModificationManager();
        }

        return INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, Modification> builder = ImmutableMap.builder();

        object.forEach((id, element) -> {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(element, "top element");
                Modification modification = new Modification(GsonUtil.getAsResourceLocation(json, "target"));
                AtomicInteger i = new AtomicInteger(-1);
                GsonUtil.forEachInListOrPrimitive(json.get("add_pools"), poolJson -> {
                    i.getAndIncrement();
                    JsonObject jsonobject = GsonHelper.convertToJsonObject(poolJson, "loot pool");
                    if (!jsonobject.has("name")) {
                        throw new JsonParseException("Loot Table Modification pool \"" + id + "\" Missing `name` entry for pool #" + i.get());
                    }

                    OVERRIDE_FORGE_NAME_LOGIC = true;
                    modification.addPool(GSON.fromJson(poolJson, LootPool.class));
                    OVERRIDE_FORGE_NAME_LOGIC = false;
                });
                builder.put(id, modification);
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading loot table modification {}", id, e);
            }
        });

        this.modifications = builder.build();
        AddonPackLog.info("Loaded {} loot table modifications", this.modifications.size());
    }

    @Nullable
    public Modification getFor(ResourceLocation tableId) {
        for (Modification modification : this.modifications.values()) {
            if (modification.getTargetTable().equals(tableId)) {
                return modification;
            }
        }

        return null;
    }

    public static class Modification {

        private final ResourceLocation targetTable;
        private final List<LootPool> lootPools = new ArrayList<>();

        public Modification(ResourceLocation targetTable) {
            this.targetTable = targetTable;
        }

        public Modification addPool(LootPool lootPool) {
            this.lootPools.add(lootPool);
            return this;
        }

        public ResourceLocation getTargetTable() {
            return targetTable;
        }

        public List<LootPool> getLootPools() {
            return lootPools;
        }
    }

}
