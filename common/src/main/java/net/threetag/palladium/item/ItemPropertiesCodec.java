package net.threetag.palladium.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.AddonObjectLoader;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemPropertiesCodec {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final Codec<Item.Properties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DataComponentMap.CODEC.optionalFieldOf("components", DataComponentMap.EMPTY).forGetter(p -> p.components.build()),
            PalladiumCodecs.listOrPrimitive(TabPlacement.CODEC).optionalFieldOf("tab", Collections.emptyList()).forGetter(p -> p instanceof ExtendedProperties ext ? ext.getTabs() : Collections.emptyList())
    ).apply(instance, (components, tabs) -> {
        var properties = new ExtendedProperties();

        if (AddonObjectLoader.ID_TO_SET != null) {
            properties.setId(AddonObjectLoader.resourceId(Registries.ITEM, AddonObjectLoader.ID_TO_SET));
            AddonObjectLoader.ID_TO_SET = null;
        }

        for (TypedDataComponent component : components) {
            properties.component(component.type(), component.value());
        }

        properties.tabs(tabs);

        return properties;
    }));

    public static class ExtendedProperties extends Item.Properties {

        private final List<TabPlacement> tabs = new ArrayList<>();

        public ExtendedProperties tab(TabPlacement tab) {
            this.tabs.add(tab);
            return this;
        }

        public ExtendedProperties tabs(Collection<TabPlacement> tabs) {
            this.tabs.addAll(tabs);
            return this;
        }

        public List<TabPlacement> getTabs() {
            return tabs;
        }
    }

}
