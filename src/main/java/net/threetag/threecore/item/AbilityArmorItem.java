package net.threetag.threecore.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ability.AbilityGenerator;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.capability.ItemAbilityContainerProvider;
import net.threetag.threecore.addonpacks.item.ItemParser;
import net.threetag.threecore.client.renderer.entity.model.DummyBipedModel;
import net.threetag.threecore.client.renderer.entity.modellayer.*;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class AbilityArmorItem extends ArmorItem implements IAbilityProvider, IModelLayerProvider {

    private List<AbilityGenerator> abilityGenerators;
    public List layers = new LinkedList<>();

    public AbilityArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    public AbilityArmorItem setAbilities(List<AbilityGenerator> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public AbilityArmorItem addAbility(AbilityGenerator abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) DummyBipedModel.INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE.toString();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemAbilityContainerProvider(stack);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<IModelLayer> getModelLayers(IModelLayerContext context) {
        return this.layers;
    }

    public static AbilityArmorItem parse(JsonObject jsonObject, Item.Properties properties) {
        EquipmentSlotType slot = EquipmentSlotType.fromString(JSONUtils.getString(jsonObject, "slot"));
        if (slot.getSlotType() == EquipmentSlotType.Group.HAND)
            throw new JsonParseException("Slot type must be an armor slot!");

        JsonElement materialJson = jsonObject.get("armor_material");
        IArmorMaterial material = materialJson.isJsonPrimitive() ? ArmorMaterialRegistry.getArmorMaterial(materialJson.getAsString()) : ItemParser.parseArmorMaterial(materialJson.getAsJsonObject(), false);
        if (material == null)
            throw new JsonParseException("The armor material '" + materialJson.getAsString() + "' can not be found!");
        AbilityArmorItem item = new AbilityArmorItem(material, slot, properties);

        // Since items are registered before any resources are loaded I need to push this back using the resource callback
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ModelLayerLoader.POST_LOAD_CALLBACKS.add(() -> {
                if (JSONUtils.hasField(jsonObject, "layers")) {
                    if (jsonObject.get("layers").isJsonPrimitive()) {
                        IModelLayer layer = ModelLayerManager.parseLayer(jsonObject.get("layers"));

                        if (layer != null)
                            item.layers.add(layer);
                    } else {
                        JsonArray layersArray = JSONUtils.getJsonArray(jsonObject, "layers");

                        for (int i = 0; i < layersArray.size(); i++) {
                            IModelLayer layer = ModelLayerManager.parseLayer(layersArray.get(i));
                            if (layer != null)
                                item.layers.add(layer);
                        }
                    }
                }
            });
        });

        return item.setAbilities(JSONUtils.hasField(jsonObject, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(jsonObject, "abilities"), true) : null);
    }

}
