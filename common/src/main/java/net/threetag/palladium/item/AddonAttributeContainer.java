package net.threetag.palladium.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.threetag.palladium.util.PlayerSlot;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AddonAttributeContainer {

    private final Multimap<Attribute, AttributeModifier> allSlots = ArrayListMultimap.create();
    private final Map<PlayerSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers = new HashMap<>();

    public Multimap<Attribute, AttributeModifier> get(PlayerSlot slot, Multimap<Attribute, AttributeModifier> original) {
        Multimap<Attribute, AttributeModifier> finalMap = ArrayListMultimap.create();
        finalMap.putAll(original);
        finalMap.putAll(allSlots);

        if (this.attributeModifiers.containsKey(slot)) {
            finalMap.putAll(this.attributeModifiers.get(slot));
        }

        return finalMap;
    }

    public AddonAttributeContainer add(@Nullable PlayerSlot slot, Attribute attribute, AttributeModifier modifier) {
        if (slot != null) {
            this.attributeModifiers.computeIfAbsent(slot, s -> ArrayListMultimap.create()).put(attribute, modifier);
        } else {
            this.allSlots.put(attribute, modifier);
        }

        return this;
    }

    public AddonAttributeContainer addForAllSlots(Attribute attribute, AttributeModifier modifier) {
        this.allSlots.put(attribute, modifier);
        return this;
    }

}
