package net.threetag.palladium.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrySynonymsHandler {

    private static final Map<ResourceKey<?>, List<Synonym>> SYNONYMS = new HashMap<>();

    static {
        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:step_height_addition"), new ResourceLocation("forge:step_height_addition"));
        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:entity_gravity"), new ResourceLocation("forge:entity_gravity"));
        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:swim_speed"), new ResourceLocation("forge:swim_speed"));

        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:reach_distance"), new ResourceLocation("forge:block_reach"));
        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:attack_range"), new ResourceLocation("forge:entity_reach"));
        register(Registries.ATTRIBUTE, new ResourceLocation("reach-entity-attributes:reach"), new ResourceLocation("forge:block_reach"));
        register(Registries.ATTRIBUTE, new ResourceLocation("reach-entity-attributes:attack_range"), new ResourceLocation("forge:entity_reach"));
        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:reach_distance"), new ResourceLocation("reach-entity-attributes:reach"));
        register(Registries.ATTRIBUTE, new ResourceLocation("porting_lib:attack_range"), new ResourceLocation("reach-entity-attributes:attack_range"));
    }

    public static void register(ResourceKey<?> registry, ResourceLocation id1, ResourceLocation id2) {
        SYNONYMS.computeIfAbsent(registry, (r) -> new ArrayList<>()).add(new Synonym(id1, id2));
    }

    public static ResourceLocation getReplacement(Registry<?> registry, ResourceLocation id) {
        if (!registry.containsKey(id)) {
            List<Synonym> list = SYNONYMS.get(registry.key());

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
