package net.threetag.palladium.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrySynonymsHandler {

    private static final Map<Registry<?>, List<Synonym>> SYNONYMS = new HashMap<>();

    static {
        register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation("porting_lib:step_height_addition"), new ResourceLocation("forge:step_height_addition"));
        register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation("porting_lib:entity_gravity"), new ResourceLocation("forge:entity_gravity"));
        register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation("porting_lib:swim_speed"), new ResourceLocation("forge:swim_speed"));
    }

    public static void register(Registry<?> registry, ResourceLocation id1, ResourceLocation id2) {
        SYNONYMS.computeIfAbsent(registry, (r) -> new ArrayList<>()).add(new Synonym(id1, id2));
    }

    public static ResourceLocation getReplacement(Registry<?> registry, ResourceLocation id) {
        if (!registry.containsKey(id)) {
            List<Synonym> list = SYNONYMS.get(registry);

            if (list != null) {
                for (Synonym synonym : list) {
                    if (synonym.id1.equals(id) && registry.containsKey(synonym.id2)) {
                        return synonym.id2;
                    }
                    if (synonym.id2.equals(id) && registry.containsKey(synonym.id1)) {
                        return synonym.id1;
                    }
                }
            }
        }

        return id;
    }

    static class Synonym {

        private final ResourceLocation id1, id2;

        public Synonym(ResourceLocation id1, ResourceLocation id2) {
            this.id1 = id1;
            this.id2 = id2;
        }
    }

}
