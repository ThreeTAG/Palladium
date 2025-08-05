package net.threetag.palladium.documentation;

import net.minecraft.core.HolderLookup;
import net.threetag.palladium.entity.PlayerSlot;

public interface Documented<T, R extends T> {

    SettingType TYPE_STRING = SettingType.simple("string");
    SettingType TYPE_STRING_ARRAY = SettingType.simple("string[]");
    SettingType TYPE_RESOURCE_LOCATION = SettingType.simple("ResourceLocation");
    SettingType TYPE_BLOCK_TAG = SettingType.simple("Block Tag");
    SettingType TYPE_FLUID_TAG = SettingType.simple("Fluid Tag");
    SettingType TYPE_BOOLEAN = SettingType.simple("boolean");
    SettingType TYPE_INT = SettingType.simple("integer");
    SettingType TYPE_FLOAT = SettingType.simple("float");
    SettingType TYPE_DOUBLE = SettingType.simple("double");
    SettingType TYPE_VECTOR3 = SettingType.simple("Vector 3D");
    SettingType TYPE_VECTOR2 = SettingType.simple("Vector 2D");
    SettingType TYPE_DYNAMIC_TEXTURE = SettingType.simple("Dynamic Texture");
    SettingType TYPE_TEXTURE_REFERENCE = SettingType.simple("Texture Reference");
    SettingType TYPE_ANY_TEXTURE = SettingType.combined(TYPE_RESOURCE_LOCATION, TYPE_DYNAMIC_TEXTURE, TYPE_TEXTURE_REFERENCE);
    SettingType TYPE_TEXT_COMPONENT = SettingType.simple("Text Component");
    SettingType TYPE_NBT = SettingType.simple("NBT");
    SettingType TYPE_PLAYER_SLOTS = SettingType.enumList(PlayerSlot.exampleValues().stream().map(Object::toString).toList());
    SettingType TYPE_RENDER_LAYERS = SettingType.simple("Render Layers");
    SettingType TYPE_LASER_RENDERER = SettingType.simple("Laser Renderer Settings");
    SettingType TYPE_GEO_ANIMATION_CONTROLLER = SettingType.listOrPrimitive("Animation Controller");
    SettingType TYPE_MOLANG = SettingType.simple("MoLang");
    SettingType TYPE_MAP_VARIABLES = SettingType.simple("Map<String, Path Variable>");
    SettingType TYPE_ABILITY_REFERENCE = SettingType.simple("Ability Reference");
    SettingType TYPE_ENERGY_BAR_REFERENCE = SettingType.simple("Energy Bar Reference");
    SettingType TYPE_CONDITION_LIST = SettingType.listOrPrimitive("Condition");

    SettingType TYPE_ATTRIBUTE = SettingType.simple("Attribute ID");
    SettingType TYPE_DAMAGE_TYPE = SettingType.simple("Damage Type ID");
    SettingType TYPE_DAMAGE_TYPE_ID_OR_TAG = SettingType.simple("Damage Type ID or Tag");
    SettingType TYPE_PARTICLE_TYPE = SettingType.simple("Particle Type ID");

    CodecDocumentationBuilder<T, R> getDocumentation(HolderLookup.Provider provider);

}
