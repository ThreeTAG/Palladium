package net.threetag.threecore.util.scripts.bindings;

import net.threetag.threecore.util.threedata.*;

public class ThreeDataBuilder {

    public ThreeData<?> create(String name, String type) {
        if (type.equalsIgnoreCase("integer"))
            return new IntegerThreeData(name);
        if (type.equalsIgnoreCase("float"))
            return new FloatThreeData(name);
        if (type.equalsIgnoreCase("boolean"))
            return new BooleanThreeData(name);
        if (type.equalsIgnoreCase("block"))
            return new BlockThreeData(name);
        if (type.equalsIgnoreCase("attributeOperation"))
            return new AttributeOperationThreeData(name);
        if (type.equalsIgnoreCase("attribute"))
            return new AttributeThreeData(name);
        if (type.equalsIgnoreCase("color"))
            return new ColorThreeData(name);
        if (type.equalsIgnoreCase("commandList"))
            return new CommandListThreeData(name);
        if (type.equalsIgnoreCase("equipmentSlot"))
            return new EquipmentSlotThreeData(name);
        if (type.equalsIgnoreCase("experience"))
            return new ExperienceThreeData(name);
        if (type.equalsIgnoreCase("hotbarElement"))
            return new HotbarElementThreeData(name);
        if (type.equalsIgnoreCase("icon"))
            return new IconThreeData(name);
        if (type.equalsIgnoreCase("itemStack"))
            return new ItemStackThreeData(name);
        if (type.equalsIgnoreCase("potion"))
            return new PotionThreeData(name);
        if (type.equalsIgnoreCase("resourceLocation"))
            return new ResourceLocationThreeData(name);
        if (type.equalsIgnoreCase("sizeChangeType"))
            return new SizeChangeTypeThreeData(name);
        if (type.equalsIgnoreCase("string"))
            return new StringThreeData(name);
        if (type.equalsIgnoreCase("textComponent"))
            return new TextComponentThreeData(name);
        if (type.equalsIgnoreCase("uuid"))
            return new UUIDThreeData(name);
        return new StringThreeData(name);
    }

}
