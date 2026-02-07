package net.threetag.palladium.documentation;

import net.minecraft.core.HolderLookup;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.entity.flight.SwingingFlightType;
import net.threetag.palladium.util.NumberComparator;

public interface Documented<T, R extends T> {

    SettingType TYPE_ANY = SettingType.simple("Anything");
    SettingType TYPE_STRING = SettingType.simple("String");
    SettingType TYPE_STRING_ARRAY = SettingType.simple("string[]");
    SettingType TYPE_IDENTIFIER = SettingType.simple("Identifier");
    SettingType TYPE_BLOCK_STATE = SettingType.simple("Block State");
    SettingType TYPE_BLOCK_TAG = SettingType.simple("Block Tag");
    SettingType TYPE_FLUID_TAG = SettingType.simple("Fluid Tag");
    SettingType TYPE_BOOLEAN = SettingType.simple("Boolean");
    SettingType TYPE_INT = SettingType.simple("Integer");
    SettingType TYPE_NON_NEGATIVE_INT = SettingType.simple("Integer (>= 0)");
    SettingType TYPE_POSITIVE_INT = SettingType.simple("Integer (> 0)");
    SettingType TYPE_FLOAT = SettingType.simple("Float");
    SettingType TYPE_NON_NEGATIVE_FLOAT = SettingType.simple("Float (>= 0.0)");
    SettingType TYPE_POSITIVE_FLOAT = SettingType.simple("Float (> 0.0)");
    SettingType TYPE_DOUBLE = SettingType.simple("Double");
    SettingType TYPE_VECTOR3 = SettingType.simple("Vector 3D");
    SettingType TYPE_VECTOR2 = SettingType.simple("Vector 2D");
    SettingType TYPE_TIME_STRING = SettingType.simple("Time String (\"40s\", \"2m\", etc.)");
    SettingType TYPE_TIME = SettingType.combined(TYPE_INT, TYPE_TIME_STRING);
    SettingType TYPE_DYNAMIC_TEXTURE = SettingType.simple("Dynamic Texture");
    SettingType TYPE_TEXTURE_REFERENCE = SettingType.simple("Texture Reference");
    SettingType TYPE_ANY_TEXTURE = SettingType.combined(TYPE_IDENTIFIER, TYPE_DYNAMIC_TEXTURE, TYPE_TEXTURE_REFERENCE);
    SettingType TYPE_TEXT_COMPONENT = SettingType.simple("Text Component");
    SettingType TYPE_NBT = SettingType.simple("NBT");
    SettingType TYPE_PLAYER_SLOT = SettingType.enumList(PlayerSlot.exampleValues().stream().map(Object::toString).toList());
    SettingType TYPE_RENDER_LAYERS = SettingType.simple("Render Layers");
    SettingType TYPE_LASER_RENDERER = SettingType.simple("Laser Renderer Settings");
    SettingType TYPE_GEO_ANIMATION_CONTROLLER = SettingType.listOrPrimitive("Animation Controller");
    SettingType TYPE_MOLANG = SettingType.simple("MoLang");
    SettingType TYPE_MAP_VARIABLES = SettingType.simple("Map<String, Path Variable>");
    SettingType TYPE_ABILITY_REFERENCE = SettingType.simple("Ability Reference");
    SettingType TYPE_ENERGY_BAR_REFERENCE = SettingType.simple("Energy Bar Reference");
    SettingType TYPE_CONDITION_LIST = SettingType.listOrPrimitive("Condition");
    SettingType TYPE_VALUE = SettingType.simple("(Dynamic) Value");
    SettingType TYPE_DEFAULT_FLIGHT_ANIMATION = SettingType.simple("Default Flight Animation Settings");
    SettingType TYPE_SWINGING_ANIMATION = SettingType.simple("Swinging Animation Settings");
    SettingType TYPE_SWINGING_HEIGHT_TYPE = SettingType.enumList(SwingingFlightType.MaxHeightType.values());
    SettingType TYPE_COLOR = SettingType.simple("Color");
    SettingType TYPE_NUMBER_COMPARATOR = SettingType.enumList(NumberComparator.values());
    SettingType TYPE_INGREDIENT = SettingType.simple("Ingredient / Item");
    SettingType TYPE_ICON = SettingType.simple("Icon definition");
    SettingType TYPE_UI_PROPERTIES = SettingType.simple("UI Properties");
    SettingType TYPE_DIALOG_ACTION = SettingType.simple("Dialog Action");
    SettingType TYPE_ITEM_STACK = SettingType.simple("Item / ItemStack");

    SettingType TYPE_ATTRIBUTE = SettingType.simple("Attribute ID");
    SettingType TYPE_DAMAGE_TYPE = SettingType.simple("Damage Type ID");
    SettingType TYPE_PARTICLE_TYPE = SettingType.simple("Particle Type ID");
    SettingType TYPE_ABILITY_TYPE = SettingType.simple("Ability Type ID");
    SettingType TYPE_FLIGHT_TYPE = SettingType.simple("Flight Type ID");
    SettingType TYPE_DIMENSION = SettingType.simple("Dimension ID");
    SettingType TYPE_POWER = SettingType.simple("Power ID");
    SettingType TYPE_DAMAGE_TYPE_HOLDER_SET = SettingType.simple("Damage Type ID(s) / Tag(s)");
    SettingType TYPE_ABILITY_TYPE_HOLDER_SET = SettingType.simple("Ability Type ID(s) / Tag(s)");
    SettingType TYPE_ENTITY_TYPE_HOLDER_SET = SettingType.simple("Entity Type ID(s) / Tag(s)");
    SettingType TYPE_MOB_EFFECT_TYPE_HOLDER_SET = SettingType.simple("Mob Effect ID(s) / Tag(s)");
    SettingType TYPE_POWER_HOLDER_SET = SettingType.simple("Power ID(s) / Tag(s)");
    SettingType TYPE_FLIGHT_TYPE_HOLDER_SET = SettingType.simple("Flight Type ID(s) / Tag(s)");
    SettingType TYPE_CUSTOMIZATION_HOLDER_SET = SettingType.simple("Customization ID(s) / Tag(s)");

    CodecDocumentationBuilder<T, R> getDocumentation(HolderLookup.Provider provider);

}
